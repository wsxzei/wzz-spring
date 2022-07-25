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


/** 07 版本
 * 在xml文件中设置userDao的init-method和destroy-method属性
 * UserService实现 InitializingBean 实现 DisposableBean接口
 *  */
public class ApiTest {

    @Test
    public void test_initializeAndDestroyMethod(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        context.registerShutDownHook();// 注册关闭钩子
        UserService userService = (UserService)context.getBean("userService");
        userService.queryUserInfo();
    }
    /* 结果:
执行: init-method...
执行: UserService.afterPropertiesSet
查询到用户信息: 姓名[wzz]; 公司[阿里巴巴]; 居住地[杭州].
执行: destroy-method...
执行: UserService.destroy*/

}
