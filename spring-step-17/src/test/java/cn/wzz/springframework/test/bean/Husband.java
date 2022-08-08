package cn.wzz.springframework.test.bean;

import java.time.LocalDate;

public class Husband {

    private Integer age;

    private LocalDate marriageDate; // 结婚日期

    public String toString(){
        return "Age: " + age + "; marriageDate: " + marriageDate.toString();
    }
}
