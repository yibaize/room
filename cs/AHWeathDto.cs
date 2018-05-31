using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class AHWeathDto : IProtostuff {
	[ProtoMember(1)]
	public int Result{get;set;}
	[ProtoMember(2)]
	public long Gold{get;set;}
	[ProtoMember(3)]
	public long ResultMoney{get;set;}
}
