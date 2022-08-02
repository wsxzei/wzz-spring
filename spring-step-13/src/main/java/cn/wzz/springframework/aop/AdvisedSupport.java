package cn.wzz.springframework.aop;

import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;

@Data
// 将代理、拦截、匹配的各项属性包装到一个类中，方便proxy实现类中使用
public class AdvisedSupport {

    // 被代理的目标对象(构造方法传入Object对象, 可以获取目标类TargetClass的所有接口的信息)
    private TargetSource targetSource;
    // 方法拦截器(用户实现invoke方法)
    private MethodInterceptor methodInterceptor;
    // 方法匹配器(由AspectJExpressionPointcut提供检查方法与切点表达式是否匹配的功能)
    private MethodMatcher methodMatcher;

    // 是否使用Cglib动态代理
    private boolean proxyTargetClass = false;

    // get/set方法
}
