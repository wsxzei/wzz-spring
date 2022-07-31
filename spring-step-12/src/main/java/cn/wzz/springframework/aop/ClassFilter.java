package cn.wzz.springframework.aop;

public interface ClassFilter {

    /* 判断pointcut是否应用于给定的接口或目标类 */
    boolean matches(Class<?> clazz);
}
