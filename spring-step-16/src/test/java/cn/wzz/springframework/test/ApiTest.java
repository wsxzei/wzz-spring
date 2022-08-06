package cn.wzz.springframework.test;


import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.test.bean.Husband;
import cn.wzz.springframework.test.bean.Wife;
import org.junit.Test;

/** 16 版本
 * test_aopCircular 测试动态代理对象的属性在循环依赖条件下能够被成功注入
 *  这里husband和wife都是AOP动态代理对象, 且均被注入了对方属性中; 可以通过husband
 * */
public class ApiTest {
    @Test
    public void test_aopCircular(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = (Husband) context.getBean("husband");
        Wife wife = (Wife) context.getBean("wife");
        System.out.println("测试Husband: " + husband.queryWife());
        System.out.println("测试Wife: " + wife.queryHusband());
    }
/*
前置拦截, method: public java.lang.String cn.wzz.springframework.test.bean.Husband.queryWife()
前置拦截, method: public java.lang.String cn.wzz.springframework.test.bean.Wife.selfIntroduction()
测试Husband: I'm Wife!
前置拦截, method: public java.lang.String cn.wzz.springframework.test.bean.Wife.queryHusband()
前置拦截, method: public java.lang.String cn.wzz.springframework.test.bean.Husband.selfIntroduction()
测试Wife: I'm Husband!
 */
}