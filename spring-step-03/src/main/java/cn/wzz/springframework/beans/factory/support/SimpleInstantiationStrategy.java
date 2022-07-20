package cn.wzz.springframework.beans.factory.support;


import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * JDK 实例化, 使用 getDeclaredConstructor*/
public class SimpleInstantiationStrategy implements InstantiationStrategy{
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args)
            throws BeansException {
        Class clazz = beanDefinition.getBeanClass();

        try {
            if (ctor != null) {
                //有参数的构造器ctor, getParameterTypes获取参数列表的类对象, args参数值
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            } else {
                //无参构造
                return clazz.getDeclaredConstructor().newInstance();
            }
        }catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            throw new BeansException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
    }
}
