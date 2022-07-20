package cn.wzz.springframework.test.bean;

//定义UserService对象,用户后续对Spring容器的测试
public class UserService {

    private String name;

    //有参构造, 测试使用cglib实例化生成bean的动态代理对象
    public UserService(String name){
        this.name = name;
    }

    //注意: 有参构造存在时需要显示声明无参构造
    public UserService(){ }

    public void queryUserInfo(){
        System.out.println("查询用户[ " + name +  " ]的信息" );
    }
}
