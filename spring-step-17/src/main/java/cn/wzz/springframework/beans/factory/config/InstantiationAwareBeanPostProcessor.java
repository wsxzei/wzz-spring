package cn.wzz.springframework.beans.factory.config;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.BeanPostProcessor;

/**
 * {@link BeanPostProcessor}的子接口, 增加了(1.实例化前)和(2.实例化后显式设置属性前)的回调方法
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在 Bean 对象执行初始化方法之前，执行此方法<br/>
     * 返回的bean对象可能是一个代理对象而不是目标bean, 有效抑制了目标bean的默认实例化.
     *
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;


    /**
     * 在将属性值集合pvs应用于bean对象前, 对pvs进行后处理。<br/>
     * 检查是否满足所有依赖项, 例如@Required注解修饰的set方法, 对应的bean属性必须在xml文件中配置, 否则抛出BeanInitializationException
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;


    /**
     * 暴露bean对象的早期引用
     */
    default Object getEarlyBeanReference(Object bean, String beanName){
        return bean;
    }
}
