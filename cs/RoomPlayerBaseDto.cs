using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class RoomPlayerBaseDto : IProtostuff {
	[ProtoMember(1)]
	public string Username{get;set;}
	[ProtoMember(2)]
	public long Gold{get;set;}
}
