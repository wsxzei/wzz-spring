package cn.wzz.springframework.test;


import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.test.bean.IUserService;
import org.junit.Test;

/** 15 版本

 * */
public class ApiTest {
    @Test
    public void test_autoProxy(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = (IUserService) context.getBean("userService");
        System.out.println(userService.queryUserInfo());
        System.out.println(userService);
    }
/* 测试结果
拦截方法: queryUserInfo
wzz, 001 上海
UserService#token = {123456}
 */
}