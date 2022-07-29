package cn.wzz.springframework.test.event;

import cn.wzz.springframework.context.ApplicationListener;
import cn.wzz.springframework.context.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("容器[" + event.getSource()
                + "]关闭事件; 监听者[" + this.getClass().getSuperclass().getSimpleName() + "]正在处理....");
    }
}
