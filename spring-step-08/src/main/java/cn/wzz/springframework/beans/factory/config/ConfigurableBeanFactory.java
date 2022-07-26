package cn.wzz.springframework.beans.factory.config;


import cn.wzz.springframework.beans.factory.BeanPostProcessor;
import cn.wzz.springframework.beans.factory.HierarchicalBeanFactory;

/**
 * Configuration interface to be implemented by most bean factories. Provides
 * facilities to configure a bean factory, in addition to the bean factory
 * client methods in the {@link cn.wzz.springframework.beans.factory.BeanFactory}
 * interface.
 * 可获取 BeanPostProcessor、BeanClassLoader等的一个配置化接口
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /*
     销毁单例对象, AbstractBeanFactory通过继承DefaultSingletonRegistry实现该方法
     一种隔离分层服务的设计方式
     */
    void destroySingletons();

}
