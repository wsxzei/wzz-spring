package cn.wzz.springframework.core.convert.support;

import cn.wzz.springframework.core.convert.converter.Converter;
import cn.wzz.springframework.core.convert.converter.ConverterFactory;
import cn.wzz.springframework.util.NumberUtils;

public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {
    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {

        return new StringToNumber<T>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T>{
        private Class<T> targetClass;

        public StringToNumber(Class<T> targetClass){
            this.targetClass = targetClass;
        }

        @Override
        public T convert(String source) {
            if(source.isEmpty()){
                return null;
            }
            return NumberUtils.parseNumber(source, targetClass);
        }
    }
}
