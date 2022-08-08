package cn.wzz.springframework.beans;

import java.util.ArrayList;
import java.util.List;

// bean的属性集合, 实例化后填充属性信息
public class PropertyValues {

    private List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue propertyValue){
        propertyValueList.add(propertyValue);
    }

    // 传递数组作为入参, 数组构成元素的类型会作为toArray返回值的范型
    // 如果传入的数组length能填充ArrayList中的元素, 则将ArrayList中的元素填入该数组然后返回
    public PropertyValue[] getPropertyValues(){
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName){
        for(PropertyValue pv: propertyValueList){
            // 注意字符串不用==判断相等!
            if(pv.getName().equals(propertyName)){
                return pv;
            }
        }
        return null;
    }

    public void removePropertyValue(String propertyName){
        for(PropertyValue pv: propertyValueList){
            // 注意字符串不用==判断相等!
            if(pv.getName().equals(propertyName)){
               propertyValueList.remove(pv);
               break;
            }
        }
    }

}
