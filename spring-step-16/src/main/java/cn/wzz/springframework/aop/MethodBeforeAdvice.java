package cn.wzz.springframework.aop;

import java.lang.reflect.Method;

/**
* Spring框架中, Advice都是通过方法拦截器MethodInterceptor实现;<br/>
* 环绕Advice类似一个拦截器的链路, BeforeAdvice、AfterAdvice等
 * */
public interface MethodBeforeAdvice extends BeforeAdvice{

    /**
     * 在给定方法method被调用前回调before方法
     * @param method 正在被调用的方法
     * @param args 方法的入参
     * @param target 方法调用的目标
     * @throws Exception
     */
    void before(Method method, Object[] args, Object target) throws Exception;
}
