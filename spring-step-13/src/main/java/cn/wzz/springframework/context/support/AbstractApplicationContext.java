package cn.wzz.springframework.context.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.BeanPostProcessor;
import cn.wzz.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.wzz.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.wzz.springframework.context.ApplicationEvent;
import cn.wzz.springframework.context.ApplicationListener;
import cn.wzz.springframework.context.ConfigurableApplicationContext;
import cn.wzz.springframework.context.event.ApplicationEventMulticaster;
import cn.wzz.springframework.context.event.ContextClosedEvent;
import cn.wzz.springframework.context.event.ContextRefreshedEvent;
import cn.wzz.springframework.context.event.SimpleApplicationEventMulticaster;
import cn.wzz.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;


public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    private static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    // 事件广播器, 用于管理事件监听者, 可以返回某一特定事件的监听者集合
    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() throws BeansException {
        // 1.创建BeanFactory, 并且读取指定位置的配置文件, 完成bean定义的注册
        refreshBeanFactory();

        // 2.获取bean工厂
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3. 注册应用上下文感知处理器到 BeanPostProcessor 容器中, 传入this指针(bean对象运行时的应用上下文)
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 4.Bean实例化之前执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5.Bean实例化前注册BeanPostProcessor
        registerBeanPostProcessors(beanFactory);

        // 6.初始化事件广播器: 包括实例化事件广播器; 注册广播器对象到单例对象容器中
        initApplicationEventMulticaster(beanFactory);

        // 7.注册事件监听器到事件广播器的容器中(会实例化ApplicationListener类型的bean对象)
        registerListeners();

        // 8.单例bean的预实例化(DefaultListableBeanFactory中实现该方法, 就是调用getBean)
        beanFactory.preInstantiateSingletons();

        // 9.发布容器刷新完成事件
        finishRefresh();
    }

    private void initApplicationEventMulticaster(ConfigurableListableBeanFactory beanFactory){
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.addSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerListeners(){
        Collection<ApplicationListener> listeners = getBeansOfType(ApplicationListener.class).values();
        for(ApplicationListener listener: listeners){
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    // 发布容器刷新完成事件
    private void finishRefresh(){
        publishEvent(new ContextRefreshedEvent(this));
    }

    /*
    发布事件, SimpleApplicationEventMulticaster会调用getApplicationListeners获取监听该事件的集合
    然后调用他们的onApplicationEvent方法
     */
    public void publishEvent(ApplicationEvent event){
        applicationEventMulticaster.multicastEvent(event);
    }


    // 注册虚拟机关闭钩子, 回调close方法
    public void registerShutDownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                System.out.println("JVM closing...");
                close();
            }
        });
    }

    // 虚拟机关闭前的回调方法, 用于执行单例对象的销毁方法
    public void close(){
        // 发布容器关闭事件, source对象为当前应用上下文
        publishEvent(new ContextClosedEvent(this));

        // 执行单例bean的销毁方法
        getBeanFactory().destroySingletons();
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    // 调用BeanFactoryPostProcessors, bean注册后实例化前对bean定义进行修改
    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory){
        // 实例化所有类型为BeanFactoryPostProcessor的bean对象
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap =
                beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);

        beanFactoryPostProcessorMap.forEach((beanName, processor)->{
            processor.postProcessBeanFactory(beanFactory);
        });
    }

    // 对所有BeanPostProcessor进行注册操作, 后续对单例的预实例化会使用这些对象的方法
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory){
        // 实例化所有BeanPostProcessor对象
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory
                .getBeansOfType(BeanPostProcessor.class);
        // 注册到BeanPostProcessors中, AbstractBeanFactory中实现类add方法
        beanPostProcessorMap.forEach((beanName, processor)->{
            beanFactory.addBeanPostProcessor(processor);
        });
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }
}
