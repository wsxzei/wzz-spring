package cn.wzz.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.DisposableBean;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

/**
 * 适配器类: 需要在DefaultSingletonRegister中的容器进行注册
 * 当 JVM 关闭前, 会通过关闭钩子回调close方法;
 * 该方法会执行容器中所有DisposableBeanAdapter对象的destroy方法, 进行bean对象的销毁
 */
public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition){
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        boolean isDisposableBean;
        if(isDisposableBean = (bean instanceof DisposableBean)){
            ((DisposableBean)bean).destroy();
        }

        // 避免二次销毁
        if(StrUtil.isNotEmpty(destroyMethodName) && !isDisposableBean){
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if(destroyMethod == null){
                throw new BeansException("Couldn't find a destroy method named [" +
                        destroyMethodName + "] on bean with name [" + beanName + "].");
            }
            destroyMethod.invoke(bean);
        }
    }
}
