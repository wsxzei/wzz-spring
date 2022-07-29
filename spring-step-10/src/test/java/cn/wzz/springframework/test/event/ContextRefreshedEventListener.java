package cn.wzz.springframework.test.event;

import cn.wzz.springframework.context.ApplicationListener;
import cn.wzz.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("容器[" + event.getSource()
                + "]刷新完成事件; 监听者[" + this.getClass().getSuperclass().getSimpleName() + "]正在处理....");
    }
}
