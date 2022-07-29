package cn.wzz.springframework.core.io;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ClassUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * ClassPath下的资源读取, 通过classLoader的getResourceAsStream获取
 * */
public class ClassPathResource implements Resource{

    private final String path;

    private final ClassLoader classLoader;

    public ClassPathResource(String path){
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader){
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        // ClassUtil工具类的getClassLoader会获取上下文类加载器
        this.classLoader = classLoader != null ? classLoader
                    : ClassUtil.getClassLoader();
//        System.out.println(this.classLoader.getResource(""));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        // classLoader获取资源的当前路径为classes文件夹, 后面填写相对路径(不以'/'开头)
        InputStream is = classLoader.getResourceAsStream(path);
        if(is == null){
            throw new FileNotFoundException(this.path + "cannot be opened because it does not exist");
        }
        return is;
    }
}
