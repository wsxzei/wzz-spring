package cn.wzz.springframework.context.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 配置到类上, Spring会自动扫描作为bean
public @interface Component {
    String value() default "";
}
