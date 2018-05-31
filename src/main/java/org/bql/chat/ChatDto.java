package org.bql.chat;

import org.bql.utils.DateUtils;
import org.bql.utils.builder_clazz.ann.Protostuff;

@Protostuff
public class ChatDto {
    /**广播类型 1，玩家广播 2 系统广播*/
    private int broadcatType;
    private int msgType;
    private String account;
    private String username;
    private int vipLv;
    private String msg;
    private long sendTime;
    public ChatDto() {
        sendTime = DateUtils.currentTime();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getVipLv() {
        return vipLv;
    }

    public void setVipLv(int vipLv) {
        this.vipLv = vipLv;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getBroadcatType() {
        return broadcatType;
    }

    public void setBroadcatType(int broadcatType) {
        this.broadcatType = broadcatType;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
