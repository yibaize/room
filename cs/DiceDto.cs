using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class DiceDto : IProtostuff {
	[ProtoMember(1)]
	public int One{get;set;}
	[ProtoMember(2)]
	public int Two{get;set;}
	[ProtoMember(3)]
	public long BattleCount{get;set;}
}
