using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class BetUpdateDto : IProtostuff {
	[ProtoMember(1)]
	public string Account{get;set;}
	[ProtoMember(2)]
	public long Gold{get;set;}
	[ProtoMember(3)]
	public long WinGld{get;set;}
	[ProtoMember(4)]
	public int Position{get;set;}
	[ProtoMember(5)]
	public int BetPlayerNum{get;set;}
}
