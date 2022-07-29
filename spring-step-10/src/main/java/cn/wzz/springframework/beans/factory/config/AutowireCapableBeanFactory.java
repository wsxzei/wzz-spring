package cn.wzz.springframework.beans.factory.config;


import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.BeanFactory;

/**
 * Extension of the {@link BeanFactory}
 * interface to be implemented by bean factories that are capable of
 * autowiring, provided that they want to expose this functionality for
 * existing bean instances.
 * 接口的扩展将被能够自动装配的bean工厂实现, 前提是他们想向存在的bean实例暴露这个功能;
 *  一个自动化处理bean工厂配置的接口
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行 BeanPostProcessor 接口实现类的 postProcessBeforeInitialization 方法
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    /**
     * 执行 BeanPostProcessor 接口实现类的 postProcessorsAfterInitialization 方法
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
