package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.DisposableBean;
import cn.wzz.springframework.beans.factory.ObjectFactory;
import cn.wzz.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// AbstractBeanFactory将继承这个类
public class DefaultSingletonRegistry implements SingletonBeanRegistry {

    /**
     * Internal marker for a null singleton object:
     * used as marker value for concurrent Maps (which don't support null values).
     */
    protected static final Object NULL_OBJECT = new Object();

    //一级缓存, 成品对象
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 二级缓存, 没有完全实例化的对象(半成品)
    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    // 三级缓存, 存放代理对象工厂
    private Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>();

    // 注册销毁
    private Map<String, DisposableBean> disposableBeanMap = new HashMap<>();

    @Override
    /**
     * 获取单例对象:
     * 1、先从一级缓存中获取成品对象, 如果不存在则获取二级缓存中的半成品对象;
     * 2、如果都没有, 通过三级缓存中的工厂对象(ObjectFactory#getObject)获取代理对象
     * 3、将生成的代理对象放置到二级缓存中, 同时删除三级缓存中的工厂对象
     * 调用时刻:
     * getObject方法会先利用getSingleton获取缓存的单例对象(成品对象或半成品对象);
     * createBean方法最后会对单例bean执行getSingleton获取(工厂暴露的对象)或(普通bean对象), 然后注册到一级缓存中
     */
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);

        if(singletonObject == null){
            // 一级缓存中不存在beanName的成品对象, 尝试从二级缓存中获取
            singletonObject = earlySingletonObjects.get(beanName);
            if(singletonObject == null){
                // 二级缓存中不存在半成品对象, 尝试利用三级缓存中的工厂生成bean对象
                ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
                if(objectFactory != null){
                    singletonObject = objectFactory.getObject();
                    // 放入二级缓存中, 删除三级缓存中的工厂对象
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    // 注册单例对象到一级缓存中, 在doCreateBean完成bean对象创建后调用
    public void addSingleton(String beanName, Object singletonObject){
        singletonObjects.put(beanName, singletonObject);
        // 移除二级缓存中的bean半成品对象
        earlySingletonObjects.remove(beanName);
        // 移除三级缓存中的工厂对象
        singletonFactories.remove(beanName);
    }

    // 暴露实例化后的bean对象到三级缓存(doCreateBean中调用)
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory){
        if(!singletonObjects.containsKey(beanName)){
            // 如果一级缓存中不存在名为beanName的对象, 则将单例工厂注册到三级缓存中(清除二级缓存)
            singletonFactories.put(beanName, singletonFactory);
            earlySingletonObjects.remove(beanName);
        }
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
