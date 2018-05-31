using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class ChatDto : IProtostuff {
	[ProtoMember(1)]
	public int BroadcatType{get;set;}
	[ProtoMember(2)]
	public int MsgType{get;set;}
	[ProtoMember(3)]
	public string Account{get;set;}
	[ProtoMember(4)]
	public string Username{get;set;}
	[ProtoMember(5)]
	public int VipLv{get;set;}
	[ProtoMember(6)]
	public string Msg{get;set;}
	[ProtoMember(7)]
	public long SendTime{get;set;}
}
