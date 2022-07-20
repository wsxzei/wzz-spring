package cn.wzz.springframework.test;


import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.wzz.springframework.test.bean.UserService;
import org.junit.Test;

import java.lang.reflect.Constructor;


/**
 * 此次的单元测试中除了包括；Bean 工厂、注册 Bean、获取 Bean，三个步骤
 * 与02章不同点在于, 通过getBean方法传入bean实例化参数, 使用了有参构造方法*/
public class ApiTest {

    @Test // 测试无参构造方法实例化bean
    public void test_NoArgConstructor(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2.注册bean(将UserService.class传递给BeanDefinition构造方法)
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);
        // 3.第一次获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
    //结果: 查询用户[ null ]的信息

    @Test   //测试有参数构造方法实例化bean
    public void test_BeanFactory(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2.注册bean(将UserService.class传递给BeanDefinition构造方法)
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);
        // 3.第一次获取bean, 传入name属性的初始值 "wzz"
        UserService userService = (UserService) beanFactory.getBean("userService", "wzz");
        userService.queryUserInfo();
    }
    //结果: 查询用户[ wzz ]的信息

    @Test // 获取构造函数信息
    public void test_parameterTypes(){
        Class<UserService> beanClass = UserService.class;
        Constructor[] declaredConstructors = beanClass.getDeclaredConstructors();
        for(Constructor ctor: declaredConstructors){
            System.out.println(ctor);
        }
    }
    /**
     * 结果:
     * public cn.wzz.springframework.test.bean.UserService(java.lang.String)
     * public cn.wzz.springframework.test.bean.UserService()    */
}
