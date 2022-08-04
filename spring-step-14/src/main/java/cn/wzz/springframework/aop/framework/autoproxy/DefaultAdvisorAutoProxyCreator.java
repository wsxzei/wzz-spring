package cn.wzz.springframework.aop.framework.autoproxy;

import cn.wzz.springframework.aop.*;
import cn.wzz.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.wzz.springframework.aop.framework.ProxyFactory;
import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.BeanFactory;
import cn.wzz.springframework.beans.factory.BeanFactoryAware;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

/**
 * 自动代理创建者, 实现了实例化感知BeanPostProcessor接口
 * 在{@link cn.wzz.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation(String, BeanDefinition)}
 * 方法中会使用这个类的postProcessBeforeInstantiation方法在bean实例化前构造代理对象
 * */
public class DefaultAdvisorAutoProxyCreator implements BeanFactoryAware, InstantiationAwareBeanPostProcessor {

    private DefaultListableBeanFactory factory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if(isInfrastructureClass(beanClass)) return null;

        // 获取所有的切面信息以及需要执行的方法拦截器
        Collection<AspectJExpressionPointcutAdvisor> advisors =
                factory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for(AspectJExpressionPointcutAdvisor advisor: advisors){
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            // 如果beanClass与当前的切面类过滤器匹配失败, 则检查下一个切面
            if(!classFilter.matches(beanClass)) continue;
            // 找到切面后, 设置AdvisedSupport传入代理工厂生成代理类
            TargetSource targetSource = null;
            try{
                // 生成目标源(如果代理对象需要通过有参构造获取, 则当前方法需要传入构造函数的入参)
                targetSource = new TargetSource(beanClass.getConstructor().newInstance()) ;
            }catch (Exception e){
                e.printStackTrace();
            }

            AdvisedSupport advisedSupport = new AdvisedSupport();
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);// 默认jdk动态代理

            return new ProxyFactory(advisedSupport).getProxy();
        }

        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
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
}
