package cn.wzz.springframework.test.bean;

public class Wife {

    private Husband husband;

    public String queryHusband(){
        return husband.selfIntroduction();
    }

    public String selfIntroduction(){
        return "I'm Wife!";
    }
}
