using System;
using System.Collections.Generic;
using ProtoBuf;
[ProtoContract]
public class TOSettleRanking : IProtostuff {
	[ProtoMember(1)]
	public List<BetUpdateDto> Ranking{get;set;}
}
