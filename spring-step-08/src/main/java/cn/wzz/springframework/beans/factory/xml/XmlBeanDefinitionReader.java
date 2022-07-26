package cn.wzz.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.wzz.springframework.beans.BeansException;
import cn.wzz.springframework.beans.PropertyValue;
import cn.wzz.springframework.beans.factory.config.BeanDefinition;
import cn.wzz.springframework.beans.factory.config.BeanReference;
import cn.wzz.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import cn.wzz.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.wzz.springframework.core.io.Resource;
import cn.wzz.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry){
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader){
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try (InputStream is = resource.getInputStream()) {
                doLoadBeanDefinition(is);
            }
        }catch (IOException | ClassNotFoundException e){
            throw new BeansException("IOException parsing XML document from" + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for(Resource resource:resources){
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        // 资源加载器构建Resource对象
        Resource resource = getResourceLoader().getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for(String location: locations){
            loadBeanDefinitions(location);
        }
    }

    // 从Resource对象的输入流中, 读取xml文件配置信息, 从而构建BeanDefinition对象
    protected void doLoadBeanDefinition(InputStream inputStream) throws ClassNotFoundException {
        Document document = XmlUtil.readXML(inputStream);
        Element root =  document.getDocumentElement(); // 根节点
        NodeList nodes = root.getChildNodes();

        for(int i = 0; i < nodes.getLength(); i++){
            // 判断元素
            if(!(nodes.item(i) instanceof Element)) continue;
            // 判断是否为bean标签
            if(!nodes.item(i).getNodeName().equals("bean"))  continue;

            // 解析标签
            Element bean = (Element) nodes.item(i);
            String id = bean.getAttribute("id");
            String className = bean.getAttribute("class");
            String name = bean.getAttribute("name");
            // 获取配置文件中bean的初始化方法和销毁属性
            String initMethodName = bean.getAttribute("init-method");
            String destroyMethodName = bean.getAttribute("destroy-method");

            // 获取Class
            Class clazz = Class.forName(className);
            // 优先级 id > name, 如果id不为null且非空, 则将 id 作为beanName
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if(StrUtil.isEmpty(beanName)){
                // 将简单的类名的首字母变为小写, 作为bean的名称
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            // 定义 Bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);

            // 读取属性进行填充
            NodeList propertyNodes = bean.getChildNodes();
            for(int j = 0; j < propertyNodes.getLength(); j++){
                if(!(propertyNodes.item(j) instanceof Element)) continue;
                if(!propertyNodes.item(j).getNodeName().equals("property")) continue;

                // 解析标签property
                Element property = (Element) propertyNodes.item(j);
                String attributeName = property.getAttribute("name");
                String attributeValue = property.getAttribute("value");
                String reference = property.getAttribute("ref");

                // 设置PropertyValue的value属性, 如果为引用类型, value为BeanReference对象
                Object value = StrUtil.isNotEmpty(reference) ? new BeanReference(reference) : attributeValue;

                // 将属性对象加入beanDefinition中
                PropertyValue propertyValue = new PropertyValue(attributeName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }

            // 设置初始化方法和销毁方法
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);

            // 将解析好的beanDefinition对象进行注册
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }


}
