package cn.wzz.springframework.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 文件系统资源, 通过FileInputStream获取文件输入流
 * */
public class FileSystemResource implements Resource{

    private final String path;

    private final File file;

    // 通过文件路径获取文件对象
    public FileSystemResource(String path){
        this.path = path;
        file = new File(path);
    }

    //直接传入文件对象, 并获取文件路径
    public FileSystemResource(File file){
        this.file = file;
        path = file.getPath();//获取构造文件对象的时候传入的path
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public final String getPath(){
        return path;
    }
}
