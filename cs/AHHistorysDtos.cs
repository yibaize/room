using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class AHHistorysDtos : IProtostuff {
	[ProtoMember(1)]
	public int PlayerNum{get;set;}
	[ProtoMember(2)]
	public long LastTimeGrantAward{get;set;}
	[ProtoMember(3)]
	public long NowBetMoney{get;set;}
	[ProtoMember(4)]
	public int RoomTime{get;set;}
	[ProtoMember(5)]
	public List<AHHistoryDto> HistoryDtos{get;set;}
}
