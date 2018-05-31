using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class TOCardsDto : IProtostuff {
	[ProtoMember(1)]
	public int Position{get;set;}
	[ProtoMember(2)]
	public int CardType{get;set;}
	[ProtoMember(3)]
	public bool Result{get;set;}
	[ProtoMember(4)]
	public List<int> CardIds{get;set;}
}
