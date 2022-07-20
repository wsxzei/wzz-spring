package cn.wzz.springframework.beans.factory.support;


import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;

/** spring自动装配, 实例化bean对象：
 * 实现AbstractBeanFactory中定义的createBean方法
 * 调用DefaultSingletonRegistry的addSingleton方法, 将单例对象加入缓存
 * */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    protected Object createBean(String beanName, BeanDefinition beanDefinition)
        throws BeansException{
        Object bean = null;

        try {
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // 抛出BeansException, 实例化bean错误; 定义为了运行时异常, 不强制进行捕获
            throw new BeansException("Instantiation of bean failed" , e);
        }

        //加入到单例对象缓存
        addSingleton(beanName, bean);

        return bean;

    }
}
