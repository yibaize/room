using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class TORoomBankerListDto : IProtostuff {
	[ProtoMember(1)]
	public List<TOBankerListDto> Bankers{get;set;}
}
