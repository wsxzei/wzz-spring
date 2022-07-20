package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * BeanDefinitionRegistry 接口定义了bean定义的注册方法
 * AbstractFactory 定义了bean定义对象的获取方法getBeanDefinition
 * */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry{

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();


    @Override   //从bean定义Map中获取BeanDefinition对象
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        // bean未定义异常, 不存在指定beanName的BeanDefinition对象
        if(beanDefinition == null)
            throw new BeansException("No bean named '" + beanName + "' is defined");
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }
}
