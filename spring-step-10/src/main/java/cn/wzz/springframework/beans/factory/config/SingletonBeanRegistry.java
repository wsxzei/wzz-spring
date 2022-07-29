package cn.wzz.springframework.beans.factory.config;

// 定义了获取单例对象的接口
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);

    void addSingleton(String beanName, Object bean);
}
