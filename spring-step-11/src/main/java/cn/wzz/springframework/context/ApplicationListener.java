package cn.wzz.springframework.context;

import java.util.EventListener;

public interface ApplicationListener <E extends ApplicationEvent> extends EventListener {

    /* 处理一个Application事件 */
    void onApplicationEvent(E event);
}
