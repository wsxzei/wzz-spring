package cn.wzz.springframework.core.convert.converter;

import cn.hutool.core.lang.Assert;

import java.util.Set;

public interface GenericConverter {

    /**
     * Return the source and target types that this converter can convert between.
     */
    Set<ConvertiblePair> getConvertibleTypes();

    /**
     * Convert the source object to the targetType.
     * @param source the source object to convert
     * @param sourceType  the type descriptor of the field we are converting from
     * @param targetType the type descriptor of the field we are converting to
     * @return the converted object
     */
    Object convert(Object source, Class sourceType, Class targetType);

    /* Holder for a source-to-target class pair. */
    final class ConvertiblePair{
        // enclosing class和inner class都可以相互访问对方的private属性
        private final Class<?> sourceType;

        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType){
            Assert.notNull(sourceType, "Source type must not be null.");
            Assert.notNull(targetType, "Target type must not be null!");
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        /* 重写equals方法 */

        @Override
        public boolean equals(Object obj) {
            // 内存地址相同是同一个对象, 判定相等
            if (this == obj)
                return true;
            // obj为空指针, 或者obj不是ConvertiblePair相同类型, 判定为不相等
            if (obj == null || ConvertiblePair.class != obj.getClass())
                return false;
            ConvertiblePair other = (ConvertiblePair) obj;
            // sourceType和targetType属性相同的ConvertibleType对象, 判定为相等
            return other.targetType == targetType && other.sourceType == sourceType;
        }

        @Override
        /**
         * 重写了equals方法必须重写hashCode, 需要满足如下三点:
         * 1. 一个对象多次调用hashCode方法返回相同的哈希值, 因此需要确保targetType和sourceType为final属性
         * 2. 两个对象通过equals判定相同, 需要返回相同的哈希值
         * 3. 拥有两个不相等哈希值的对象必须是不同对象
         */
        public int hashCode() {
            return targetType.hashCode() + sourceType.hashCode()*31;
        }
    }
}
