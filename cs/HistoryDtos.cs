using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class HistoryDtos : IProtostuff {
	[ProtoMember(1)]
	public List<HistoryDto> History{get;set;}
}
