package org.zgl.error;

/**
 * 作者： 白泽
 * 时间： 2018/1/8.
 * 描述：
 */
public class CloseConnectionError extends ErrorAbs {
    public CloseConnectionError(int errorCode) {
        super(errorCode);
        throw this;
    }
}
