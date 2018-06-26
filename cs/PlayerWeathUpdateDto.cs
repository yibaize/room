using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class PlayerWeathUpdateDto : IProtostuff {
	[ProtoMember(1)]
	public long Weath{get;set;}
	[ProtoMember(2)]
	public long Jackpot{get;set;}
}
