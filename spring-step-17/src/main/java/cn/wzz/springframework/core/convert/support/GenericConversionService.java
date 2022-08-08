package cn.wzz.springframework.core.convert.support;

import cn.wzz.springframework.core.convert.ConversionService;
import cn.wzz.springframework.core.convert.converter.Converter;
import cn.wzz.springframework.core.convert.converter.ConverterFactory;
import cn.wzz.springframework.core.convert.converter.ConverterRegistry;
import cn.wzz.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class GenericConversionService implements ConversionService, ConverterRegistry {

    // 保存能在指定(sourceType, targetType)间进行数据转换的通用转换器
    private Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        GenericConverter converter = getConverter(sourceType, targetType);
        return converter != null;
    }

    /**
     *  获取通用转换器:
     *  1. 先获取sourceType和targetType的类层次
     *  2. 对每一个原类型候选者sourceCandidate遍历targetCandidates集合, 找到能到适用的转换器, 直接返回
     *  */
    private GenericConverter getConverter(Class<?> sourceType, Class<?> targetType){
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        for(Class<?> sourceCandidate: sourceCandidates){
            for(Class<?> targetCandidate: targetCandidates){
                GenericConverter.ConvertiblePair convertiblePair = new
                        GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
                GenericConverter converter = converters.get(convertiblePair);
                if(converter != null)
                    return converter;
            }
        }
        return null;
    }

    /* 获取类的层次 */
    private List<Class<?>> getClassHierarchy(Class<?> clazz){
        List<Class<?>> hierarchy = new ArrayList<>();
        while(clazz != null){
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();
        // 获取到的converter可能将对象转换为targetType的超类?
        GenericConverter converter = getConverter(sourceType, targetType);
        return (T) converter.convert(source, sourceType, targetType);
    }

    @Override
    // 将converter包装为为通用转换器GenericConverter类型后, 加入到converters容器中
    public void addConverter(Converter<?, ?> converter) {
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converter);
        // GenericConverter的适配器包装类
        ConverterAdapter converterAdapter = new ConverterAdapter(typeInfo, converter);
        for(GenericConverter.ConvertiblePair convertiblePair: converterAdapter.getConvertibleTypes()){
            converters.put(convertiblePair, converterAdapter);
        }
    }

    // 获取converter实现类的参数化类型的接口, 从类型参数中获取需要转换数据的源类型和目标类型, 用于构造ConvertiblePair对象
    private GenericConverter.ConvertiblePair getRequiredTypeInfo(Object object){
        Type[] types = object.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];

        // 获取接口Converter<S, T>中类型参数 S, T 或 ConverterFactory<S, R>中参数类型 S, RT
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class<?> sourceType = (Class<?>) actualTypeArguments[0];
        Class<?> targetType = (Class<?>) actualTypeArguments[1];
        return new GenericConverter.ConvertiblePair(sourceType, targetType);
    }

    @Override
    public void addConverter(GenericConverter converter) {
        for(GenericConverter.ConvertiblePair convertiblePair: converter.getConvertibleTypes()){
            converters.put(convertiblePair, converter);
        }
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        // 获取原类型和目标类型 ConverterFactory<S, R>
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converterFactory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(typeInfo, converterFactory);

        for(GenericConverter.ConvertiblePair convertiblePair: converterFactoryAdapter.getConvertibleTypes()){
            converters.put(convertiblePair, converterFactoryAdapter);
        }
    }

    private final class ConverterAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final Converter<Object, Object> converter;

        public ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = (Converter<Object, Object>) converter;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            // 将typeInfo作为不可变集合中唯一的元素, 返回该集合
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converter.convert(source);
        }
    }

    // 转换器工厂适配器, 适配通用转换器类型
    private final class ConverterFactoryAdapter implements GenericConverter{

        private GenericConverter.ConvertiblePair typeInfo;
        // 转换器工厂获取到的转换器作用的 源类型是 Object, 目标类型是Object或Object的某一子类
        private ConverterFactory<Object, Object> converterFactory;

        public ConverterFactoryAdapter(GenericConverter.ConvertiblePair typeInfo, ConverterFactory<?, ?> converterFactory){
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            // targetType类型一定是Object的子类, 因此满足转换器工厂的泛型限制
            return converterFactory.getConverter(targetType).convert(source);
        }
    }
}
