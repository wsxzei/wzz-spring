package cn.wzz.springframework.test.bean;

public class Husband {

    private Wife wife;

    public String queryWife(){
        return wife.selfIntroduction();
    }

    public String selfIntroduction(){
        return "I'm Husband!";
    }
}
