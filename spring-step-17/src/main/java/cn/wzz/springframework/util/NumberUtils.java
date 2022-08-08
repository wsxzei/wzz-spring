package cn.wzz.springframework.util;

import cn.hutool.core.lang.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {
    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        Assert.notNull(text, "Text must not be null");
        Assert.notNull(targetClass, "Target class must not be null");
        String trimmed = trimAllWhitespace(text);

        if (Byte.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed));
        }
        else if (Short.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
        }
        else if (Integer.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
        }
        else if (Long.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
        }
        else if (BigInteger.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed));
        }
        else if (Float.class == targetClass) {
            return (T) Float.valueOf(trimmed);
        }
        else if (Double.class == targetClass) {
            return (T) Double.valueOf(trimmed);
        }
        else if (BigDecimal.class == targetClass || Number.class == targetClass) {
            return (T) new BigDecimal(trimmed);
        }
        else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        }
    }

    // 修剪掉字符串str中的所有空白字符
    private static String trimAllWhitespace(String str){
        if(str == null || str.isEmpty()) return str;
        int len = str.length();
        StringBuilder builder = new StringBuilder(str.length());
        for(int i = 0; i < len; i++){
            char c = str.charAt(i);
            // 判断是否为空白字符
            if(!Character.isWhitespace(c)){
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private static boolean isHexNumber(String value){
        // 如果value是个负数, 以"-"开始, 则从1开始检查
        int startIndex = value.startsWith("-")? 1 : 0;
        return value.startsWith("0x", startIndex) || value.startsWith("0X", startIndex)
                || value.startsWith("#", startIndex);
    }

    private static BigInteger decodeBigInteger(String value){
        int radix = 10; // 默认10进制
        int index = 0; // 数字是从value的index开始
        boolean negative = false;

        if(value.startsWith("-")) {
            negative = true;
            index ++;
        }
        if(value.startsWith("0x", index) || value.startsWith("0X", index)){
            index += 2;
            radix = 16;
        }else if(value.startsWith("#", index)){
            index++;
            radix = 16;
        }else if(value.startsWith("0", index)){
            index++;
            radix = 8;
        }

        BigInteger result = new BigInteger(value.substring(index), radix);
        return negative?result.negate():result;
    }
}
