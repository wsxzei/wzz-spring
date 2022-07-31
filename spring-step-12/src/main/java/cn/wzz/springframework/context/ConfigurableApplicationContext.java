package cn.wzz.springframework.context;

import cn.wzz.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{
    /**
     * 核心方法: 刷新容器
     * */
    void refresh() throws BeansException;

    /* 注册ShutDown钩子 */
    void registerShutDownHook();

    /* 虚拟机ShutDown钩子的回调方法 */
    void close();
}
