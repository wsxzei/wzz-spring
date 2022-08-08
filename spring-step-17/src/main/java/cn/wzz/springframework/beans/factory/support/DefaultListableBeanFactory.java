package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BeanDefinitionRegistry 接口定义了bean定义的注册方法
 * AbstractFactory 定义了bean定义对象的获取方法getBeanDefinition
 * */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();


    @Override   //从bean定义Map中获取BeanDefinition对象
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        // bean未定义异常, 不存在指定beanName的BeanDefinition对象
        if(beanDefinition == null)
            throw new BeansException("No bean named '" + beanName + "' is defined");
        return beanDefinition;
    }

    public boolean containsBeanDefinition(String beanName){
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    // 初始化所有bean对象
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }


    @Override
    // ListableBeanFactory接口中定义, 返回为type子类型或type类型的bean对象
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition)->{
            Class beanClass = beanDefinition.getBeanClass();
            /**
             * A.isAssignableFrom(B)
             * 确定一个类(B)是不是继承来自于另一个父类(A)，一个接口(A)是不是实现了另外一个接口(B)，或者两个类相同。
             * */
            if(type.isAssignableFrom(beanClass)){
                result.put(beanName, (T) getBean(beanName));
            }
        });
        return result;
    }

    @Override
    // ListableBeanFactory接口定义, 返回所有注册的bean的名称集合
    public String[] getBeanDefinitionNames() {
//        return (String[]) beanDefinitionMap.keySet().toArray();
//        注意上面的返回会出错, 因为声明Object[]的数组即使里面的元素为String, 也不能强制转换为String[]
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override // 通过类型获取bean对象, 用于@Autowired自动装配属性
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        List<String> beanNames = new ArrayList<>();
        beanDefinitionMap.forEach((beanName, beanDefinition)->{
            Class<?> beanClass = beanDefinition.getBeanClass();
            // 如果requiredType是beanClass的父类或同类, 则将beanName添加到集合中
            if(requiredType.isAssignableFrom(beanClass)){
                beanNames.add(beanName);
            }
        });
        if(beanNames.size() == 1) return getBean(beanNames.get(0), requiredType);

        throw new BeansException(requiredType + "expected single bean but found " + beanNames.size() + ": " + beanNames);
    }
}
