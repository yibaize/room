package org.zgl.redenvelope;

import org.zgl.utils.builder_clazz.ann.Protostuff;

import java.sql.Date;

/**
 * @作者： big
 * @创建时间： 2018/6/23
 * @文件描述：
 */
@Protostuff
public class DBRedEvenlopes {
    private Integer id;
    private Long uid;
    private String headIcon;
    private String userName;
    private Integer vipLv;
    private Integer redEvenlopesType;
    private Long targetUid;
    private Integer hasGet;
    private String givePlayer;
    private Integer num;
    private Integer numed;
    private String explain;
    private Integer residueGold;
    private Date createTime;

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public Integer getResidueGold() {
        return residueGold;
    }

    public void setResidueGold(Integer residueGold) {
        this.residueGold = residueGold;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getVipLv() {
        return vipLv;
    }

    public void setVipLv(Integer vipLv) {
        this.vipLv = vipLv;
    }

    public Integer getRedEvenlopesType() {
        return redEvenlopesType;
    }

    public void setRedEvenlopesType(Integer redEvenlopesType) {
        this.redEvenlopesType = redEvenlopesType;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public Integer getHasGet() {
        return hasGet;
    }

    public void setHasGet(Integer hasGet) {
        this.hasGet = hasGet;
    }

    public String getGivePlayer() {
        return givePlayer;
    }

    public void setGivePlayer(String givePlayer) {
        this.givePlayer = givePlayer;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getNumed() {
        return numed;
    }

    public void setNumed(Integer numed) {
        this.numed = numed;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
