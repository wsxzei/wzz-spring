package cn.wzz.springframework.beans.factory;


import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.config.AutowireCapableBeanFactory;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Configuration interface to be implemented by most listable bean factories.
 * In addition to ConfigurableBeanFactory, it provides facilities to
 * analyze and modify bean definitions, and to pre-instantiate singletons.
 * 提供分析和修改Bean以及预先实例化的操作接口
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory,
            AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
