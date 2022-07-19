package cn.wzz.springframework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Bean对象的工厂,存放BeanDefinition到HashMap容器, 通过容器获取Bean对象
public class BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    // Bean对象的获取
    public Object getBean(String name){
        return beanDefinitionMap.get(name).getBean();
    }

    //注册Bean, 注册的是Bean的定义信息
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition){
        beanDefinitionMap.put(name, beanDefinition);
    }
}
