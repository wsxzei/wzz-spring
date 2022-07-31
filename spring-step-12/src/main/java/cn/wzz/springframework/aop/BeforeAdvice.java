package cn.wzz.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * Tag interface for Advice. Implementations can be any type of advice, such as Interceptors.<p>
 * Advice为通知, 表示需要执行的增强逻辑方法; 继承该接口的有 BeforeAdvice, AfterAdvice, Interceptor 接口
 */

public interface BeforeAdvice extends Advice {
}
