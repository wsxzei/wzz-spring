package cn.wzz.springframework.beans.factory;

/**
 * Interface to be implemented by objects used within a {@link BeanFactory}
 * which are themselves factories. If a bean implements this interface,
 * it is used as a factory for an object to expose, not directly as a bean
 * instance that will be exposed itself.<br/>
 * Spring 对外提供可以二次从FactoryBean的getObject方法获取对象的功能, 实现这个接口的对象可以扩充自己暴露的对象的功能。<br/>
 * 正如上述英文注释描述, 二次是指实现这个接口的bean作为暴露某个对象的工厂, 在被用户获取时:<br/>
 *  1. 先尝试从Spring的单例对象容器中获取FactoryBean对象；如果没有，则调用createBean方法实例化FactoryBean, 并将它加入容器。<br/>
 *  2. 然后尝试从FactoryBeanObjectCache获取工厂bean暴露的对象；如果没有则会使用FactoryBean对象的getObject方法进行获取，
 *      并将其放入FactoryBeanObject缓存中。<br/>
 *  用户最终通过getBean拿到的并非工厂bean对象，而是它暴露的对象(类型为FactoryBean接口指定的范型)<br/>
 * @param <T>
 * */
public interface FactoryBean<T>{

    T getObject() throws Exception;

    boolean isSingleton();

    Class<?> getObjectType();
}
