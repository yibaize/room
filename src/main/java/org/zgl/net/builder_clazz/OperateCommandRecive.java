package org.zgl.net.builder_clazz;
import org.zgl.rooms.three_cards.three_cards_1.cmd.FirstRoom_Ready;
import org.zgl.rooms.three_cards.three_cards_1.cmd.FirstRoom_LookCard;
import org.zgl.rooms.three_cards.three_cards_1.cmd.FirstRoom_Bet;
import org.zgl.rooms.three_cards.three_cards_1.cmd.FirstRoom_Compare;
import org.zgl.rooms.thousands_of.cmd.TOBetOperation;
import org.zgl.rooms.three_cards.three_cards_1.cmd.FirstRoom_ChangeRoom;
import org.zgl.rooms.thousands_of.cmd.TOUpPositionOperation;
import org.zgl.rooms.thousands_of.cmd.TOHistoryOperation;
import org.zgl.rooms.three_cards.three_cards_1.cmd.FirstRoom_AddBet;
import org.zgl.rooms.three_cards.three_cards_1.cmd.FirstRoom_GiveUpCard;
import org.zgl.hall_connection.KickingOperation;
import org.zgl.rooms.great_pretenders.cmd.GPRoom_ExchangeCard;
import org.zgl.rooms.thousands_of.cmd.TOPlayerPlay;
import org.zgl.rooms.thousands_of.cmd.ToUpBanker;
import org.zgl.rooms.thousands_of.cmd.TOBankerList;
import org.zgl.rooms.thousands_of.cmd.TOBankerDown;
import org.zgl.rooms.always_happy.cmd.AHBet;
import org.zgl.rooms.three_cards.three_cards_1.cmd.FirstRoom_BetAll;
import org.zgl.rooms.always_happy.cmd.AHInfoRoomOperation;
import org.zgl.rooms.great_pretenders.cmd.GPRoom_ForbidCompare;
import org.zgl.rooms.dice.cmd.DiceBetOperation;
import org.zgl.rooms.dice.cmd.DiceClearBetOperation;
import org.zgl.rooms.dice.cmd.DiceHisoryOperation;
import org.zgl.rooms.dice.cmd.DicePlayPlayerOperation;
import org.zgl.rooms.dice.cmd.DiceUpPositionOperation;
import org.zgl.rooms.thousands_of.cmd.TODownPositionOperation;
import org.zgl.rooms.dice.cmd.DiceDownPositionOperation;
import org.zgl.rooms.thousands_of.cmd.TORoomJackpotOperation;
import org.zgl.redenvelope.OpenRedEnvelopesOperation;
import org.zgl.redenvelope.HandOutRedEnvelopesOperation;
import org.zgl.redenvelope.SnatchRedEnvelopesOperation;
import org.zgl.givegift.GiveGiftOperation;
import org.zgl.chat.BroadcastOparetion;
import org.zgl.chat.ChatRoomOperation;
import org.zgl.rooms.always_happy.cmd.AHStartTest;
import org.zgl.rooms.dice.cmd.DiceStartTest;
import org.zgl.rooms.always_happy.cmd.AHEndTest;
import org.zgl.rooms.dice.cmd.DiceEndTest;
import org.zgl.hall_connection.PlayerInfoOperation;
import org.zgl.hall_connection.BackHall;
import org.zgl.hall_connection.ErrorLigout;
import org.zgl.hall_connection.ShopUpdateWeathOPeration;
import org.zgl.net.server.manage.OperateCommandAbstract;public class OperateCommandRecive{
	private static OperateCommandRecive instance;
	public static OperateCommandRecive getInstance(){
		if(instance == null)
			instance = new OperateCommandRecive();
		return instance;
	}
	public OperateCommandAbstract recieve(int id,String[] params){
		switch (id){
			case 1000:
				return getFirstRoom_Ready(params);
			case 1001:
				return getFirstRoom_LookCard(params);
			case 1002:
				return getFirstRoom_Bet(params);
			case 1003:
				return getFirstRoom_Compare(params);
			case 1005:
				return getTOBetOperation(params);
			case 1006:
				return getFirstRoom_ChangeRoom(params);
			case 1007:
				return getTOUpPositionOperation(params);
			case 1008:
				return getTOHistoryOperation(params);
			case 1009:
				return getFirstRoom_AddBet(params);
			case 1010:
				return getFirstRoom_GiveUpCard(params);
			case 1011:
				return getKickingOperation(params);
			case 1012:
				return getGPRoom_ExchangeCard(params);
			case 1013:
				return getTOPlayerPlay(params);
			case 1014:
				return getToUpBanker(params);
			case 1015:
				return getTOBankerList(params);
			case 1016:
				return getTOBankerDown(params);
			case 1017:
				return getAHBet(params);
			case 1018:
				return getFirstRoom_BetAll(params);
			case 1019:
				return getAHInfoRoomOperation(params);
			case 1023:
				return getGPRoom_ForbidCompare(params);
			case 1024:
				return getDiceBetOperation(params);
			case 1025:
				return getDiceClearBetOperation(params);
			case 1026:
				return getDiceHisoryOperation(params);
			case 1027:
				return getDicePlayPlayerOperation(params);
			case 1028:
				return getDiceUpPositionOperation(params);
			case 1029:
				return getTODownPositionOperation(params);
			case 1030:
				return getDiceDownPositionOperation(params);
			case 1031:
				return getTORoomJackpotOperation(params);
			case 1032:
				return getOpenRedEnvelopesOperation(params);
			case 1033:
				return getHandOutRedEnvelopesOperation(params);
			case 1034:
				return getSnatchRedEnvelopesOperation(params);
			case 1035:
				return getGiveGiftOperation(params);
			case 5560:
				return getBroadcastOparetion(params);
			case 5599:
				return getChatRoomOperation(params);
			case 8880:
				return getAHStartTest(params);
			case 8888:
				return getDiceStartTest(params);
			case 9990:
				return getAHEndTest(params);
			case 9999:
				return getDiceEndTest(params);
			case 10000:
				return getPlayerInfoOperation(params);
			case 10002:
				return getBackHall(params);
			case 10003:
				return getErrorLigout(params);
			case 10008:
				return getShopUpdateWeathOPeration(params);
			default:
				return null;
		}
	}
	private OperateCommandAbstract getFirstRoom_Ready(String[] params){
		return new FirstRoom_Ready();
	}
	private OperateCommandAbstract getFirstRoom_LookCard(String[] params){
		return new FirstRoom_LookCard();
	}
	private OperateCommandAbstract getFirstRoom_Bet(String[] params){
		int value0 = Integer.parseInt(params[0]);
		return new FirstRoom_Bet(value0);
	}
	private OperateCommandAbstract getFirstRoom_Compare(String[] params){
		String value0 = params[0];
		return new FirstRoom_Compare(value0);
	}
	private OperateCommandAbstract getTOBetOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		int value1 = Integer.parseInt(params[1]);
		return new TOBetOperation(value0,value1);
	}
	private OperateCommandAbstract getFirstRoom_ChangeRoom(String[] params){
		return new FirstRoom_ChangeRoom();
	}
	private OperateCommandAbstract getTOUpPositionOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		return new TOUpPositionOperation(value0);
	}
	private OperateCommandAbstract getTOHistoryOperation(String[] params){
		return new TOHistoryOperation();
	}
	private OperateCommandAbstract getFirstRoom_AddBet(String[] params){
		int value0 = Integer.parseInt(params[0]);
		return new FirstRoom_AddBet(value0);
	}
	private OperateCommandAbstract getFirstRoom_GiveUpCard(String[] params){
		return new FirstRoom_GiveUpCard();
	}
	private OperateCommandAbstract getKickingOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		return new KickingOperation(value0);
	}
	private OperateCommandAbstract getGPRoom_ExchangeCard(String[] params){
		int value0 = Integer.parseInt(params[0]);
		return new GPRoom_ExchangeCard(value0);
	}
	private OperateCommandAbstract getTOPlayerPlay(String[] params){
		return new TOPlayerPlay();
	}
	private OperateCommandAbstract getToUpBanker(String[] params){
		return new ToUpBanker();
	}
	private OperateCommandAbstract getTOBankerList(String[] params){
		return new TOBankerList();
	}
	private OperateCommandAbstract getTOBankerDown(String[] params){
		return new TOBankerDown();
	}
	private OperateCommandAbstract getAHBet(String[] params){
		int value0 = Integer.parseInt(params[0]);
		int value1 = Integer.parseInt(params[1]);
		return new AHBet(value0,value1);
	}
	private OperateCommandAbstract getFirstRoom_BetAll(String[] params){
		return new FirstRoom_BetAll();
	}
	private OperateCommandAbstract getAHInfoRoomOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		return new AHInfoRoomOperation(value0);
	}
	private OperateCommandAbstract getGPRoom_ForbidCompare(String[] params){
		String value0 = params[0];
		return new GPRoom_ForbidCompare(value0);
	}
	private OperateCommandAbstract getDiceBetOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		int value1 = Integer.parseInt(params[1]);
		return new DiceBetOperation(value0,value1);
	}
	private OperateCommandAbstract getDiceClearBetOperation(String[] params){
		return new DiceClearBetOperation();
	}
	private OperateCommandAbstract getDiceHisoryOperation(String[] params){
		return new DiceHisoryOperation();
	}
	private OperateCommandAbstract getDicePlayPlayerOperation(String[] params){
		return new DicePlayPlayerOperation();
	}
	private OperateCommandAbstract getDiceUpPositionOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		return new DiceUpPositionOperation(value0);
	}
	private OperateCommandAbstract getTODownPositionOperation(String[] params){
		return new TODownPositionOperation();
	}
	private OperateCommandAbstract getDiceDownPositionOperation(String[] params){
		return new DiceDownPositionOperation();
	}
	private OperateCommandAbstract getTORoomJackpotOperation(String[] params){
		return new TORoomJackpotOperation();
	}
	private OperateCommandAbstract getOpenRedEnvelopesOperation(String[] params){
		return new OpenRedEnvelopesOperation();
	}
	private OperateCommandAbstract getHandOutRedEnvelopesOperation(String[] params){
		long value0 = Long.parseLong(params[0]);
		int value1 = Integer.parseInt(params[1]);
		String value2 = params[2];
		int value3 = Integer.parseInt(params[3]);
		return new HandOutRedEnvelopesOperation(value0,value1,value2,value3);
	}
	private OperateCommandAbstract getSnatchRedEnvelopesOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		return new SnatchRedEnvelopesOperation(value0);
	}
	private OperateCommandAbstract getGiveGiftOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		long value1 = Long.parseLong(params[1]);
		return new GiveGiftOperation(value0,value1);
	}
	private OperateCommandAbstract getBroadcastOparetion(String[] params){
		int value0 = Integer.parseInt(params[0]);
		int value1 = Integer.parseInt(params[1]);
		String value2 = params[2];
		return new BroadcastOparetion(value0,value1,value2);
	}
	private OperateCommandAbstract getChatRoomOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		String value1 = params[1];
		return new ChatRoomOperation(value0,value1);
	}
	private OperateCommandAbstract getAHStartTest(String[] params){
		return new AHStartTest();
	}
	private OperateCommandAbstract getDiceStartTest(String[] params){
		return new DiceStartTest();
	}
	private OperateCommandAbstract getAHEndTest(String[] params){
		return new AHEndTest();
	}
	private OperateCommandAbstract getDiceEndTest(String[] params){
		return new DiceEndTest();
	}
	private OperateCommandAbstract getPlayerInfoOperation(String[] params){
		int value0 = Integer.parseInt(params[0]);
		long value1 = Long.parseLong(params[1]);
		return new PlayerInfoOperation(value0,value1);
	}
	private OperateCommandAbstract getBackHall(String[] params){
		return new BackHall();
	}
	private OperateCommandAbstract getErrorLigout(String[] params){
		return new ErrorLigout();
	}
	private OperateCommandAbstract getShopUpdateWeathOPeration(String[] params){
		return new ShopUpdateWeathOPeration();
	}
}
