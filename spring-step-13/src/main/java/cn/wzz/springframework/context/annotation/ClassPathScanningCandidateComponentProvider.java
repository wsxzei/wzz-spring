package cn.wzz.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;

import java.util.LinkedHashSet;
import java.util.Set;

/* 扫描classpath路径下所有包含Component注解的类, 生成BeanDefinition集合 */
public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage){
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        // 扫描basePackage目录下所有的类, 返回注解@Component修饰的Class集合
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for(Class<?> clazz: classes){
            candidates.add(new BeanDefinition(clazz));
        }
        return candidates;
    }
}
