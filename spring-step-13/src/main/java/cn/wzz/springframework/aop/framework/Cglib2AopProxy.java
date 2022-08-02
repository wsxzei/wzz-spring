package cn.wzz.springframework.aop.framework;

import cn.wzz.springframework.aop.AdvisedSupport;
import cn.wzz.springframework.aop.AopProxy;
import cn.wzz.springframework.aop.MethodMatcher;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Cglib2AopProxy implements AopProxy {

    // 保存被代理对对象的信息
    private final AdvisedSupport advised;

    public Cglib2AopProxy(AdvisedSupport advised){
        this.advised = advised;
    }

    @Override
    // Enhancer代理类在运行期间使用底层ASM字节码增强为目标对象生成生成对象，因此被代理类不需要实现任何接口
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    // 设置回调
    private static class DynamicAdvisedInterceptor implements MethodInterceptor{

        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advisedSupport){
            this.advised = advisedSupport;
        }

        @Override
        // o为代理对象; objects为方法的入参; method是目标类的方法, 可以直接使用反射对目标对象invoke
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            // 目标对象
            Object target = advised.getTargetSource().getTarget();
            // 描述方法调用的连接点, proceed方法就是调用methodProxy的
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(target, method, objects, methodProxy);
            // 方法匹配器
            MethodMatcher matcher = advised.getMethodMatcher();

            if(matcher.matches(method, target.getClass())){
                org.aopalliance.intercept.MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
                return methodInterceptor.invoke(methodInvocation);
            }
//            return method.invoke(target, objects);
            return methodInvocation.proceed();
        }
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation{

        /**
         * MethodProxy有invoke和invokeSuper两种方法
         * invoke需要传入目标对象, 使用代理对象会陷入死循环
         * invokeSuper需要传入代理对象, 用于调用父类(即目标对象)的方法
         */
        private MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }

        @Override
        public Object proceed() throws Throwable {
            // 不重写应该也可以
//            return super.proceed();
            return this.methodProxy.invoke(this.target, this.arguments);
        }
    }
}
