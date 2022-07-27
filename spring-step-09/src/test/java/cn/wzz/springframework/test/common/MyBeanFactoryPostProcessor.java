package cn.wzz.springframework.test.common;

import cn.wzz.springframework.beans.PropertyValue;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");

        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("company", "阿里巴巴"));
    }
}
