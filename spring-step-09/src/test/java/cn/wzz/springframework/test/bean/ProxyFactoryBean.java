package cn.wzz.springframework.test.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.ClassUtil;
import cn.wzz.springframework.beans.factory.FactoryBean;

public class ProxyFactoryBean implements FactoryBean<IUserDao> {
    @Override
    /*
     采用动态代理的方式, 生成实现IUserDao的代理对象;
     未手动实现的接口定义IUserDao最终却能被注入到UserService的userDao属性中
     */
    public IUserDao getObject() throws Exception {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("001", "wzz");
                hashMap.put("002", "wsx");

                System.out.println("IUserDao 已经被代理...\n代理方法为[" + method.getName()
                        + "]\n代理参数为[" + hashMap.get((String)args[0]) + "]");

                return  null;
            }
        };

        IUserDao result = (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                , new Class[]{IUserDao.class}, handler);

        return result;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<IUserDao> getObjectType() {
        return IUserDao.class;
    }
}
