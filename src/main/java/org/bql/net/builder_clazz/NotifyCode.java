package org.bql.net.builder_clazz;

import org.bql.utils.builder_clazz.ann.ExcelInversionToAnn;
import org.bql.utils.builder_clazz.ann.ExcelValue;

@ExcelInversionToAnn
public class NotifyCode {
    /**初级场的主动下发协议使用1500-1999段*/
    @ExcelValue(value = "财富变更")
    public static final short ROOM_WEATH_UPDATE = 70;
    @ExcelValue(value = "房间有人准备 返回RoomReadDto对象")
    public static final short ROOM_HAS_PLAYER_READY = 1500;
    @ExcelValue(value = "房间开始战斗 FirstRoomStartDto")
    public static final short ROOM_START = 1501;
    @ExcelValue(value = "通知玩家到他下注了 返回null 之针对一个玩家")
    public static final short ROOM_PLAYER_BOTTOM = 1502;
    @ExcelValue(value = "通知所有玩家财富变更，下一玩家下注 返回RoomBetDto对象")
    public static final short ROOM_PLAYER_BOTTOM_NEXT = 1503;
    @ExcelValue(value = "通知所有玩家某个玩家输了 返回CompareCardResultDto对象")
    public static final short ROOM_LOWER_PLAYER = 1504;
    @ExcelValue(value = "通知所有玩家本局结束 下发CompareCardResultDtos对象")
    public static final short ROOM_BATTLE_END = 1505;
    @ExcelValue(value = "通知所有玩家有新玩家进来 下发PlayerRoomBaseInfoDto")
    public static final short ROOM_PLAYER_ENTER = 1506;
    @ExcelValue(value = "通知所有玩家有玩家出去 下发FirstRoomStartDto")
    public static final short ROOM_PLAYER_EXIT = 1507;
    @ExcelValue(value = "通知所有玩家有玩家看牌了 下发RoomReadDto")
    public static final short ROOM_PLAYER_LOOK_CARD = 1508;
    @ExcelValue(value = "通知所有玩家有玩家下注超时了  下发RoomPlayerAccountDto")
    public static final short ROOM_BET_TIME_OUT = 1509;
    @ExcelValue(value = "通知所有玩家有玩家下注超时了  下发RoomAddBetDto")
    public static final short ROOM_ADD_BET = 1510;
    @ExcelValue(value = "通知所有玩家有玩家弃牌  下发RoomPlayerAccountDto")
    public static final short ROOM_PLAYER_GIVE_UP_CARD = 1511;
    @ExcelValue(value = "通知所有玩家牌局结束，那哪个玩家赢了多少输了多少  下发FirstRoomSettleDto")
    public static final short ROOM_SETTLE_ACCOUNT = 1512;
    @ExcelValue(value = "通知所有玩家有玩家换牌 下发RoomPlayerAccountDto")
    public static final short ROOM_PLAYER_EXCHANGE_CARD = 1513;
    @ExcelValue(value = "下一玩家有人全压了")
    public static final short NEXT_BET_ALL = 1514;
    @ExcelValue(value = "全压")
    public static final short BET_ALL = 1515;
    @ExcelValue(value = "通知所有玩家牌局结束，那哪个玩家赢了多少输了多少  下发BetAllDto")
    public static final short FIRST_ROOM_GAME_OVER = 1516;
    @ExcelValue(value = "在玩的时候有人跑了 下发FirstRoomStartDto")
    public static final short HASH_PLAYER_EXIT = 1517;
    @ExcelValue(value = "您被人提出房间")
    public static final short KICKING_ROOM = 1518;

    /**万人场使用2000-3000*/
    @ExcelValue(value = "有人上了位置 返回一个PlayerRoomBaseInfoDto对象")
    public static final short UP_POSTION = 2000;
    @ExcelValue(value = "换庄了 返回一个PlayerRoomBaseInfoDto对象")
    public static final short EXCHANGE_BANKER = 2001;
    @ExcelValue(value = "有人进入房间 返回null客户端人数自己+1")
    public static final short ENTER_ROOM = 2002;
    @ExcelValue(value = "有人离开房间 返回RoomPlayerAccountDto客户端人数自己-1")
    public static final short EXIT_ROOM = 2003;
    @ExcelValue(value = "房间这个位置为空 返回PositionDto对象")
    public static final short POISITION_NULL = 2004;
    @ExcelValue(value = "被人踢出位置 返回RoomPlayerAccountDto 踢人的那个玩家的名字")
    public static final short POISITION_KICKING = 2005;
    @ExcelValue(value = "通知是系统庄家 返回null 前端自己显示一些数据信息")
    public static final short IS_SYSTEM_BANKER = 2006;
    @ExcelValue(value = "通知所有玩家本据发的哪些牌返回 TOCardsDtos 对象")
    public static final short NOTIFY_HAND_CARD = 2007;
    @ExcelValue(value = "通知所有玩家位置上的玩家财富变更 返回BetUpdateDto对象")
    public static final short NOTIFY_POSITION_PLAYER_WEATH_UPDATE = 2008;
    @ExcelValue(value = "通知所有玩家有人下注了多少 返回BetUpdateDto对象")
    public static final short NOTIFY_BET_WEATH_UPDATE = 2009;
    @ExcelValue(value = "通知所有玩家万人场开局了")
    public static final short TO_ROOM_START = 2010;
    @ExcelValue(value = "通知所有玩家庄家财富变更 返回PlayerWeathUpdateDto对象")
    public static final short NOTIFY_POSITION_BANKER_WEATH_UPDATE = 2011;
    @ExcelValue(value = "通知所有玩家牌局结束发送本局排行 返回TOSettleRanking对象")
    public static final short NOTIFY_END_RANKING = 2012;
    @ExcelValue(value = "有人被踢下线")
    public static final short HASK_PLAYER_KICKING = 2013;

    @ExcelValue(value = "时时乐结算通知返回AHWeathDto对象")
    public static final short AH_ROOM_SETTLE_ACCOUNT = 3000;
    @ExcelValue(value = "时时乐下注通知返回AHBetDto对象")
    public static final short AH_ROOM_BET = 3001;
    @ExcelValue(value = "时时乐开牌结果 单双 散 对 顺 下发AHResultDto对象")
    public static final short AH_ROOM_CARD_RESULT = 3002;
    @ExcelValue(value = "时时乐开局 下发 null 所有在线玩家都会收到重新计时")
    public static final short AH_ROOM_START = 3003;
    @ExcelValue(value = "时时乐本局结束  下发 null 所有在线玩家都会收到")
    public static final short AH_ROOM_END = 3004;
    @ExcelValue(value = "时时乐开牌结果这一局不能下注了等待下一局吧")
    public static final short AH_ROOM_CAN_NOT_BET = 3005;

    /**请求大厅的请求吗段10000 - 20000*/
    @ExcelValue(value = "请求大厅获取玩家信息数据")
    public static final short REQUEST_HALL_PLAYER_INFO = 10000;
    @ExcelValue(value = "通知大厅财富更新")
    public static final short REQUEST_HALL_UPDATE_WEATH = 10001;
    @ExcelValue(value = "玩家离开房间返回大厅")
    public static final short BACK_HALL = 10002;
    @ExcelValue(value = "玩家异常断开")
    public static final short ERROR_LOG_OUT = 10003;
    @ExcelValue(value = "系统庄家当天财富收入")
    public static final short SYSTEM_WEATH_UPDATE = 10004;
    @ExcelValue(value = "交换房间")
    public static final short EXCHANGE_ROOM = 10006;
    @ExcelValue(value = "新进来的玩家有座驾通知所有人 返回PositionDto对象")
    public static final short PLAYER_HASH_OUTO = 10007;
    @ExcelValue(value = "在商城中买东西之后财富变更")
    public static final short SHOP_WEATH_UPDATE = 10008;
    @ExcelValue(value = "聊天通知")
    public static final short CHAT = 5599;
    @ExcelValue(value = "广播")
    public static final short BROADCAST = 5560;
}
