package cn.wzz.springframework.aop.framework.adapter;

import cn.wzz.springframework.aop.AfterReturningAdvice;
import cn.wzz.springframework.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/* 包含BeforeAdvice 操作的方法拦截器, beforeAdvice属性需要xml配置文件中指定bean */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    /* 在拦截方法被调用前执行回调 */
    private MethodBeforeAdvice beforeAdvice;

    /* 在拦截方法被调用并返回后, 执行回调 */
    private AfterReturningAdvice afterReturningAdvice;

    public MethodBeforeAdviceInterceptor(){
    }

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice){
        this.beforeAdvice = advice;
    }

    @Override
    // 先调用advice的before方法, 并传入连接点(MethodInvocation)信息
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;

        try{
            // 分别传入方法、方法入参、目标对象给before方法
            if(beforeAdvice != null) {
                this.beforeAdvice.before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
            }
            result = invocation.proceed();
            return result;
        }finally {
            if(afterReturningAdvice != null){
                afterReturningAdvice.afterReturning(result,
                        invocation.getMethod(), invocation.getArguments(), invocation.getThis());
            }
        }

    }
}
