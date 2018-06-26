package org.zgl.error;

import org.zgl.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class ErrorCodeDto {
    private short errCode;

    public ErrorCodeDto(short errCode) {
        this.errCode = errCode;
    }

    public ErrorCodeDto() {
    }

    public short getErrCode() {
        return errCode;
    }

    public void setErrCode(short errCode) {
        this.errCode = errCode;
    }
}