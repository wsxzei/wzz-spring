package cn.wzz.springframework.core.convert.support;

import cn.wzz.springframework.core.convert.converter.ConverterRegistry;

public class DefaultConversionService extends GenericConversionService{

    public DefaultConversionService(){
        addDefaultConverters(this);
    }

    // 注册String到数字的转换器
    public void addDefaultConverters(ConverterRegistry converterRegistry){
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
    }
}
