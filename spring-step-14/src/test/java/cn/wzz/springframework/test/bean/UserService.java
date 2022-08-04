package cn.wzz.springframework.test.bean;

import cn.wzz.springframework.context.annotation.Autowired;
import cn.wzz.springframework.context.annotation.Component;
import cn.wzz.springframework.context.annotation.Value;

import java.util.Random;

@Component("userService")
public class UserService implements IUserService{

    @Value("${token1}.${token2}")
    private String token;

    @Value("${uid}")
    private String uid;

    @Autowired
    private UserDao userDao;

    public String getUid(){
        return uid;
    }

    public String queryUserInfo() {
        try {
            // 生成0~100范围内的任意整数, 不包括100
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userDao.queryUserName(uid);
    }

    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

    public String toString(){
        return "UserService#token = {" + token + "}";
    }

}
