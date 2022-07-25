package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.DisposableBean;
import cn.wzz.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

// AbstractBeanFactory将继承这个类
public class DefaultSingletonRegistry implements SingletonBeanRegistry {

    //注册的单例对象放入HashMap
    private Map<String, Object> singletonObjects = new HashMap<>();

    // 注册销毁
    private Map<String, DisposableBean> disposableBeanMap = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    //继承此类的类可以调用此方法对单例对象注册
    protected void addSingleton(String beanName, Object singletonObject){
        singletonObjects.put(beanName, singletonObject);
    }

    // 执行所有注册的单例销毁方法
    public void destroySingletons(){
        String[] beanNames = disposableBeanMap.keySet().toArray(new String[0]);
        for(String beanName: beanNames){
            DisposableBean bean = disposableBeanMap.remove(beanName);
            try {
                bean.destroy();
            } catch (Exception exception) {
                throw new BeansException("Destroy method on bean with name ["
                        + beanName + "] threw an exception.", exception);
            }
        }
    }

    protected void registerDisposalBean(String beanName, DisposableBean disposableBean){
        disposableBeanMap.put(beanName, disposableBean);
    }
}
