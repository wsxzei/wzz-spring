package cn.wzz.springframework.beans.factory.config;

/**
 * 对于引用bean类型的实例属性, PropertyValue的bean属性保存的是BeanReference对象
 * BeanReference 对象只保存beanName, 在实例化时通过 getBean 递归创建并填充引用属性
 * */
public class BeanReference {

    private final String beanName;

    public BeanReference(String beanName){
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
