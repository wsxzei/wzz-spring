package cn.wzz.springframework.aop.framework.autoproxy;

import cn.wzz.springframework.aop.*;
import cn.wzz.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.wzz.springframework.aop.framework.ProxyFactory;
import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.BeanFactory;
import cn.wzz.springframework.beans.factory.BeanFactoryAware;
import cn.wzz.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

/**
 * 自动代理创建者, 实现了实例化感知BeanPostProcessor接口
 * */
public class DefaultAdvisorAutoProxyCreator implements BeanFactoryAware, InstantiationAwareBeanPostProcessor {

    private DefaultListableBeanFactory factory;

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }



    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = (DefaultListableBeanFactory) beanFactory;
    }

    // 如果beanClass为基础组件类, 不需要执行实例化前操作(即切面bean，拦截器bean不需要生成代理对象)
    private boolean isInfrastructureClass(Class<?> beanClass){
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass)
                || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    // 生成AOP动态代理对象
    public Object getEarlyBeanReference(Object bean, String beanName){
        return wrapIfNecessary(bean, beanName);
    }

    // 包装bean对象为AOP动态代理对象
    private Object wrapIfNecessary(Object bean, String beanName){
        if(isInfrastructureClass(bean.getClass())) return bean;

        Collection<AspectJExpressionPointcutAdvisor> advisors = factory.getBeansOfType(
                AspectJExpressionPointcutAdvisor.class).values();
        for(AspectJExpressionPointcutAdvisor advisor: advisors){
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 过滤切面类
            if(!classFilter.matches(bean.getClass())) continue;

            // 构造创建AOP动态代理所需的信息
            AdvisedSupport advisedSupport = new AdvisedSupport();
            // 将已经注入了属性的bean对象作为目标对象
            advisedSupport.setTargetSource(new TargetSource(bean));
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setProxyTargetClass(true);

            // 返回代理对象
            return new ProxyFactory(advisedSupport).getProxy();
        }
        return bean;
    }
}
