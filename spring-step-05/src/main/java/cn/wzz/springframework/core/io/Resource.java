package cn.wzz.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

// 获取资源的输入流 getInputStream
public interface Resource {
    InputStream getInputStream() throws IOException;
}
