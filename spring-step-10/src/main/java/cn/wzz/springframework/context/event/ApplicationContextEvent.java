package cn.wzz.springframework.context.event;

import cn.wzz.springframework.context.ApplicationContext;
import cn.wzz.springframework.context.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent {

    // 与父类匹配的构造函数
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    /**
     * 获取事件产生的应用上下文
     * getSource继承自EventObject类的方法, 返回source对象
     * 构造应用上下文事件时, 需要将事件发生的source对象, 即ApplicationContext传递到构造函数中
     */
    public final ApplicationContext getApplicationContext(){
        return (ApplicationContext) getSource();
    }
}
