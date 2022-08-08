package cn.wzz.springframework.test.converter;

import cn.wzz.springframework.beans.factory.FactoryBean;

import java.util.HashSet;
import java.util.Set;

public class ConvertersFactoryBean implements FactoryBean<Set<?>> {
    @Override
    public Set<?> getObject() throws Exception {
        Set<Object> converters = new HashSet<>();
        converters.add(new StringToLocalDateConverter("yyyy-MM-dd"));
        return converters;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
