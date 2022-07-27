package cn.wzz.springframework.context.support;

import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.wzz.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 实现AbstractRefreshableApplicationContext定义的loadBeanDefinitions方法
 * 该方法构造Xml文件的资源读取器, 完成xml配置文件的读取、注册Bean定义
 * 留有 getConfigLocations, 从入口应用上下文中获取配置信息的地址描述
 */

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{

    // 获取配置文件的路径数组
    protected abstract String[] getConfigLocations();

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // AbstractApplicationContext继承了DefaultResourceLoader, 因此当前对象具有资源加载器功能
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if(configLocations != null){
            reader.loadBeanDefinitions(configLocations);
        }
    }
}
