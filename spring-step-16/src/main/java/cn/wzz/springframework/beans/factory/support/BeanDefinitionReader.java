package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.core.io.Resource;
import cn.wzz.springframework.core.io.ResourceLoader;


/**
 * 从Resource对象中解析出BeanDefinition信息
 * 构造BeanDefinition然后注册到Spring容器中
 * */
public interface BeanDefinitionReader {
    // 获取资源加载器
    ResourceLoader getResourceLoader();

    // 获取BeanDefinition注册点, 用于将构建好的bean定义托管给spring容器进行管理
    BeanDefinitionRegistry getRegistry();


    /* 上述两个方法是提供给后面三个方法的工具, 用于加载和注册, 会现在抽象类中实现 */

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(Resource... resources) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String... locations) throws BeansException;
}
