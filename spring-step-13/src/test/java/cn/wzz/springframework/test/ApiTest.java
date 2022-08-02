package cn.wzz.springframework.test;


import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.test.bean.IUserService;
import org.junit.Test;

/** 13 版本
 * test_property测试自动替换占位符
 * */
public class ApiTest {

    @Test
    public void test_property(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-property.xml");
        IUserService userService = (IUserService) context.getBean("userService");
        System.out.println("测试结果: " + userService);
    }
/* 测试结果:
测试结果: UserService#token = {wsx9906291234.wzz9906291234}
 */
    @Test
    public void test_scan(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        IUserService userService = (IUserService) context.getBean("userService");
        System.out.println("测试结果: " + userService.queryUserInfo());
        IUserService userService2 = (IUserService) context.getBean("userService");
        System.out.println(userService2 == userService);
    }
/* 测试结果:
测试结果: wzz 0001 杭州
false
 */
}