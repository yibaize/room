using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class PlayerRoomBaseInfoDto : IProtostuff {
	[ProtoMember(1)]
	public string Account{get;set;}
	[ProtoMember(2)]
	public long Uid{get;set;}
	[ProtoMember(3)]
	public string UserName{get;set;}
	[ProtoMember(4)]
	public long Gold{get;set;}
	[ProtoMember(5)]
	public string HeadIcon{get;set;}
	[ProtoMember(6)]
	public int VipLv{get;set;}
	[ProtoMember(7)]
	public long BottomNum{get;set;}
	[ProtoMember(8)]
	public bool HasReady{get;set;}
	[ProtoMember(9)]
	public int Postion{get;set;}
	[ProtoMember(10)]
	public int AutoId{get;set;}
	[ProtoMember(11)]
	public string Gender{get;set;}
}
