package cn.wzz.springframework.test;

import cn.wzz.springframework.BeanDefinition;
import cn.wzz.springframework.BeanFactory;
import cn.wzz.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_BeanFactory(){
        // 1.初始化BeanFactory
        BeanFactory beanFactory = new BeanFactory();

        // 2.注册bean
        BeanDefinition beanDefinition = new BeanDefinition(new UserService());
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // 3.获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
