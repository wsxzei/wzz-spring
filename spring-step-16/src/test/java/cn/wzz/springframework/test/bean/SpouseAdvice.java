package cn.wzz.springframework.test.bean;

import cn.wzz.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class SpouseAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Exception {
        System.out.println("前置拦截, method: " + method);
    }
}
