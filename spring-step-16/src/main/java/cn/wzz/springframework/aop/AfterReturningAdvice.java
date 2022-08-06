package cn.wzz.springframework.aop;

import java.lang.reflect.Method;

public interface AfterReturningAdvice extends AfterAdvice{

    /* 四个参数依次为: 被拦截方法的返回值, 被拦截方法, 方法的入参, 目标对象 */
    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;
}
