package cn.wzz.springframework.context.support;

import cn.wzz.springframework.beans.BeansException;

/**
 * 对外提供给用户的应用上下文类, 提供了配置文件地址信息
 * 获取bean对象的方式:
 * 1. 传入xml配置文件路径, 实例化ClassPathXmlApplicationContext
 * 2. 获取ConfigurableListBeanFactory对象
 * 3. 使用bean工厂的getBean方法
 * */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{

    // 配置信息地址
    private String[] configLocations;

    public ClassPathXmlApplicationContext(){}

    public ClassPathXmlApplicationContext(String configLocation){
        this(new String[]{configLocation});
    }

    public ClassPathXmlApplicationContext(String[] configLocations){
        this.configLocations = configLocations;
        /**
         * 刷新bean容器:
         * 自动实例化bean工厂、读取配置文件并注册Bean定义
         * 执行BeanFactoryPostProcessor对bean定义的修改操作
         * BeanPostProcessor对象注册到BeanPostProcessors
         * 实例化bean对象(期间执行BeanPostProcessor的扩展操作)
         */
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }
}
