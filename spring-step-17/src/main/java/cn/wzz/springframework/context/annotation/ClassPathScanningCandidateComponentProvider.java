package cn.wzz.springframework.context.annotation;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.util.ClassUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/* 扫描classpath路径下所有包含Component注解的类, 生成BeanDefinition集合 */
public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage){
        try {
            Set<BeanDefinition> candidates = new LinkedHashSet<>();
            // 扫描basePackage目录下所有的类, 返回注解@Component修饰的Class集合
            Set<Class<?>> classes = null;
            classes = ClassUtils.scanPackageByAnnotation(basePackage, Component.class);
            for(Class<?> clazz: classes){
                candidates.add(new BeanDefinition(clazz));
            }
            return candidates;
        } catch (ClassNotFoundException e) {
            throw new BeansException("Fail to findCandidateComponents", e);
        }
    }
}
