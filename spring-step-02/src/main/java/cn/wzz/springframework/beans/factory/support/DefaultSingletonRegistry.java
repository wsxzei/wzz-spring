package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

// AbstractBeanFactory将继承这个类
public class DefaultSingletonRegistry implements SingletonBeanRegistry {

    //注册的单例对象放入HashMap
    private Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    //继承此类的类可以调用此方法对单例对象注册
    protected void addSingleton(String beanName, Object singletonObject){
        singletonObjects.put(beanName, singletonObject);
    }
}
