using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class FirstRoomStartDto : IProtostuff {
	[ProtoMember(1)]
	public string Account{get;set;}
}
