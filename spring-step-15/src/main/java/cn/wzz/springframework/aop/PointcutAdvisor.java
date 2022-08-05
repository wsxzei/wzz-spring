package cn.wzz.springframework.aop;

/* 该接口是Pointcut和Advice的组合, Pointcut用户获取JoinPoint, Advice用户决定对JoinPoint执行什么操作 */
public interface PointcutAdvisor extends Advisor{

    Pointcut getPointcut();
}
