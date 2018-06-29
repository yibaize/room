package org.zgl.error;


import org.zgl.utils.builder_clazz.ann.ExcelInversionToAnn;
import org.zgl.utils.builder_clazz.ann.ExcelValue;

/**
 * 作者： 白泽
 * 时间： 2017/11/8.
 * 描述：
 */
@ExcelInversionToAnn
public final class AppErrorCode{
    @ExcelValue(value = "服务器异常...")
    public final static int DATA_ERR = 404;
    @ExcelValue(value = "服务器异常...")
    public final static int SERVER_ERR = 409;
    @ExcelValue(value = "您登陆的账号是空的...")
    public final static int ACCOUNT_IS_NULL = 420;
    @ExcelValue(value = "成功交换场景")
    public final static int EXCHANGE_SCENES_SUCCEED = 700;

    @ExcelValue(value = "您的金币不足，先到商店去购买吧！^_^ 呵呵呵")
    public final static int GOLD_NOT = 900;
    @ExcelValue(value = "没有您传入的这个uid")
    public final static int NOT_UID = 901;
    @ExcelValue(value = "红包已经被抢完，等下次土豪再发红包是再抢吧!")
    public final static int RED_ENVELOPES_NOT = 902;

    @ExcelValue(value = "当前已经有位置了")
    public final static int TO_ROOM_HASH_POSITION = 1000;
    @ExcelValue(value = "金币需要大于30000才能下注哦，到商店购买吧！^_^ 嘿嘿嘿")
    public final static int GOLD_NEED_30000 = 1001;
    @ExcelValue(value = "庄家无法下注哟！")
    public final static int BANKER_CAN_NOT_BET = 1002;
    @ExcelValue(value = "位置已经被占满了，再等等吧！")
    public final static int POSITION_NOT = 1003;
    @ExcelValue(value = "位置已经有人了，再等等吧！")
    public final static int POSITION_HASH_PLAYER = 1004;
    @ExcelValue(value = "目标玩家已经比过牌并且输掉了！")
    public final static int PLAYER_IS_COMPARE = 1005;
    @ExcelValue(value = "加注失败，当前房间底注大于选择的注码！")
    public final static int BET_MIN_ERR = 1006;
    @ExcelValue(value = "已经没有剩余的牌可以交换了！")
    public final static int NOT_CARD_CAN_EXCHANGE = 1007;
    @ExcelValue(value = "您的换牌卡不足，请到商店购买先吧！")
    public final static int NOT_EXCHANGE_CARD_PROP = 1008;
    @ExcelValue(value = "这个玩家本局没在玩！")
    public final static int PLAYER_NOT_PAY = 1009;
    @ExcelValue(value = "该玩家已经被您禁比！")
    public final static int PLAYER_IS_FORBID = 1010;
    @ExcelValue(value = "您被该玩家禁比5轮比！")
    public final static int TARGET_IS_FORBID = 1011;
    @ExcelValue(value = "您的禁比卡不足！")
    public final static int NOT_FORBID_CARD = 1012;
    @ExcelValue(value = "您的喇叭卡不足,无法广播消息！")
    public final static int NOT_TRUMPET = 1013;
    @ExcelValue(value = "当前已经在上庄列表！")
    public final static int NOW_IN_BANKER_LIST = 1014;
    @ExcelValue(value = "你在万人人场下注了很多不能再在时时乐下注这么多金币，请先到商城购买或者等待万人场牌局完毕吧！")
    public final static int IN_TO_ROOM_BET_NIMIETY = 1015;
    @ExcelValue(value = "你在时时乐下注了很多不能再在万人场下注这么多金币，请先到商城购买或者等待万人场牌局完毕吧！")
    public final static int IN_AH_ROOM_BET_NIMIETY = 1016;
    @ExcelValue(value = "当前剩余人数不是2人不能说哈！")
    public final static int NOW_PLAY_PLAYER_ERR = 1017;
    @ExcelValue(value = "上一玩家全压，您只能全压或者弃牌！")
    public final static int HASH_PLAYER_BET_ALL = 1018;
    @ExcelValue(value = "想在已经开局，无法清楚下注！")
    public final static int NOW_IS_START_BETTLE = 1019;
    @ExcelValue(value = "每局只能换三次牌，这局你已经换完！")
    public final static int EXCHANGE_CARD_LIMT = 1020;
    @ExcelValue(value = "不能踢出自己下线！")
    public final static int DO_NOT_KICKING_SELF = 1021;
    @ExcelValue(value = "当前没在位置上，无法下坐！")
    public final static int PLAYER_NOT_POSITION = 1022;
    @ExcelValue(value = "踢人卡不足！")
    public final static int NOT_KICKING_CATD = 1023;
    @ExcelValue(value = "本剧投注达到上限！")
    public final static int BET_LIMIT = 1024;

    @ExcelValue(value = "超时下注了,等待下一局吧！")
    public final static int BET_TIME_OUT = 2000;
    @ExcelValue(value = "还没到您操作")
    public final static int NOT_IS_YOU_BET = 2001;
    @ExcelValue(value = "房间已经开局")
    public final static int ROOM_IS_START = 2002;
    @ExcelValue(value = "你的vip等级没有对方高，不能踢对方")
    public final static int VIP_LV_NOT_ERR = 2003;

    @ExcelValue(value = "切换房间过于频繁,两次交换不能低于5秒！")
    public final static int EXCHANGE_ROOM_OFTEN = 2004;
    @ExcelValue(value = "你已经是本剧最后一个人，已经赢了不能弃牌，坐等收钱吧！")
    public final static int NOT_GIVE_UP = 2005;
    @ExcelValue(value = "好友不在线不能赠送礼物！")
    public final static int FRIENT_NOT_ONLIEN = 2006;
    @ExcelValue(value = "不是礼物不能赠送！")
    public final static int NOT_IS_GIF = 2007;
    @ExcelValue(value = "不在同一个场不能赠送礼物！")
    public final static int NOT_IS_LIKE_SCENES = 2008;

    @ExcelValue(value = "红包已经领取！")
    public final static int RED_ENVELOPES_IS_GET = 2009;
}
