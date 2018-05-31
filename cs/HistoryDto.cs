using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class HistoryDto : IProtostuff {
	[ProtoMember(1)]
	public long Count{get;set;}
	[ProtoMember(2)]
	public bool One{get;set;}
	[ProtoMember(3)]
	public bool Two{get;set;}
	[ProtoMember(4)]
	public bool Three{get;set;}
	[ProtoMember(5)]
	public bool Four{get;set;}
}
