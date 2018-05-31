using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class CardsDto : IProtostuff {
	[ProtoMember(1)]
	public int CardType{get;set;}
	[ProtoMember(2)]
	public List<int> CardIds{get;set;}
}
