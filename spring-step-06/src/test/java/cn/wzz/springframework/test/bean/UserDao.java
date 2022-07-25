package cn.wzz.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    // 模拟数据库查询
    private static Map<String,String> userMap = new HashMap<>();

    static {
        userMap.put("001", "wzz");
        userMap.put("002", "wsx");
    }

    // DAO(数据访问对象) 的查询操作
    public String queryUserName(String userId){
        return userMap.get(userId);
    }
}
