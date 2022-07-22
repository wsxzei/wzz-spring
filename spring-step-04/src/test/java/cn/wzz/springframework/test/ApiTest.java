package cn.wzz.springframework.test;


import cn.wzz.springframework.beans.PropertyValue;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.BeanReference;
import cn.wzz.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.wzz.springframework.test.bean.UserDao;
import cn.wzz.springframework.test.bean.UserService;
import org.junit.Test;


/** 04版本:
 * 与之前的测试不同, 不再通过构造函数进行属性的初始化, 而是使用属性注入的方式
 * 1. 需要先注册UserService userDao属性依赖的UserDao类型作为spring管理的bean
 * 2. 构造 PropertyValues对象, 并与UserService.class一起传入BeanDefinition的构造方法
 * 3. 注册 UserService的 beanDefinition
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

}
