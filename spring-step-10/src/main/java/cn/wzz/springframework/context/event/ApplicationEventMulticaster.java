package cn.wzz.springframework.context.event;

import cn.wzz.springframework.context.ApplicationEvent;
import cn.wzz.springframework.context.ApplicationListener;

public interface ApplicationEventMulticaster {

    /* 将监听者加入到由事件广播器中的监听者集合中 */
    void addApplicationListener(ApplicationListener<?> listener);

    /* 移除监听者 */
    void removeApplicationListener(ApplicationListener<?> listener);

    /* 广播给定的应用事件给特定的监听者 */
    void multicastEvent(ApplicationEvent event);
}
