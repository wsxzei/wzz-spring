package cn.wzz.springframework.core.convert.converter;

public interface ConverterRegistry {

    /**
     * Add a plain converter to this registry.
     * The convertible source/target type pair is derived from the Converter's parameterized types.
     * 可转换的source/target类型由Converter接口的参数化类型指定.
     * @param converter
     */
    void addConverter(Converter<?,?> converter);

    /**
     * Add a generic converter to this registry.
     */
    void addConverter(GenericConverter converter);

    /**
     * Add a ranged converter factory to this registry.
     * The convertible source/target type pair is derived from the ConverterFactory's parameterized types.
     */
    void addConverterFactory(ConverterFactory<?,?> converterFactory);
}
