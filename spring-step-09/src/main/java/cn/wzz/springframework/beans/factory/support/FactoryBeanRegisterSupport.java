package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该抽象类为FactoryBean的注册提供支持, 是新增在AbstractBeanFactory和DefaultSingletonRegistry间的一层。<br/>
 * 定义了FactoryBean暴露对象的缓存用于存放单例类型的对象, 避免重复创建。<br/>
 * doGetObjectFromFactoryBean是具体调用FactoryBean#getObject()的方法;<br/>
 * getObjectFromFactoryBean既有缓存的查询, 又有对象的获取, 因此提供这个方法作为逻辑包装;
 * */
public abstract class FactoryBeanRegisterSupport extends DefaultSingletonRegistry{

    /*
     ConcurrentHashMap的get方法返回null说明不存在key, key-value不支持null
     如果FactoryBean对象的getObject方法返回null, 则会注册NULL_OBJECT到
     */
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getObjectFromFactoryBean(FactoryBean factory, String beanName){
        //工厂暴露的对象为单例类型, 需要保存到缓存中
        if(factory.isSingleton()){
            /*
            先查询缓存, 若为null说明没有执行过工厂bean的getObject方法, 缓存中没有key为beanName的条目;
            若为NULL_OBJECT, 说明调用过getObject方法, 但方法返回的是null值;
             */
            Object bean = factoryBeanObjectCache.get(beanName);
            if(bean == null){
                bean = doGetObjectFromFactoryBean(factory, beanName);
                factoryBeanObjectCache.put(beanName, bean==null ? NULL_OBJECT:bean);
            }
            return bean == NULL_OBJECT ? null : bean;
        }else{
            return doGetObjectFromFactoryBean(factory, beanName);
        }
    }

    private Object doGetObjectFromFactoryBean(FactoryBean factoryBean, String beanName){
        try{
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object["
                    + beanName + "] creation", e);
        }
    }

}
