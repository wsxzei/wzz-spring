package cn.wzz.springframework.beans.factory.config;

import cn.wzz.springframework.beans.PropertyValues;

//定义Bean实例化信息
public class BeanDefinition {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    /**
     * 存放bean的Class对象, 用于bean的实例化;
     * 将 bean 的实例化操作交给Spring容器, 而不是手动传入实例化对象
     */
    private Class beanClass;

    // 实例化 bean 对象后按照属性集合的值填充 bean 对象
    private PropertyValues propertyValues;

    // 配置文件init-method属性设置
    private String initMethodName;

    // 配置文件destroy-method属性配置
    private String destroyMethodName;

    private String scope = SCOPE_SINGLETON;

    // 单例对象
    private boolean singleton = true;

    // 原型对象
    private boolean prototype = false;

    public BeanDefinition(Class beanClass){
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues){
        this.beanClass = beanClass;
        if(propertyValues != null)
            this.propertyValues = propertyValues;
        else
            this.propertyValues = new PropertyValues();
    }


    public PropertyValues getPropertyValues(){
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues pvs){
        this.propertyValues = pvs;
    }

    public Class getBeanClass(){
        return beanClass;
    }

    public void setInitMethodName(String initMethodName){
        this.initMethodName = initMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName){
        this.destroyMethodName = destroyMethodName;
    }

    public String getDestroyMethodName(){
        return destroyMethodName;
    }

    public String getInitMethodName(){
        return initMethodName;
    }

    public void setScope(String scope){
        this.scope = scope;
        singleton = scope.equals(SCOPE_SINGLETON);
        prototype = scope.equals(SCOPE_PROTOTYPE);
    }

    public boolean isSingleton() {
        return singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }
}
