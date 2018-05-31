using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class RoomPlayerBaseDto : IProtostuff {
	[ProtoMember(1)]
	public int Id{get;set;}
	[ProtoMember(2)]
	public string Username{get;set;}
	[ProtoMember(3)]
	public string Account{get;set;}
	[ProtoMember(4)]
	public long Gold{get;set;}
	[ProtoMember(5)]
	public int VipLv{get;set;}
	[ProtoMember(6)]
	public string HeadIcon{get;set;}
}
