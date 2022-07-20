package cn.wzz.springframework.beans.factory.config;

//定义Bean实例化信息
public class BeanDefinition {

    /**
     * 存放bean的Class对象, 用于bean的实例化;
     * 将 bean 的实例化操作交给Spring容器, 而不是手动传入实例化对象
     */
    private Class beanClass;

    public BeanDefinition(Class beanClass){
        this.beanClass = beanClass;
    }

    public Class getBeanClass(){
        return beanClass;
    }
}
