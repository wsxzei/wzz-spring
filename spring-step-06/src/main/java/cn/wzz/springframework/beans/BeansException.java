package cn.wzz.springframework.beans;

// 不检查异常(运行时异常)，类似 NullPointerException、ArrayIndexOutOfBoundsException 之类
// 可以编码避免的逻辑错误
public class BeansException extends RuntimeException {

    public BeansException(String msg){
        super(msg);
    }

    public BeansException(String msg, Throwable throwable){
        super(msg, throwable);
    }
}
