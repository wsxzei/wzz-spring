package cn.wzz.springframework.core.convert.converter;

public interface ConverterFactory <S, R>{
    /**
     * Get the converter to convert from S to T, where T is an instance of R.
     * @param <T> the target type
     * @param targetType the target type to convert to
     * @return a converter from S to T
     */
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
