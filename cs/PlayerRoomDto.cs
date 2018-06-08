using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class PlayerRoomDto : IProtostuff {
	[ProtoMember(1)]
	public int RoomId{get;set;}
	[ProtoMember(2)]
	public int RoomState{get;set;}
	[ProtoMember(3)]
	public int SelfPosition{get;set;}
	[ProtoMember(4)]
	public int ExchangeCardCount{get;set;}
	[ProtoMember(5)]
	public List<PlayerRoomBaseInfoDto> Players{get;set;}
}
