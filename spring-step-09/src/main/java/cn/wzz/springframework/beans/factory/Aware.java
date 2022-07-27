package cn.wzz.springframework.beans.factory;

/**
 * Marker superinterface indicating that a bean is eligible to be
 * notified by the Spring container of a particular framework object
 * through a callback-style method.  Actual method signature is
 * determined by individual subinterfaces, but should typically
 * consist of just one void-returning method that accepts a single
 * argument.<br/>
 * 标记超接口，实现该接口的bean对象可以被Spring容器感知, 有资格被Spring容器通过回调风格的方法通知.<br/>
 * 实际的方法签名由单个子接口确定，但通常只应由一个接受单个参数的void返回方法组成.
 */
public interface Aware {
}
