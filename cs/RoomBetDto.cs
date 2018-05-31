using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class RoomBetDto : IProtostuff {
	[ProtoMember(1)]
	public string UpdateAccount{get;set;}
	[ProtoMember(2)]
	public string NextAccount{get;set;}
	[ProtoMember(3)]
	public long Gold{get;set;}
}
