package cn.wzz.springframework;

//定义Bean实例化信息
public class BeanDefinition {

    //存放Bean对象
    private Object bean;

    public BeanDefinition(Object bean){
        this.bean = bean;
    }

    public Object getBean(){
        return bean;
    }
}
