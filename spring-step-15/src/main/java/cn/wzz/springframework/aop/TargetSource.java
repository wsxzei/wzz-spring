package cn.wzz.springframework.aop;

import cn.wzz.springframework.util.ClassUtils;

public class TargetSource {
    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    /**
     * Return the type of targets returned by this {@link TargetSource}.
     * <p>Can return <code>null</code>, although certain usages of a
     * <code>TargetSource</code> might just work with a predetermined
     * target class.
     * @return the type of targets returned by this {@link TargetSource}
     */
    public Class<?>[] getTargetClass(){
        Class<?> targetClass = target.getClass();
        /* 需要判断target是否为Cglib实例化生成的对象 */
        targetClass = ClassUtils.isCglibProxyClass(targetClass)? targetClass.getSuperclass():targetClass;
        Class<?>[] interfaces = targetClass.getInterfaces();
        return interfaces;
    }

    /**
     * Return a target instance. Invoked immediately before the
     * AOP framework calls the "target" of an AOP method invocation.
     * @return the target object, which contains the joinpoint
     * @throws Exception if the target object can't be resolved
     */
    public Object getTarget(){
        return this.target;
    }
}
