package cn.wzz.springframework.beans.factory.support;


import cn.hutool.core.bean.BeanUtil;
import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.PropertyValue;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;

/** spring自动装配, 实例化bean对象：
 * 1. 实现AbstractBeanFactory中定义的createBean方法:
 *      1.1 这个版本使用cglib动态代理创建含有有参构造函数的bean对象(jdk动态代理也可以)
 *      1.2 通过getObject方法接收bean的构造函数参数作为入参
 * 2. 调用DefaultSingletonRegistry的addSingleton方法, 将单例对象加入缓存
 * */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    //实例化策略类
    private InstantiationStrategy instantiationStrategy =
                new CglibSubclassingInstantiationStrategy();


    // 实现有参构造函数的bean的实例化
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args)
        throws BeansException{

        Object bean = null;

        //创建bean对象
        try{
            bean = createInstance(beanName, beanDefinition, args);
            applyPropertyValues(bean, beanDefinition, beanName);
        }catch (Exception e){
            // 捕获instantiate可能出现的运行时异常
            throw new BeansException("Instantiation of bean failed", e);
        }

        //加入到单例对象缓存
        addSingleton(beanName, bean);

        return bean;
    }

    // 抽取创建bean实例的执行逻辑, 作为单独的createInstance方法
    private Object createInstance(String beanName, BeanDefinition beanDefinition, Object... args){
        Constructor constructorToUse = null;
        // 注意: 无参构造的args为null, 无法获得length, 因此要进行判断
        if(args != null) {
            Constructor[] constructors = beanDefinition.getBeanClass().getDeclaredConstructors();

            for (Constructor ctor : constructors) {
                if (ctor.getParameterTypes().length == args.length) {
                    constructorToUse = ctor;
                    break;
                }
            }
        }
        return instantiationStrategy.instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    // 将beanDefinition中的propertyValueList属性集合注入新创建的bean对象
    private void applyPropertyValues(Object bean, BeanDefinition beanDefinition, String beanName){
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue pv : propertyValues.getPropertyValues()) {
                String propertyName = pv.getName();
                Object propertyValue = pv.getValue();

                if (propertyValue instanceof BeanReference) {
                    // bean 的引用属性依赖于另一个bean的实例化
                    BeanReference beanReference = (BeanReference) propertyValue;
                    // 递归创建bean的引用属性
                    propertyValue = getBean(beanReference.getBeanName());
                }
                // 设置属性(hutool包提供)
                BeanUtil.setFieldValue(bean, propertyName, propertyValue);
            }
        }catch (Exception e){
            throw new BeansException("Error setting property value: " + beanName);
        }
    }
}
