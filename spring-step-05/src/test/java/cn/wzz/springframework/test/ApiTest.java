package cn.wzz.springframework.test;


import cn.hutool.core.io.IoUtil;
import cn.wzz.springframework.beans.PropertyValue;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.BeanReference;
import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.wzz.springframework.core.io.DefaultResourceLoader;
import cn.wzz.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.wzz.springframework.core.io.Resource;
import cn.wzz.springframework.test.bean.UserDao;
import cn.wzz.springframework.test.bean.UserService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/** 05版本：
 * 1. 先对资源加载和读取进行测试, 分别测试了classpath, 文件系统, url资源
 *      使用了hutool提供的IO工具类读取输入流
 * 2. test_xml 测试了从xml配置文件中读取bean定义信息, 最后实例化bean对象
 *      2.1 先实例化DefaultListableBeanFactory
 *      2.2 构造XmlBeanDefinitionReader对象(传入资源构造器和BeanDefinition注册点), 用于解析xml资源并注册bean定义
 *      2.3 使用reader的loadBeanDefinitions方法完成上述逻辑
 * */
public class ApiTest {

    @Test
    //测试有参数构造方法实例化bean
    public void test_BeanFactory(){
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.注册UserDao, UserService引用属性的注入依赖这个bean的实例化
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        // 3.UserService 设置属性[userId, userDao], 引用类型的属性值设置为BeanReference对象
        PropertyValue propertyValue = new PropertyValue("userId", "001");
        PropertyValue propertyValue1 = new PropertyValue("userDao", new BeanReference("userDao"));
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(propertyValue);
        propertyValues.addPropertyValue(propertyValue1);

        // 4.UserService注册
        beanFactory.registerBeanDefinition("userService", new BeanDefinition(UserService.class, propertyValues));

        // 5.获取bean对象, queryUserInfo会调用userDao的的查询方法(模拟数据库的查询)
        UserService userService =(UserService)beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
    // 结果: 查询到用户信息: wzz


    private DefaultResourceLoader resourceLoader;

    @Before
    public void init(){
        resourceLoader = new DefaultResourceLoader();
    }

    // 测试classpath下的资源 important.properties(放于resources目录下)
    // 如果找不到文件, 则修改pom.xml配置, 增加 build>resources设置
    @Test
    public void test_classpath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:important.properties");
        InputStream is = resource.getInputStream();
        String content = IoUtil.read(is, StandardCharsets.UTF_8);
        System.out.println(content);
    }

    @Test
    // 测试系统文件资源, 当前目录为项目(或模块)的根目录
    public void test_file()throws IOException{
        Resource resource = resourceLoader.getResource("src/test/resources/important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        System.out.println(content);
    }

    /** 结果:
     * # config File
     * system.key = xxxxxxx
     * */

    @Test
    // 使用url读取网络上的资源(以csapp官网的代码页面为例)
    public void test_url() throws IOException {
        Resource resource = resourceLoader.getResource("http://www.cs.cmu.edu/afs/cs/academic/class/15213-f15/www/code/02-03-bits-ints/show-bytes.c");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        System.out.println(content);
    }

    @Test
    public void test_xml()throws IOException{
        // 1. 实例化工厂, 将bean定义放入容器中管理
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2. 读取配置文件, 并注册bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory, resourceLoader);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3.获取userService对象, 调用queryInfo方法
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }

    // 结果: 查询要用户信息: wzz
}
