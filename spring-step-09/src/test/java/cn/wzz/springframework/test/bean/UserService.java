package cn.wzz.springframework.test.bean;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.*;
import cn.wzz.springframework.context.ApplicationContext;
import cn.wzz.springframework.context.ApplicationContextAware;
import lombok.Data;

/**
 * 08 版本测试: 实现各种感知接口, 从而获取到
 * bean对象在Spring容器中的名称-->beanName
 * 管理bean对象的工厂对象-->beanFactory
 * 加载bean对象的类加载器-->classLoader
 * bean运行所处的应用上下文-->applicationContext
 */
@Data
public class UserService {

    private String userId;
    private IUserDao userDao;
    private String company;
    private String location;


    public String  toString(){
        StringBuilder builder = new StringBuilder("");
        builder.append("UserService:\n")
                .append("\tuserId = "+ userId + "\n")
                .append("\tuserDao = "+ userDao + "\n")
                .append("\tcompany = "+ company + "\n")
                .append("\tlocation = "+ location + "\n");
//                .append("\n\tbeanName = "+ beanName + "\n")
//                .append("\tapplicationContext = "+ applicationContext + "\n")
//                .append("\tbeanFactory = "+ beanFactory + "\n")
//                .append("\tclassLoader = "+ classLoader + "\n");
        return builder.toString();
    }

    public void queryUserInfo(){
        userDao.queryUserName("001");
    }


}
