package cn.wzz.springframework.context.annotation;

import java.lang.annotation.*;

//注解的作用目标类型为接口、类、枚举、方法
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    // 通过bean对象注解的时候, 拿到Bean对象的作用域
    String value() default "singleton";
}
