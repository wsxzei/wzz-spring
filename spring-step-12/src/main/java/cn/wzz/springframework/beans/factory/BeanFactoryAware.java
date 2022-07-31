package cn.wzz.springframework.beans.factory;

import cn.wzz.springframework.beans.BeansException;

/**
 * Interface to be implemented by beans that wish to be aware of their owning {@link BeanFactory}.
 */
public interface BeanFactoryAware extends Aware{

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
