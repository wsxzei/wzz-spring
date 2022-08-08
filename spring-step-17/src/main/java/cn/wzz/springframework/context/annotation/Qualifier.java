package cn.wzz.springframework.context.annotation;

import java.lang.annotation.*;

// 注解的作用对象为方法参数、字段、方法、类、注解
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Qualifier {
    String value() default "";
}
