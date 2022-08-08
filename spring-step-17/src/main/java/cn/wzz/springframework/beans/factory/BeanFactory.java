package cn.wzz.springframework.beans.factory;


import cn.wzz.springframework.beans.BeansException;

/** 采用模板模式的设计方法
 * Bean工厂接口：抽象类AbstractBeanFactory 将实现这个接口
 * 收口 Bean 获取的核心方法 getBean, 后续类的继承者只需关注具体方法的逻辑实现
 * spring-step-03 中考虑构造函数的入参,实现有参数的bean对象的实例化
 */
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws  BeansException;

    <T> T getBean(Class<T> requiredType) throws BeansException;

    boolean containsBean(String beanName);
}
