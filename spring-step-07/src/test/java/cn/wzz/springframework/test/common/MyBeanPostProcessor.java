package cn.wzz.springframework.test.common;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.BeanPostProcessor;
import cn.wzz.springframework.test.bean.UserService;

public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("userService")){
            ((UserService)bean).setLocation("杭州");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
