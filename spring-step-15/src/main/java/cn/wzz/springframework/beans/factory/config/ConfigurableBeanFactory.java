package cn.wzz.springframework.beans.factory.config;


import cn.wzz.springframework.beans.factory.BeanPostProcessor;
import cn.wzz.springframework.beans.factory.HierarchicalBeanFactory;
import cn.wzz.springframework.util.StringValueResolver;

/**
 * Configuration interface to be implemented by most bean factories. Provides
 * facilities to configure a bean factory, in addition to the bean factory
 * client methods in the {@link cn.wzz.springframework.beans.factory.BeanFactory}
 * interface.
 * 可获取 BeanPostProcessor、BeanClassLoader等的一个配置化接口
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /*
     销毁单例对象, AbstractBeanFactory通过继承DefaultSingletonRegistry实现该方法
     一种隔离分层服务的设计方式
     */
    void destroySingletons();

    /**
     * 添加字符串解析器, 用于对包含内嵌值的注解属性进行解析
     * Add a String resolver for embedded values such as annotation attributes.
     * @param valueResolver the String resolver to apply to embedded values
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     *  Resolve the given embedded value, e.g. an annotation attribute.
     * @param value
     * @return
     */
    String resolveEmbeddedValue(String value);
}
