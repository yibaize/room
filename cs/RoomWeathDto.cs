using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class RoomWeathDto : IProtostuff {
	[ProtoMember(1)]
	public string Account{get;set;}
	[ProtoMember(2)]
	public long Gold{get;set;}
	[ProtoMember(3)]
	public long Diamond{get;set;}
	[ProtoMember(4)]
	public long Integral{get;set;}
	[ProtoMember(5)]
	public bool HasWin{get;set;}
	[ProtoMember(6)]
	public int WinCardType{get;set;}
}
