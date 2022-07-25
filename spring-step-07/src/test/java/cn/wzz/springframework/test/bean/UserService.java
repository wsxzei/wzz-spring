package cn.wzz.springframework.test.bean;

import cn.wzz.springframework.beans.factory.DisposableBean;
import cn.wzz.springframework.beans.factory.InitializingBean;
import lombok.Data;

/**
 * 04版本的测试:
 * 定义UserService对象,用户后续对Spring容器的测试
 * 本次测试使用属性注入而非构造函数的方式, 对UserService的值进行初始化
 * 对于UserDao属性的注入 需要先将 BeanDefinition 注册到 beanDefinitionMap, Spring 框架才能进行实例化
 *
 * 05 版本开始实现了加载配置文件、读取bean定义、注册bean 等自动化操作, 无需手动注册BeanDefinition
 *
 */
@Data
public class UserService implements InitializingBean, DisposableBean{
    private String userId;

    private UserDao userDao;

    private String company;

    private String location;


    public void queryUserInfo(){
        System.out.println("查询到用户信息: 姓名[" + userDao.queryUserName(userId)
                +"]; 公司[" + company + "]; 居住地[" + location + "].");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("执行: UserService.destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("执行: UserService.afterPropertiesSet");
    }
}
