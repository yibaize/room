using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class FirstRoomSettleDto : IProtostuff {
	[ProtoMember(1)]
	public string Account{get;set;}
	[ProtoMember(2)]
	public int CardType{get;set;}
	[ProtoMember(3)]
	public long WinPlayerGetNum{get;set;}
	[ProtoMember(4)]
	public List<int> CardIds{get;set;}
	[ProtoMember(5)]
	public List<SettleLoseModelDto> SettleModelDtos{get;set;}
}
