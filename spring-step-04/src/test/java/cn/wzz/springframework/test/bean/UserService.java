package cn.wzz.springframework.test.bean;

/**
 * 定义UserService对象,用户后续对Spring容器的测试
 * 本次测试使用属性注入而非构造函数的方式, 对UserService的值进行初始化
 * 对于UserDao属性的注入 需要先将 BeanDefinition 注册到 beanDefinitionMap, Spring 框架才能进行实例化
 */
public class UserService {

    private String userId;

    private UserDao userDao;


    public void queryUserInfo(){
        System.out.println("查询到用户信息: " + userDao.queryUserName(userId));
    }
}
