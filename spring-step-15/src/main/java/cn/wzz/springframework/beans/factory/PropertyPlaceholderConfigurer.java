package cn.wzz.springframework.beans.factory;

import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.PropertyValue;
import cn.wzz.springframework.beans.PropertyValues;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.wzz.springframework.core.io.DefaultResourceLoader;
import cn.wzz.springframework.core.io.Resource;
import cn.wzz.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/* 属性占位符配置类, 在bean定义注册后, 实例化前执行占位符替换操作 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    private static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    // .properties文件的位置, xml文件中指定
    private String location;

    public void setLocation(String location){
        this.location = location;
    }

    @Override
    // 将BeanDefinition属性值中的占位符${proKey}全部替换为.properties文件中对应的proValue
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            // 获取资源对象
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            // 加载.properties文件的key-value到Properties对象
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for(String beanName: beanDefinitionNames){
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                // 获取属性集合
                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for(PropertyValue propertyValue: propertyValues.getPropertyValues()){
                    Object value = propertyValue.getValue();
                    if(!(value instanceof String)) continue;

                    value = resolvePlaceholder((String) value, properties);

                    PropertyValue newPropertyValue = new PropertyValue(propertyValue.getName(), value);
                    propertyValues.removePropertyValue(propertyValue.getName());
                    propertyValues.addPropertyValue(newPropertyValue);
                }
            }

            // 向容器中添加新的字符串解析器, 供解析@Value注解中的属性使用
            StringValueResolver resolver = new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(resolver);

        } catch (IOException e) {
            throw new BeansException("Couldn't load properties", e);
        }
    }

    // 将val字符串中的占位符替换为properties集合中的值
    private String resolvePlaceholder(String val, Properties properties){
        StringBuilder builder = new StringBuilder(val);
        // 获取占位符的起始位置和终止位置, 并全部替换为.properties文件中的属性
        int startIdx = builder.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int endIdx = builder.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        while(startIdx != -1 && endIdx != -1 && startIdx < endIdx){
            String proKey = builder.substring(startIdx + 2, endIdx);
            String proValue = properties.getProperty(proKey);
            // 替换字符串的占位符
            builder.replace(startIdx, endIdx + 1, proValue);
            startIdx += proValue.length();
            endIdx = startIdx;
            startIdx = builder.indexOf(DEFAULT_PLACEHOLDER_PREFIX, startIdx);
            endIdx = builder.indexOf(DEFAULT_PLACEHOLDER_SUFFIX, endIdx);
        }
        return builder.toString();
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver{

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties){
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }
    }

}
