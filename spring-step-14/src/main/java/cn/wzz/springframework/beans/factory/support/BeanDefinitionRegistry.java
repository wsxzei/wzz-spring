package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.factory.config.BeanDefinition;

//定义注册BeanDefinition的方法, DefaultListableBeanFactory 实现该接口
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

}
