package cn.wzz.springframework.context.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.BeanPostProcessor;
import cn.wzz.springframework.context.ApplicationContext;
import cn.wzz.springframework.context.ApplicationContextAware;

/**
 * refresh方法会构造并注册这个bean实例化后处理器 {@link BeanPostProcessor}, 构造方法入参为 {@link ApplicationContext} 对象.<br/>
 * 处理器的功能是在bean对象初始化时, 向实现了 {@link ApplicationContextAware} 接口的bean对象提供它运行时所处的应用上下文.
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext context){
        this.applicationContext = context;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof ApplicationContextAware){
            ((ApplicationContextAware)bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
