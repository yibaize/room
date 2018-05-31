using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class RoomPlayerBaseDtos : IProtostuff {
	[ProtoMember(1)]
	public List<RoomPlayerBaseDto> PlayerBaseDtos{get;set;}
}
