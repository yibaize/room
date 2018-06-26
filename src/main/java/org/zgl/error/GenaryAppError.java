package org.zgl.error;

/**
 * 作者： 白泽
 * 时间： 2017/12/1.
 * 描述：
 */
public class GenaryAppError extends ErrorAbs {
    public GenaryAppError(int errorCode) {
        super(errorCode);
        throw this;
    }
}
