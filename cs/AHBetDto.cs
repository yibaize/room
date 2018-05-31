using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class AHBetDto : IProtostuff {
	[ProtoMember(1)]
	public int NowBetPlayerNum{get;set;}
	[ProtoMember(2)]
	public long AllMoney{get;set;}
}
