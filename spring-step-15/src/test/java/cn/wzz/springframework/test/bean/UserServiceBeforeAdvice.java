package cn.wzz.springframework.test.bean;

import cn.wzz.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class UserServiceBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Exception {
        System.out.println("拦截方法: " + method.getName());
    }
}
