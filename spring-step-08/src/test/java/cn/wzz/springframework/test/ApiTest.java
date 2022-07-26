package cn.wzz.springframework.test;


import cn.hutool.core.io.IoUtil;
import cn.wzz.springframework.beans.PropertyValue;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.BeanReference;
import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.core.io.DefaultResourceLoader;
import cn.wzz.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.wzz.springframework.core.io.Resource;
import cn.wzz.springframework.test.bean.UserDao;
import cn.wzz.springframework.test.bean.UserService;
import cn.wzz.springframework.test.common.MyBeanFactoryPostProcessor;
import cn.wzz.springframework.test.common.MyBeanPostProcessor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/** 08 版本
 *  bean对象userService实现各种 Aware 接口;
 *  感知诸如 Spring 框架提供的 BeanFactory、ApplicationContext、BeanClassLoader等对象
 *  */
public class ApiTest {

    @Test
    public void test_aware(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        context.registerShutDownHook();

        UserService userService = (UserService) context.getBean("userService");
        System.out.println(userService);
    }

}
