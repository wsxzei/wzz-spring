package cn.wzz.springframework.beans.factory;

public interface DisposableBean {
    /**
     * Invoked by the containing on destruction of a bean.
     * @throws Exception in case of shutdown errors. Exceptions will get logged
     * but not rethrown to allow other beans to release their resources as well.
     * 在bean销毁时有容器调用该方法
     */
    void destroy() throws Exception;
}
