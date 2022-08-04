package cn.wzz.springframework.test;


import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.test.bean.UserService;
import org.junit.Test;

/** 14 版本
 * test_scan测试:
 * 1. 自动扫描@Compoennt注解标记的类, 注册为bean;
 * 2. @Autowired自动注入字段类型的bean对象
 * 3. @Value自动注入String类型的成员属性, 并使用字符串解析器修改内嵌的占位符
 * */
public class ApiTest {
    @Test
    public void test_scan(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        UserService userService = (UserService) context.getBean("userService");
        System.out.println("测试结果: ");
        System.out.println("user[" + userService.getUid() + "]的信息: " + userService.queryUserInfo());
        System.out.println(userService);
    }
/* 测试结果:
测试结果:
user[002]的信息: wsx, 浙江杭州 余杭区
UserService#token = {wsx9906291234.wzz9906291234}
 */
}