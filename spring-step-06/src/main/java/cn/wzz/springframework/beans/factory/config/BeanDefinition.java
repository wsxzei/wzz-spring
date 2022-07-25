package cn.wzz.springframework.beans.factory.config;

import cn.wzz.springframework.beans.PropertyValues;

//定义Bean实例化信息
public class BeanDefinition {

    /**
     * 存放bean的Class对象, 用于bean的实例化;
     * 将 bean 的实例化操作交给Spring容器, 而不是手动传入实例化对象
     */
    private Class beanClass;

    // 实例化 bean 对象后按照属性集合的值填充 bean 对象
    private PropertyValues propertyValues;


    public BeanDefinition(Class beanClass){
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues){
        this.beanClass = beanClass;
        if(propertyValues != null)
            this.propertyValues = propertyValues;
        else
            this.propertyValues = new PropertyValues();
    }

    public PropertyValues getPropertyValues(){
        return propertyValues;
    }

    public Class getBeanClass(){
        return beanClass;
    }
}
