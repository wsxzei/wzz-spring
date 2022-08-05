package cn.wzz.springframework.beans.factory.support;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.*;
import cn.wzz.springframework.beans.PropertyValue;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.config.AutowireCapableBeanFactory;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.BeanReference;
import cn.wzz.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/** spring自动装配, 实例化bean对象：
 * 1. 实现AbstractBeanFactory中定义的createBean方法:
 *      1.1 03版本增加了使用cglib动态代理创建含有有参构造函数的bean对象的功能(jdk动态代理也可以)
 *      1.2 通过getObject方法接收bean的构造函数参数作为入参
 * 2. 调用DefaultSingletonRegistry的addSingleton方法, 将单例对象加入缓存
 *
 * 04版本实现了 bean定义中的属性集合PropertyValues的注入到bean对象的功能
 * 06版本增加bean对象的初始化方法 initializeBean: Spring的扩展机制提供了bean实例化后对bean对象的修改
 * */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
    implements AutowireCapableBeanFactory {

    //实例化策略类
    private InstantiationStrategy instantiationStrategy =
                new CglibSubclassingInstantiationStrategy();


    // 实现有参构造函数的bean的实例化
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args)
        throws BeansException{

        Object bean = null;

        try{
            // 执行bean实例化前的BeanPostProcessor, 返回的是代理对象
            bean = resolveBeforeInstantiation(beanName, beanDefinition);
            if(bean != null){
                // 注意: 这里代理对象直接返回了, 并没有设置属性, 后续加上这个功能!
                return bean;
            }
            //创建bean对象
            bean = createInstance(beanName, beanDefinition, args);
            // 设置Bean属性前, 允许BeanPostProcessor修改属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
            // 给 Bean 实例填充属性
            applyPropertyValues(bean, beanDefinition, beanName);
            // 执行Bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            bean = initializeBean(beanName,bean, beanDefinition);
        }catch (Exception e){
            // 捕获instantiate可能出现的运行时异常
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册bean对象的销毁方法
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        //如果是bean对象是单例模式, 则加入到单例对象缓存
        if(beanDefinition.isSingleton()) {
            addSingleton(beanName, bean);
        }

        return bean;
    }

    // 用于生成AOP动态代理对象, 因此需要在原本的bean实例化前调用该方法
    private Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition){
        // 实例化前生成代理对象的操作
        Object result = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if(result != null){
            // 对代理对象进行实例化后置操作
            result = applyBeanPostProcessorsAfterInitialization(result, beanName);
        }
        return result;
    }

    private Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName){
        Object result = null;
        for(BeanPostProcessor beanPostProcessor: getBeanPostProcessors()){
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor)
                        .postProcessBeforeInstantiation(beanClass, beanName);
                if(result != null) return result;
            }
        }
        return null;
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

    /**
     * 在设置bean属性前, 允许BeanPostProcessor修改属性值
     */
    private void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition){
        for(BeanPostProcessor beanPostProcessor: getBeanPostProcessors()){
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                InstantiationAwareBeanPostProcessor processor = (InstantiationAwareBeanPostProcessor) beanPostProcessor;
                PropertyValues pvs = processor.postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if(pvs != null)
                    beanDefinition.setPropertyValues(pvs);
            }
        }
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

    // 执行bean实例化后的修改操作
    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition){

        /*
         检查bean是否为Aware感知对象, 如果是则通过set方法通知bean对象
         应用上下文感知的实现由ApplicationContextAwareProcessor完成, 该处理器构造并注册于refresh方法
         */
        if(bean instanceof Aware){
            if(bean instanceof BeanNameAware)
                ((BeanNameAware)bean).setBeanName(beanName);
            if(bean instanceof BeanFactoryAware)
                ((BeanFactoryAware)bean).setBeanFactory(this);
            if(bean instanceof BeanClassLoaderAware)
                ((BeanClassLoaderAware)bean).setBeanClassLoader(ClassUtil.getClassLoader());
        }

        // 1. 执行 BeanPostProcessor Before处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        //2. 调用bean定义中的初始化方法, 或者调用实现了InitializingBean的afterPropertiesSet方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception exception) {
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed.", exception);
        }

        // 3. 执行 BeanPostProcessor After处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    //遍历beanPostProcessors, 调用每个beanPostProcessor对象定义的修改bean对象的操作
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName){
        Object result = existingBean;
        for(BeanPostProcessor processor: getBeanPostProcessors()){
            Object curr = processor.postProcessBeforeInitialization(result, beanName);
            if(curr == null) return result;
            result = curr;
        }
        return result;
    }

    /**
     * 调用初始化方法执行初始化操作, 方式有两种:
     * 1. bean对象实现类InitializingBean接口, 可以直接调用afterPropertiesSet
     * 2. 在xml配置文件中, 设置了init-method属性, 使用反射的方式调用bean对象的该方法
     * */
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 实现了Initializing接口
        if(bean instanceof InitializingBean){
            ((InitializingBean)bean).afterPropertiesSet();
        }

        String initMethodName = beanDefinition.getInitMethodName();
        // bean定义设置了initMethodName属性, 使用反射调用初始化方法
        if(StrUtil.isNotEmpty(initMethodName)){
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if(initMethod == null){
                throw new BeansException("Can't find a init method named [" + initMethodName +"].");
            }
            initMethod.invoke(bean);
        }
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for(BeanPostProcessor processor: getBeanPostProcessors()){
            Object curr = processor.postProcessAfterInitialization(result, beanName);
            if(curr == null) return result;
            result = curr;
        }
        return result;
    }

    /**
     * bean对象注册DisposalBeanAdapter, 由spring统一调用适配器的destroy方法进行销毁, 注册条件:
     * 1. bean 实现了DisposalBean接口
     * 2. 配置文件中声明了destroy-method属性
     * */
    private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition){
        // 如果bean对象不是单例模式, 不需要执行销毁方法
        if(!beanDefinition.isSingleton()) return;

        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if(bean instanceof DisposableBean || StrUtil.isNotEmpty(destroyMethodName)){
            DisposableBeanAdapter adapter = new DisposableBeanAdapter(bean, beanName, beanDefinition);
            registerDisposalBean(beanName, adapter);
        }
    }
}
