package cn.wzz.springframework.aop.framework;

import cn.wzz.springframework.aop.AdvisedSupport;
import cn.wzz.springframework.aop.AopProxy;

/* 代理工厂用于返回代理对象, 在DefaultAdvisorAutoProxyCreator中会利用该类生成代理对象 */
public class ProxyFactory {

    private AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport){
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy(){
        return createAopProxy().getProxy();
    }

    // 两种代理方式的选择
    private AopProxy createAopProxy(){
        if(advisedSupport.isProxyTargetClass()){
            return new Cglib2AopProxy(advisedSupport);
        }
        return new JdkDynamicAopProxy(advisedSupport);
    }
}
