package org.bql.player;

public class LoginDto {
    private int id;
    private long gold;
    private long diamond;
    private long integral;
    private String userName;
    private String password;
    private String account;
    private int loginType;
    private int vip;
    private long vipExp;
    private String headIcon;
    /**成长礼包*/
    private int giftBagDay;
    /**当天是否已经领取*/
    private boolean hasGiftBag;
    /**签到天数*/
    private int signInDay;
    /**今天是否签到*/
    private boolean hasSignIn;
    /**距离下次在线奖励可以领取的时间*/
    private int onlineAwardTime;
    private int onlineAwardCount;//这次是第几次领取
    /**是否已经购买摇钱树 等级大于0已经购买*/
    private int moneyTreeLv;
    /**当前使用座驾*/
    private int auto;
    /**礼物值*/
    private long giftNum;
    /**人品值*/
    private long characterNum;
    public LoginDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public int getGiftBagDay() {
        return giftBagDay;
    }

    public void setGiftBagDay(int giftBagDay) {
        this.giftBagDay = giftBagDay;
    }

    public boolean isHasGiftBag() {
        return hasGiftBag;
    }

    public void setHasGiftBag(boolean hasGiftBag) {
        this.hasGiftBag = hasGiftBag;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getDiamond() {
        return diamond;
    }

    public void setDiamond(long diamond) {
        this.diamond = diamond;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getMoneyTreeLv() {
        return moneyTreeLv;
    }

    public void setMoneyTreeLv(int moneyTreeLv) {
        this.moneyTreeLv = moneyTreeLv;
    }

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public long getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(long giftNum) {
        this.giftNum = giftNum;
    }

    public long getCharacterNum() {
        return characterNum;
    }

    public void setCharacterNum(long characterNum) {
        this.characterNum = characterNum;
    }

    public boolean isHasSignIn() {
        return hasSignIn;
    }

    public void setHasSignIn(boolean hasSignIn) {
        this.hasSignIn = hasSignIn;
    }

    public int getSignInDay() {
        return signInDay;
    }

    public void setSignInDay(int signInDay) {
        this.signInDay = signInDay;
    }

    public long getIntegral() {
        return integral;
    }

    public void setIntegral(long integral) {
        this.integral = integral;
    }

    public long getVipExp() {
        return vipExp;
    }

    public void setVipExp(long vipExp) {
        this.vipExp = vipExp;
    }

    public int getOnlineAwardCount() {
        return onlineAwardCount;
    }

    public void setOnlineAwardCount(int onlineAwardCount) {
        this.onlineAwardCount = onlineAwardCount;
    }

    public int getOnlineAwardTime() {
        return onlineAwardTime;
    }

    public void setOnlineAwardTime(int onlineAwardTime) {
        this.onlineAwardTime = onlineAwardTime;
    }


}
