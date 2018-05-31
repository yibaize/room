using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class TORoomInfoDto : IProtostuff {
	[ProtoMember(1)]
	public PlayerRoomBaseInfoDto Banker{get;set;}
	[ProtoMember(2)]
	public int RoomNum{get;set;}
	[ProtoMember(3)]
	public int RoomState{get;set;}
	[ProtoMember(4)]
	public int RoomTimer{get;set;}
	[ProtoMember(5)]
	public int SelfPosition{get;set;}
	[ProtoMember(6)]
	public List<PlayerRoomBaseInfoDto> PositionInfo{get;set;}
}
