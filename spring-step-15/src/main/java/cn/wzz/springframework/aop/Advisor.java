package cn.wzz.springframework.aop;

import org.aopalliance.aop.Advice;

public interface Advisor {
    /**
     * 返回这个切面的通知(advice)部分, 一个通知可能是一个拦截器，一个before advice等
     * @return 如果切点匹配, 返回需要被应用的advice
     */
    Advice getAdvice();
}
