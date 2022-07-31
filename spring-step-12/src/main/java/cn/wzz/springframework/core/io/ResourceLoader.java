package cn.wzz.springframework.core.io;

//功能: 识别资源类型, 并构造对应的Resource对象
public interface ResourceLoader {

    // location中标识classpath资源的前缀
    String CLASSPATH_URL_PREFIX = "classpath:";

    // 通过location, 构造Resource对象
    Resource getResource(String location);
}
