package cn.wzz.springframework.test;


import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.test.bean.IUserService;
import org.junit.Test;

/** 12 版本
 * 测试xml中切面信息、自定义方法拦截器、BeforeAdvice类的自动加载
 * 并通过这些信息获取(自动代理创建者)在(存在切面的Bean)实例化前，生成并返回代理对象
 * */
public class ApiTest {

    @Test
    public void test_aop(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        // 注意: 不要将类型设置为UserService, 代理对象实现的是接口IUserService的功能并不能转换为UserService对象!!!
        IUserService userService = (IUserService) context.getBean("userService");
        userService.queryUserInfo();
//        System.out.println("测试结果: " + userService.queryUserInfo());
    }
/* 测试结果:
拦截方法: queryUserInfo
测试结果: wzz 0001 杭州

Begin before method...
拦截方法: queryUserInfo
End before method.
Begin afterReturning method...
被拦截对象: cn.wzz.springframework.test.bean.UserService@627551fb	方法: queryUserInfo 调用结束..
返回结果为: wzz 0001 杭州
End afterReturning method.
*/

}