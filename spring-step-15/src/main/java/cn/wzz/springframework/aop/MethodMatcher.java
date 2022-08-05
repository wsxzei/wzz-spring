package cn.wzz.springframework.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    /* 方法匹配, 找到与表达式匹配的目标类和方法 */
    boolean matches(Method method, Class<?> targetClass);
}
