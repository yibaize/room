package org.zgl.remote;

import org.zgl.net.http.HttpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @作者： big
 * @创建时间： 2018/6/8
 * @文件描述：下发通知（远程调用客户端接口） 也算回调
 */
public class HttpProxyOutboundHandler {
    @SuppressWarnings("unchecked")
    public static <T> T getRemoteProxyObj(final Class serviceInterFace){
        return (T) Proxy.newProxyInstance(serviceInterFace.getClassLoader(), new Class<?>[]{serviceInterFace}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                IoMessageJavaTpeImpl ioMessage = new IoMessageJavaTpeImpl();
                ioMessage.setInterfaceName(serviceInterFace.getName());
                ioMessage.setMethodName(method.getName());
                ioMessage.setParamTypes(method.getParameterTypes());
                ioMessage.setArgs(args);
                Class<?> returnType = method.getReturnType();
                if(!returnType.equals(void.class)) {
                    return HttpClient.getInstance().syncPost(ioMessage, returnType);
                }else {
                    HttpClient.getInstance().asyncPost(ioMessage);
                }
                return null;
            }
        });
    }
}