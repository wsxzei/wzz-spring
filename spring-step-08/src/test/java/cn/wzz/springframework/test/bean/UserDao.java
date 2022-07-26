package cn.wzz.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    // 模拟数据库查询
    private static Map<String,String> userMap = new HashMap<>();

    // 不再使用静态代码块填充数据, 而是使用bean的初始化方法
    public void initDataMethod(){
        System.out.println("执行: init-method...");
        userMap.put("001", "wzz");
        userMap.put("002", "wsx");
    }

    public void destroyDataMethod(){
        System.out.println("执行: destroy-method...");
        userMap.clear();
    }

    // DAO(数据访问对象) 的查询操作
    public String queryUserName(String userId){
        return userMap.get(userId);
    }
}
