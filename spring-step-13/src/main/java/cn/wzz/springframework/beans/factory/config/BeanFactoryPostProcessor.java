package cn.wzz.springframework.beans.factory.config;

import cn.wzz.springframework.beans.factory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {

    /* 在所有的BeanDefinition加载完成后, 实例化Bean对象之前, 提供修改BeanDefinition属性的机制 */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
