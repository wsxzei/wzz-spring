package cn.wzz.springframework.test;


import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.test.event.CustomEvent;
import org.junit.Test;

/** 10 版本
 *
 * */
public class ApiTest {
    @Test
    public void test_event(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        context.registerShutDownHook();
        context.publishEvent(new CustomEvent(this, 100L, "Hello World"));
    }
    /* 测试结果
容器[cn.wzz.springframework.context.support.ClassPathXmlApplicationContext@1fbc7afb]刷新完成事件; 监听者[ContextRefreshedEventListener]正在处理....
收到: cn.wzz.springframework.test.ApiTest@4cdbe50f消息;时间：Fri Jul 29 10:29:07 CST 2022
消息: 100:Hello World
JVM closing...
容器[cn.wzz.springframework.context.support.ClassPathXmlApplicationContext@1fbc7afb]关闭事件; 监听者[ContextClosedEventListener]正在处理....
*/

}