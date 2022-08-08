package cn.wzz.springframework.context.support;

import cn.wzz.springframework.beans.factory.FactoryBean;
import cn.wzz.springframework.beans.factory.InitializingBean;
import cn.wzz.springframework.core.convert.ConversionService;
import cn.wzz.springframework.core.convert.converter.Converter;
import cn.wzz.springframework.core.convert.converter.ConverterFactory;
import cn.wzz.springframework.core.convert.converter.ConverterRegistry;
import cn.wzz.springframework.core.convert.converter.GenericConverter;
import cn.wzz.springframework.core.convert.support.DefaultConversionService;
import cn.wzz.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    // 可以在配置文件中通过引用的方式注入, 使用另一个FactoryBean<Set<?>>
    private Set<?> converters;

    private GenericConversionService conversionService;

    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    /**
     * 在 doCreateBean#initializeBean 阶段被调用, 执行初始化操作:
     * 1. 实例化转换服务对象, 它是转换系统的入口点;
     * 2. DefaultConversionService对象还是转换器的注册点, 将converters集合中的转换器注册到
      */
    public void afterPropertiesSet() throws Exception {
        this.conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    // 将集合中的转换器注册到registry
    private void registerConverters(Set<?> converters, ConverterRegistry registry){
        if(converters != null){
            for(Object converter : converters){
                if(converter instanceof Converter){
                    registry.addConverter((Converter<?, ?>) converter);
                }else if(converter instanceof GenericConverter){
                    registry.addConverter((GenericConverter) converter);
                }else if(converter instanceof ConverterFactory){
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                }else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }
}
