package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.BeanFactory;
import cn.wzz.springframework.beans.factory.BeanPostProcessor;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * 继承DefaultSingletonRegistry后, 具备了单例对象的注册功能
 * 实现BeanFactory接口的getBean方法, 收口了bean对象获取的调用逻辑
 * */
public abstract class AbstractBeanFactory extends DefaultSingletonRegistry implements ConfigurableBeanFactory {

    //BeanPostProcessors对象的List集合
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public Object getBean(String name){
        return doGetBean(name, null);
    }

    // TODO 根据名称和类型获取

    public Object getBean(String name, Object... args){
        return doGetBean(name, args);
    }



    /**
     * doGetBean方法为两个getBean方法的具体实现, 与02版本的getBean功能相同, 多了构造函数的入参数组args
     * 从singletonObjects缓存中获取单例对象, 获取不到则拿到BeanDefinition对象
     * 通过createBean方法进行实例化操作, 并放入单例对象缓存中
     */
    private Object doGetBean(String name, Object... args){
        Object bean = getSingleton(name);//继承的方法获取单例对象

        if(bean != null){
            return bean;
        }

        //单例对象缓存中没有, 需要进行bean的实例化
        BeanDefinition beanDefinition = getBeanDefinition(name);

        return createBean(name, beanDefinition, args);
    }

    @Override
    // ConfigurableBeanFactory定义这个方法, 用于将BeanPostProcessor加入到beanPostProcessor中
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 保证List中只有一个beanPostProcessor对象(如果没有则不影响)
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors(){
        return beanPostProcessors;
    }

    // 继承该抽象类的类实现如下抽象方法的具体逻辑
    protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

    // 相比02版本, 增加了构造函数的参数数组args
    protected  abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) throws BeansException;
}
