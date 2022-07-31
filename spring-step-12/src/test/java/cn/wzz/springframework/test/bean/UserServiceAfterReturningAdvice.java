package cn.wzz.springframework.test.bean;

import cn.wzz.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class UserServiceAfterReturningAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("Begin afterReturning method...");
        System.out.println( "被拦截对象: " + target + "\t方法: " + method.getName() + " 调用结束..");
        System.out.println("返回结果为: " + returnValue);
        System.out.println("End afterReturning method.");
    }
}
