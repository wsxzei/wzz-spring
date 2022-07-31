package cn.wzz.springframework.context.event;

import cn.wzz.springframework.beans.factory.BeanFactory;
import cn.wzz.springframework.context.ApplicationEvent;
import cn.wzz.springframework.context.ApplicationListener;

import java.util.Collection;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    /* beanFactory在这里的作用? */
    public SimpleApplicationEventMulticaster(BeanFactory beanFactory){
        setBeanFactory(beanFactory);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        Collection<ApplicationListener<ApplicationEvent>> applicationListeners = getApplicationListeners(event);
        for(ApplicationListener<ApplicationEvent> listener: applicationListeners){
            listener.onApplicationEvent(event);
        }
    }
}
