package cn.wzz.springframework.context;

public interface ApplicationEventPublisher {

    /* 将应用事件通知所有注册到此应用的监听器 */
    void publishEvent(ApplicationEvent event);
}
