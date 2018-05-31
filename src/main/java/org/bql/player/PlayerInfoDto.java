package org.bql.player;

import org.bql.hall_connection.dto.RoomWeathDto;
import org.bql.utils.builder_clazz.ann.Protostuff;

import java.util.List;
@Protostuff
public class PlayerInfoDto {

    private int scenesId;
    private int roomId;
    private int roomPosition;

    private int id;
    private String account;
    private String username;
    private String headIcon;
    private String gender;
    private long gold;
    private long diamond;
    private long integral;
    private int vipLv;
    private String describe;
    /**联系方式*/
    private String relation;
    /**地址*/
    private String site;
    /**战绩*/
    private String exploits;
    /**当前使用座驾*/
    private int nowUserAutos;
    /**座驾*/
    private List<ResourceModel> autos;
    /**礼物*/
    private List<ResourceModel> gifts;
    /**道具*/
    private List<ResourceModel> props;
    /**当天所输 / 赢的钱*/
    private long todayGetMoney;

    public PlayerInfoDto() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public long getIntegral() {
        return integral;
    }

    public void setIntegral(long integral) {
        this.integral = integral;
    }

    public int getVipLv() {
        return vipLv;
    }

    public void setVipLv(int vipLv) {
        this.vipLv = vipLv;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getExploits() {
        return exploits;
    }

    public void setExploits(String exploits) {
        this.exploits = exploits;
    }

    public List<ResourceModel> getAutos() {
        return autos;
    }

    public void setAutos(List<ResourceModel> autos) {
        this.autos = autos;
    }

    public List<ResourceModel> getGifts() {
        return gifts;
    }

    public void setGifts(List<ResourceModel> gifts) {
        this.gifts = gifts;
    }

    public List<ResourceModel> getProps() {
        return props;
    }

    public void setProps(List<ResourceModel> props) {
        this.props = props;
    }

    public int getScenesId() {
        return scenesId;
    }

    public void setScenesId(int scenesId) {
        this.scenesId = scenesId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomPosition() {
        return roomPosition;
    }

    public void setRoomPosition(int roomPosition) {
        this.roomPosition = roomPosition;
    }

    public int getNowUserAutos() {
        return nowUserAutos;
    }

    public void setNowUserAutos(int nowUserAutos) {
        this.nowUserAutos = nowUserAutos;
    }

    public long getTodayGetMoney() {
        return todayGetMoney;
    }

    public void setTodayGetMoney(long todayGetMoney) {
        this.todayGetMoney = todayGetMoney;
    }

    /**
     * 减少金币
     * @param num
     * @return
     */
    public boolean reduceGold(long num){
        if(gold < num)
            return false;
        gold -= num;
        todayGetMoney -= num;
        return true;
    }
    public void insertGold(long num){
        gold += num;
        todayGetMoney += num;
    }
    public RoomWeathDto weathDto(int cardType,boolean isWin){
       return new RoomWeathDto(account,gold,diamond,integral,isWin,cardType);
    }
}
