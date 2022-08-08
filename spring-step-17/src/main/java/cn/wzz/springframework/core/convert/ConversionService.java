package cn.wzz.springframework.core.convert;

import com.sun.istack.internal.Nullable;

/**
 * A service interface for type conversion. This is the entry point into the convert system.
 * Call {@link #convert(Object, Class)} to perform a thread-safe type conversion using this system.
 */
public interface ConversionService {

    /** Return {@code true} if objects of {@code sourceType} can be converted to the {@code targetType}.<br/>
     * {@code sourceType}可以为空 */
    boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);

    /** Convert the given {@code source} to the specified {@code targetType}. */
    <T> T convert(Object source, Class<T> targetType);
}
