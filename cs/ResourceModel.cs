using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class ResourceModel : IProtostuff {
	[ProtoMember(1)]
	public int Id{get;set;}
	[ProtoMember(2)]
	public int Type{get;set;}
	[ProtoMember(3)]
	public int Count{get;set;}
	[ProtoMember(4)]
	public long CreateTime{get;set;}
}
