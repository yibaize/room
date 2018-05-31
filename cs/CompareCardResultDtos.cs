using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class CompareCardResultDtos : IProtostuff {
	[ProtoMember(1)]
	public List<CompareCardResultDto> ResultDtos{get;set;}
}
