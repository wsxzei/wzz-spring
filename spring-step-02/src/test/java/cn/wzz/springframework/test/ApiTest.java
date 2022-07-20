package cn.wzz.springframework.test;


import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.wzz.springframework.test.bean.UserService;
import org.junit.Test;


/**
 * 此次的单元测试中除了包括；Bean 工厂、注册 Bean、获取 Bean，三个步骤
 * 还额外增加了一次对象的获取和调用，主要测试验证单例对象的是否正确的存放到了缓存中。*/
public class ApiTest {

    @Test
    public void test_BeanFactory(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2.注册bean(将UserService.class传递给BeanDefinition构造方法)
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);
        // 3.第一次获取bean(会调用createBean方法, 并放入单例对象缓存中)
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
        // 4.第二次从singletonObjects缓存中获取单例对象
        UserService userService_singleton = (UserService) beanFactory.getBean("userService");
        userService_singleton.queryUserInfo();
    }
}
