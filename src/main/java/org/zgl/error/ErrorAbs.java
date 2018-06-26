package org.zgl.error;

/**
 * 作者： 白泽
 * 时间： 2017/12/1.
 * 描述：
 */
public abstract class ErrorAbs extends RuntimeException {
    private static final long serialVersionUID = 4143519479094905222L;
    /**
     * 错误代码
     */
    private final int errorCode;
    private final String msg;

    public String getMsg() {
        return msg;
    }

    public int getErrorCode() {
        return errorCode;
    }
    public ErrorAbs(String msg) {
        this.errorCode = 0;
        this.msg = msg;
    }

    public ErrorAbs(int errorCode){
        this.errorCode = errorCode;

        msg = "";
    }
}
