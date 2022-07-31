package cn.wzz.springframework.test.bean;


import cn.wzz.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

// 用于注入 MethodBeforeAdviceInterceptor 中的 beforeAdvice 属性
public class UserServiceBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Exception {
        System.out.println("Begin before method...");
        System.out.println("拦截方法: " + method.getName());
        System.out.println("End before method.");
    }

}
