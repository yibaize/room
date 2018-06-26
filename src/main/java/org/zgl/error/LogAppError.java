package org.zgl.error;

/**
 * 作者： 白泽
 * 时间： 2017/12/1.
 * 描述：
 */
public class LogAppError extends ErrorAbs {
    public LogAppError(int errorCode) {
        super(errorCode);
        throw this;
    }
    public LogAppError(String msg) {
        super(msg);
        throw this;
    }
}
