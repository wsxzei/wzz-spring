package cn.wzz.springframework.aop.framework;

import cn.wzz.springframework.aop.AdvisedSupport;
import cn.wzz.springframework.aop.AopProxy;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* 基于JDK实现的代理类, 分别实现接口AopProxy, InvocationHandler, 可以将代理对象的返回和反射调用方法invoke分开处理 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    // 传入被代理对象的信息
    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                advised.getTargetSource().getTargetClass(), this);
    }

    @Override
    /**
     * 对于匹配的方法, 用户可以自定义方法拦截后的操作;
     * 这些操作由advisedSupport的methodInterceptor属性决定
     * */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = advised.getTargetSource().getTarget();
        if(advised.getMethodMatcher().matches(method, target.getClass())){
            // 获取方法拦截器, 并使用它的invoke方法实现对方法的代理
            MethodInterceptor interceptor = advised.getMethodInterceptor();
            /*
             ReflectiveMethodInvocation保存的是 对静态连接点(方法)调用 的信息, 其实是一个入参的包装信息
             提供了目标对象、方法、入参; (是一个动态连接点，即发生在静态连接点上的事件)
             */
            return interceptor.invoke(new ReflectiveMethodInvocation(target, method, args));
        }

        return method.invoke(target, args);
    }
}
