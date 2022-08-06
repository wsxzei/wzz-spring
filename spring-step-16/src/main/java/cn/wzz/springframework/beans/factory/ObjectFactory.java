package cn.wzz.springframework.beans.factory;

import cn.wzz.springframework.beans.BeansException;

/* 定义能返回Object实例的工厂 */
public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
