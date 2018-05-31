using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class TOCardsDtos : IProtostuff {
	[ProtoMember(1)]
	public List<TOCardsDto> Cards{get;set;}
}
