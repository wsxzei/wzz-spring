package cn.wzz.springframework.beans.factory;

import cn.wzz.springframework.beans.BeansException;

public interface BeanPostProcessor {
    /* 在bean对象实例化之后, 执行初始化方法之前, 执行此方法 */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /* 在bean对象实例化后, 并执行了初始化方法, 执行此方法 */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
