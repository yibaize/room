using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class TOBankerListDto : IProtostuff {
	[ProtoMember(1)]
	public string UserName{get;set;}
	[ProtoMember(2)]
	public long Gold{get;set;}
}
