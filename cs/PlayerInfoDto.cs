using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class PlayerInfoDto : IProtostuff {
	[ProtoMember(1)]
	public int ScenesId{get;set;}
	[ProtoMember(2)]
	public int RoomId{get;set;}
	[ProtoMember(3)]
	public int RoomPosition{get;set;}
	[ProtoMember(4)]
	public int Id{get;set;}
	[ProtoMember(5)]
	public string Account{get;set;}
	[ProtoMember(6)]
	public string Username{get;set;}
	[ProtoMember(7)]
	public string HeadIcon{get;set;}
	[ProtoMember(8)]
	public string Gender{get;set;}
	[ProtoMember(9)]
	public long Gold{get;set;}
	[ProtoMember(10)]
	public long Diamond{get;set;}
	[ProtoMember(11)]
	public long Integral{get;set;}
	[ProtoMember(12)]
	public int VipLv{get;set;}
	[ProtoMember(13)]
	public string Describe{get;set;}
	[ProtoMember(14)]
	public string Relation{get;set;}
	[ProtoMember(15)]
	public string Site{get;set;}
	[ProtoMember(16)]
	public string Exploits{get;set;}
	[ProtoMember(17)]
	public int NowUserAutos{get;set;}
	[ProtoMember(18)]
	public List<ResourceModel> Autos{get;set;}
	[ProtoMember(19)]
	public List<ResourceModel> Gifts{get;set;}
	[ProtoMember(20)]
	public List<ResourceModel> Props{get;set;}
	[ProtoMember(21)]
	public long TodayGetMoney{get;set;}
}
