package cn.wzz.springframework.core.convert.converter;

public interface Converter <S, T>{
    /* Convert the source object of type S to target type T */
    T convert(S source);
}
