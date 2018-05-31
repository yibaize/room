using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class AHHistoryDto : IProtostuff {
	[ProtoMember(1)]
	public long Num{get;set;}
	[ProtoMember(2)]
	public int Result{get;set;}
	[ProtoMember(3)]
	public int OddEnven{get;set;}
	[ProtoMember(4)]
	public List<int> CardIds{get;set;}
}
