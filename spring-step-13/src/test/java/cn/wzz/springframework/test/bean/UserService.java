package cn.wzz.springframework.test.bean;

import cn.wzz.springframework.context.annotation.Component;
import cn.wzz.springframework.context.annotation.Scope;

import java.util.Random;

@Component("userService")
@Scope("prototype")
public class UserService implements IUserService{

    private String token;

    public String queryUserInfo() {
        try {
            // 生成0~100范围内的任意整数, 不包括100
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "wzz 0001 杭州";
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
