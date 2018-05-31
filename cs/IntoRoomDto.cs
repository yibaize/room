using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class IntoRoomDto : IProtostuff {
	[ProtoMember(1)]
	public int RoomId{get;set;}
}
