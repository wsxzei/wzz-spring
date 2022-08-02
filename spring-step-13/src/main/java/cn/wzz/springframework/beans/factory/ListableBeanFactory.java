package cn.wzz.springframework.beans.factory;

import cn.wzz.springframework.beans.BeansException;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory {

    /**
     * 返回此BeanFactory中所有指定类型（或指定类型的子类型）的Bean的名字(通过bean定义判断)
     * */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回此BeanFactory中所包含的所有Bean定义的名称
     * */
    String[] getBeanDefinitionNames();
}
