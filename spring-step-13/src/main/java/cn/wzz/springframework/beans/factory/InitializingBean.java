package cn.wzz.springframework.beans.factory;

public interface InitializingBean {

    /* Bean对象属性填充applyPropertyValues后在initializeBean方法中调用 */
    void afterPropertiesSet() throws Exception;
}
