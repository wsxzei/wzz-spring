package cn.wzz.springframework.test;

import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.test.bean.UserService;

import org.junit.Test;


/** 09 版本
 *  1. 测试原型模式获取的两个UserService对象是否是同一个对象(test_SingletonAndPrototype)
 *  2. 测试FactoryBean, ProxyFactoryBean实现了FactoryBean<IUserDao>
 *      测试能否二次利用FactoryBean的getObject方法将 实现了IUserDao接口的代理类 暴露给UserService中的userDao属性
 *      (test_factoryBean)
 *  */
public class ApiTest {

    @Test
    public void test_SingletonAndPrototype(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        context.registerShutDownHook();

        UserService userService01 = (UserService) context.getBean("userService");
        UserService userService02 = (UserService) context.getBean("userService");

        System.out.println(userService01 == userService02);
        // false
    }


    @Test
    public void test_factoryBean(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        context.registerShutDownHook();

        UserService userService = (UserService) context.getBean("userService");
        userService.queryUserInfo();
    }
    /** 测试结果
     * IUserDao 已经被代理...
     * 代理方法为[queryUserName]
     * 代理参数为[wzz]
     * JVM closing...
     */
}