package cn.wzz.springframework.aop.aspectj;

import cn.wzz.springframework.aop.Pointcut;
import cn.wzz.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/* 将切面pointcut, 拦截方法advice和具体的拦截表达式包装在一起;
*  在xml配置文件中可以定义一个pointAdvisor切面, 需要设置advice和expression属性*/
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    /* 切面 */
    private AspectJExpressionPointcut pointcut;

    /* 切面表达式 */
    private String expression;

    /* 具体的拦截方法 */
    private Advice advice;

    public void setPointcut(String expression){
        this.expression = expression;
    }

    @Override
    public Pointcut getPointcut(){
        if(pointcut == null){
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice){
        this.advice = advice;
    }

}
