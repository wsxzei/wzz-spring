package cn.wzz.springframework.aop;

public interface Pointcut {

    /* 返回切点的ClassFilter */
    ClassFilter getClassFilter();

    /* 返回这个切点的MethodMatcher */
    MethodMatcher getMethodMatcher();

}
