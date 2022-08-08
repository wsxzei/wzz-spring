package cn.wzz.springframework.test;


import cn.wzz.springframework.context.support.ClassPathXmlApplicationContext;
import cn.wzz.springframework.core.convert.converter.Converter;
import cn.wzz.springframework.core.convert.support.StringToNumberConverterFactory;
import cn.wzz.springframework.test.bean.Husband;
import org.junit.Test;

/** 17 版本
 * test_StringToNumberConverter:
 * 测试StringToNumberConverterFactory将字符串转为各种整数和浮点数的能力
 * test_convert:
 * 测试自定义StringToLocalDateConverter对bean对象属性的类型转换
 */

public class ApiTest {
    @Test
    public void test_StringToNumberConverter(){
        StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();

        Converter<String, Integer> integerConverter = converterFactory.getConverter(Integer.class);
        Integer testInteger = integerConverter.convert("123");
        System.out.println("integerValue: " + testInteger);

        Converter<String, Long> longConverter = converterFactory.getConverter(Long.class);
        Long testLong = longConverter.convert("0x 400");
        System.out.println("longValue: " + testLong);

        Converter<String, Double> doubleConverter = converterFactory.getConverter(Double.class);
        Double testDouble = doubleConverter.convert("123.233");
        System.out.println("doubleValue: " + testDouble);

        Converter<String, Float> floatConverter = converterFactory.getConverter(Float.class);
        Float testFloat = floatConverter.convert("123.233");
        System.out.println("floatValue: " +  testFloat);
    }
/*
integerValue: 123
longValue: 1024
doubleValue: 123.233
floatValue: 123.233
 */

    @Test
    public void test_convert(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = (Husband)context.getBean("husband");
        System.out.println(husband);
    }
/*
Age: 28; marriageDate: 2022-08-08
 */
}
