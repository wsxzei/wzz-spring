package cn.wzz.springframework.context.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.BeanFactory;
import cn.wzz.springframework.beans.factory.BeanFactoryAware;
import cn.wzz.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.wzz.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.wzz.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/* 在bean实例化后注入bean定义中的属性前, 扫描自定义注解, 注入注解属性 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    /*
    该方法在DefaultAdvisorAutoProxyCreator中用于生成AOP代理
    会使用到切面信息类AspectJExpressionPointcutAdvisor判断beanClass是否存在切面
     */
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    /*
    Bean对象实例化完成后, 设置属性操作前处理属性集合pvs
    需要扫描的注解有@Autowired, @Value
     */
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 如果bean对象是采用Cglib字节码增强技术实例化生成, 则需要获取超类
        beanClass = ClassUtils.isCglibProxyClass(beanClass) ? beanClass.getSuperclass() : beanClass;
        Field[] fields = beanClass.getDeclaredFields();

        for(Field field: fields){
            // 1. 处理注解@Value
            if(field.isAnnotationPresent(Value.class)){
                Value valueAnnotation = field.getAnnotation(Value.class);
                String value = valueAnnotation.value();
                // 使用AbstractBeanFactory中的解析器集合解析注解属性值
                value = beanFactory.resolveEmbeddedValue(value);
                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
            // 2.处理注解@Autowired
            if(field.isAnnotationPresent(Autowired.class)){
                Annotation autowiredAnnotation = field.getAnnotation(Autowired.class);
                // 获取字段类型
                Class<?> fieldType = field.getType();
                String dependentBeanName = null;
                Object dependentBean = null;
                if(field.isAnnotationPresent(Qualifier.class)){
                    dependentBeanName = field.getAnnotation(Qualifier.class).value();
                    // 获取指定名称的bean对象, 该bean对象必须为fieldType类型
                    dependentBean = beanFactory.getBean(dependentBeanName, fieldType);
                }else{
                    /*
                    没有使用@Qualifier指定名称, 则按照类型进行Bean注入;
                    若有多个同类型bean则会抛出BeansException
                     */
                    dependentBean = beanFactory.getBean(fieldType);
                }
                BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
            }
        }
        return pvs;
    }
}
