package cn.wzz.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Set;

/* 获取到自动扫描的bean定义后, 设置bean的名称和作用域; 然后注册到Spring容器中 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry){
        this.registry = registry;
    }

    public void doScan(String... basePackages){
        for(String basePackage: basePackages){
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for(BeanDefinition beanDefinition: candidates){
                // 解析bean的作用域
                String scope = resolveScope(beanDefinition);
                beanDefinition.setScope(scope);
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }

        }
    }

    // 确定BeanName, 先通过@Component的value属性获取beanName;如果没有则使用首字母小写的类名称
    private String determineBeanName(BeanDefinition beanDefinition){
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String beanName = component.value();
        if(StrUtil.isEmpty(beanName)){
            beanName = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return beanName;
    }

    // 解析bean定义的作用域, 注解@Scope指定
    private String resolveScope(BeanDefinition beanDefinition){
        Class<?> beanClass = beanDefinition.getBeanClass();
        String scope = "singleton";
        if(beanClass.isAnnotationPresent(Scope.class)){
            Scope scopeAnnotation = beanClass.getAnnotation(Scope.class);
            if(scopeAnnotation != null)
                scope = scopeAnnotation.value();
        }
        return scope;
    }
}
