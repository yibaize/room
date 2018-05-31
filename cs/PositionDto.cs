using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class PositionDto : IProtostuff {
	[ProtoMember(1)]
	public int Position{get;set;}
}
