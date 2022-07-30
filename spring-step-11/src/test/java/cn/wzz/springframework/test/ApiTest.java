package cn.wzz.springframework.test;


import cn.wzz.springframework.aop.AdvisedSupport;
import cn.wzz.springframework.aop.MethodMatcher;
import cn.wzz.springframework.aop.TargetSource;
import cn.wzz.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.wzz.springframework.aop.framework.Cglib2AopProxy;
import cn.wzz.springframework.aop.framework.JdkDynamicAopProxy;
import cn.wzz.springframework.aop.framework.ReflectiveMethodInvocation;
import cn.wzz.springframework.test.bean.IUserService;
import cn.wzz.springframework.test.bean.UserService;
import cn.wzz.springframework.test.bean.UserServiceInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/** 11 版本
 * 1. test_aop:
 * 测试实现了ClassFilter和MethodMatcher接口的AspectJExpressionPointcut 是否能依据切点表达式正确的匹配到指定方法
 * 2. test_proxy_method:
 * 测试使用方法拦截器 MethodInterceptor 实现自定义的方法拦截后操作
 * */
public class ApiTest {
    @Test
    public void test_aop() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution" +
                "(* cn.wzz.springframework.test.bean.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("queryUserInfo");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method, clazz));
    }
/*
true
true*/
    @Test
    public void test_proxy_method(){
        // 目标对象
        Object target = new UserService();
        IUserService proxy = (IUserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                , target.getClass().getInterfaces(), new InvocationHandler() {
            // 定义方法匹配器
            MethodMatcher matcher = new AspectJExpressionPointcut(
                    "execution(* cn.wzz.springframework.test.bean.IUserService.*(..))");
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(matcher.matches(method, target.getClass())){
                    // 自定义方法拦截器, 对动态连接点invocation中的方法信息进行操作
                    MethodInterceptor interceptor = new MethodInterceptor() {
                        @Override
                        public Object invoke(MethodInvocation invocation) throws Throwable {
                            long start = System.currentTimeMillis();
                            try{
                            // 对于ReflectiveMethodInvocation, proceed就是直接执行目标对象的方法
                                return invocation.proceed();
                            }finally {
                                System.out.println("监控 - Begin By Aop");
                                System.out.println("方法名称: " + invocation.getMethod().getName());
                                System.out.println("方法耗时: " +
                                        (System.currentTimeMillis() - start) + "ms");
                                System.out.println("监控 - End\r\n");
                            }
                        }
                    };
                    return interceptor.invoke(new ReflectiveMethodInvocation(target, method, args));
                }
                // 如果方法不匹配, 直接反射调用原方法, 不进行方法增强
                return method.invoke(target, args);
            }
         });
        System.out.println("测试结果" + proxy.queryUserInfo());
    }
/* 运行结果
监控 - Begin By Aop
方法名称: queryUserInfo
方法耗时: 99ms
监控 - End

测试结果wzz 0001 杭州
 */

    @Test
    public void test_dynamicAopProxy(){
        // 目标对象
        IUserService userService = new UserService();

        // 组装AdvisedSupport, 包装了目标对象、用户自己实现的拦截方法以及方法匹配表达式
        AdvisedSupport advised = new AdvisedSupport();
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
                "execution(* cn.wzz.springframework.test.bean.IUserService.*(..))");
        MethodInterceptor interceptor = new UserServiceInterceptor();

        advised.setMethodMatcher(pointcut);
        advised.setTargetSource(new TargetSource(userService));
        advised.setMethodInterceptor(interceptor);

        // 使用Cglib2AopProxy获取代理对象proxy
        IUserService proxy = (IUserService) new Cglib2AopProxy(advised).getProxy();
        System.out.println("Cglib result: " + proxy.queryUserInfo());

        // 使用jdk动态代理
        IUserService proxy_jdk = (IUserService) new JdkDynamicAopProxy(advised).getProxy();
        System.out.println("JDK result: " + proxy_jdk.register("wsx"));
    }
/* 测试结果:
监控 - Begin By Aop
方法名称: queryUserInfo
方法耗时: 104ms
监控 - End

Cglib result: wzz 0001 杭州
监控 - Begin By Aop
方法名称: register
方法耗时: 88ms
监控 - End

JDK result: 注册用户：wsx success！
 */
}