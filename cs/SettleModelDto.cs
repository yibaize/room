using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class SettleModelDto : IProtostuff {
	[ProtoMember(1)]
	public string Account{get;set;}
	[ProtoMember(2)]
	public long Money{get;set;}
}
