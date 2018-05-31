using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class GPRoomExchangeCardDto : IProtostuff {
	[ProtoMember(1)]
	public string Account{get;set;}
	[ProtoMember(2)]
	public int CardId{get;set;}
}
