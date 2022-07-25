package cn.wzz.springframework.context.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.BeanPostProcessor;
import cn.wzz.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.wzz.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.wzz.springframework.context.ConfigurableApplicationContext;
import cn.wzz.springframework.core.io.DefaultResourceLoader;

import java.util.Map;


public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    @Override
    public void refresh() throws BeansException {
        // 1.创建BeanFactory, 并且读取指定位置的配置文件, 完成bean定义的注册
        refreshBeanFactory();

        // 2.获取bean工厂
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3.Bean实例化之前执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // 4.Bean实例化前注册BeanPostProcessor
        registerBeanPostProcessors(beanFactory);

        // 5.单例bean的预实例化(DefaultListableBeanFactory中实现该方法, 就是调用getBean)
        beanFactory.preInstantiateSingletons();
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
