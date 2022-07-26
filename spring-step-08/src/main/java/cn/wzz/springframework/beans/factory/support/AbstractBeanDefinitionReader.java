package cn.wzz.springframework.beans.factory.support;

import cn.wzz.springframework.core.io.DefaultResourceLoader;
import cn.wzz.springframework.core.io.ResourceLoader;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    /* 通过构造方法传入 资源加载器 和 bean定义注册点 */
    private final BeanDefinitionRegistry registry;

    /* 通过资源加载器构造Resource对象, 使得资源真正能被使用 */
    private final ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry){
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader){
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }


    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
