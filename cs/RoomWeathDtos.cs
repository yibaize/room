using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class RoomWeathDtos : IProtostuff {
	[ProtoMember(1)]
	public List<PlayerInfoDto> WeathDtos{get;set;}
}
