package cn.wzz.springframework.context;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.Aware;
import cn.wzz.springframework.context.ApplicationContext;

/**
 * Interface to be implemented by any object that wishes to be notified of the {@link ApplicationContext} that it runs in.
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext context) throws BeansException;
}
