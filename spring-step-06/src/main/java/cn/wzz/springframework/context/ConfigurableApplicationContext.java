package cn.wzz.springframework.context;

import cn.wzz.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{
    /**
     * 核心方法: 刷新容器
     * */
    void refresh() throws BeansException;
}
