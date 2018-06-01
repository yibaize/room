using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class RoomAddBetDto : IProtostuff {
	[ProtoMember(1)]
	public string UpdateAccount{get;set;}
	[ProtoMember(2)]
	public string NextAccount{get;set;}
	[ProtoMember(3)]
	public long Gold{get;set;}
	[ProtoMember(4)]
	public int AddBetPosition{get;set;}
	[ProtoMember(5)]
	public long ResidueGold{get;set;}
	[ProtoMember(6)]
	public long RoomAllGold{get;set;}
}
