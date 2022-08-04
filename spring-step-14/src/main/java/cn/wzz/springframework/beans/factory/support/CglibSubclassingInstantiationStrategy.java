package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * cglib实例化
 * */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args)
            throws BeansException {
        Enhancer enhancer = new Enhancer();

        // 设置增强bean是哪个类的代理
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        // NoOp回调: 把对方法的调用直接委派给父类中这个方法的实现, 相当于直接调用父类的方法
        enhancer.setCallback(new NoOp() {});

        if(ctor == null)
            return enhancer.create();

        // 返回动态代理对象(增强bean对象)
        return enhancer.create(ctor.getParameterTypes(), args);
    }
}
