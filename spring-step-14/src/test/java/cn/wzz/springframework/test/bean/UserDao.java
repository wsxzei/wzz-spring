package cn.wzz.springframework.test.bean;

import cn.wzz.springframework.context.annotation.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDao {
    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("001", "wzz, 上海徐汇区 徐汇区漕河泾");
        hashMap.put("002", "wsx, 浙江杭州 余杭区");
    }

    public String queryUserName(String uid){
        return hashMap.get(uid);
    }
}
