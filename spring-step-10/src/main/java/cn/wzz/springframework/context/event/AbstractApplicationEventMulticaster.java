package cn.wzz.springframework.context.event;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.BeanFactory;
import cn.wzz.springframework.beans.factory.BeanFactoryAware;
import cn.wzz.springframework.context.ApplicationEvent;
import cn.wzz.springframework.context.ApplicationListener;
import cn.wzz.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    // 事件监听者集合
    private final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory factory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    // 从applicationListeners中, 获取监听event事件类型的监听者集合
    protected Collection<ApplicationListener<ApplicationEvent>> getApplicationListeners(ApplicationEvent event){
        List<ApplicationListener<ApplicationEvent>> allListeners = new LinkedList<>();
        for(ApplicationListener<ApplicationEvent> listener: applicationListeners){
            if(supportEvent(listener, event)){
                allListeners.add(listener);
            }
        }
        return allListeners;
    }

    // 检查listener是否监听event类型的事件
    private boolean supportEvent(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event){

        Class<? extends ApplicationListener> listenerClass = listener.getClass();


        // CglibSubclassingInstantiationStrategy、SimpleInstantiationStrategy不同的实例化, 获取类对象的方式不同
        Class<?> targetClass = ClassUtils.isCglibProxyClass(listener.getClass()) ?
                listenerClass.getSuperclass(): listenerClass;

        // 需要获取listener的范型接口的类型数组, 其中的参数化类型即为ApplicationListener接口, 再获取实际类型参数即可
        Type[] genericInterfaces = targetClass.getGenericInterfaces();

        Class<?> eventClass = null;

        for(Type type: genericInterfaces){
            // 如果为参数化类型(泛型接口), 且原始类型的名称等于ApplicationListener, 则获取实际类型参数
            if(type instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if(parameterizedType.getRawType().getTypeName().equals(ApplicationListener.class.getName())){
                    eventClass = (Class<?>)parameterizedType.getActualTypeArguments()[0];
                    break;
                }
            }
        }
        // listener监听的事件与event是相同的类, 或者是event的父类; 则返回true
        // A.isAssignableFrom(B), A可以由B转换而来
        return eventClass != null && eventClass.isAssignableFrom(event.getClass());
    }
}
