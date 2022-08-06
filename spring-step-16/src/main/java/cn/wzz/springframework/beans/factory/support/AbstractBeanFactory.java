package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.BeanPostProcessor;
import cn.wzz.springframework.beans.factory.FactoryBean;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.wzz.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.List;


/**
 * 继承DefaultSingletonRegistry后, 具备了单例对象的注册功能
 * 实现BeanFactory接口的getBean方法, 收口了bean对象获取的调用逻辑
 * */
public abstract class AbstractBeanFactory extends FactoryBeanRegisterSupport implements ConfigurableBeanFactory {

    //BeanPostProcessors对象的List集合
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    // 应用于诸如注解属性值解析的字符串解析器集合
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    public Object getBean(String name){
        return doGetBean(name, null);
    }

    public Object getBean(String name, Object... args){
        return doGetBean(name, args);
    }

    public <T> T getBean(String name, Class<T> requiredType){
        Object bean = getBean(name); // 名称为name的bean定义不存在会抛出BeansException
        if(!(requiredType.isAssignableFrom(bean.getClass())))
            throw new BeansException("The bean [" + name  + "] does not match the requirement type ["
                    + requiredType + "]");
        return (T)bean;
    }

    /**
     * doGetBean方法为两个getBean方法的具体实现, 与02版本的getBean功能相同, 多了构造函数的入参数组args
     * 从singletonObjects缓存中获取单例对象, 获取不到则拿到BeanDefinition对象
     * 通过createBean方法进行实例化操作, 并放入单例对象缓存中
     * 09 版本增加了FactoryBean机制, 因此使用getObjectForBeanInstance包装方法统一返回需要获取的对象
     */
    private Object doGetBean(String name, Object... args){
        Object sharedInstance = getSingleton(name);//继承获取单例对象的方法

        if(sharedInstance != null){
            return getObjectForBeanInstance(sharedInstance, name);
        }

        //单例对象缓存中没有, 需要进行bean的实例化
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if(beanDefinition == null)
            throw new BeansException("The bean named [" + name +"] doesn't exist" );

        Object bean = createBean(name, beanDefinition, args);

        return getObjectForBeanInstance(bean, name);
    }

    // 若共享实例为FactoryBean对象, 应该调用超类的getObjectFromFactoryBean, 而不是返回工厂bean自身
    private Object getObjectForBeanInstance(Object sharedInstance, String beanName){
        if(!(sharedInstance instanceof FactoryBean)){
            return sharedInstance;
        }

        FactoryBean factoryBean = (FactoryBean)sharedInstance;
        Object object = getObjectFromFactoryBean(factoryBean, beanName);

        return object;
    }

    @Override
    // ConfigurableBeanFactory定义这个方法, 用于将BeanPostProcessor加入到beanPostProcessor中
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 保证List中只有一个beanPostProcessor对象(如果没有则不影响)
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    protected List<BeanPostProcessor> getBeanPostProcessors(){
        return beanPostProcessors;
    }

    // 继承该抽象类的类实现如下抽象方法的具体逻辑
    protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

    // 相比02版本, 增加了构造函数的参数数组args
    protected  abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) throws BeansException;

    // 添加字符串解析器, 用于注解属性中占位符的替换
    public void addEmbeddedValueResolver(StringValueResolver resolver){
        embeddedValueResolvers.add(resolver);
    }

    // 使用解析器集合中的解析器对strVal修改
    public String resolveEmbeddedValue(String strVal){
        for(StringValueResolver resolver: embeddedValueResolvers){
            strVal = resolver.resolveStringValue(strVal);
        }
        return strVal;
    }
}
