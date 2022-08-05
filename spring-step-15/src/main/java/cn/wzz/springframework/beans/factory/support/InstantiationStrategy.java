package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 定义实例化策略接口
 * 入参信息: beanDefinition、beanName、ctor、args
 * ctor为与入参信息匹配的bean的构造器 Constructor, 拿到入参列表Class[]
 * args为具体的入参信息, 实例化时会使用
 * */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args)
            throws BeansException;
}
