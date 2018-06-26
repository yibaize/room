package org.zgl.remote;

/**
 * @作者： big
 * @创建时间： 2018/6/8
 * @文件描述：
 */
public class IoMessageJavaTpeImpl implements IoMessage {
    private String interfaceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] args;

    public String getInterfaceName() {
        return interfaceName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Object getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}