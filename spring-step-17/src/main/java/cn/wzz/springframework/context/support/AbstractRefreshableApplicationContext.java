package cn.wzz.springframework.context.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * refreshBeanFactory中主要对DefaultListableBeanFactory实例化
 * 以及调用 loadBeanDefinitions: spring.xml资源配置的加载、Bean定义注册
 * loadBeanDefinitions留给后续继承的抽象类实现
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        // 读取指定xml配置文件的bean信息, 注册到Spring容器
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory(){
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
