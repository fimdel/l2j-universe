package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Summon;

public class PetStatusShow extends L2GameServerPacket
{
	private int _summonType;
	private int _summonId;

	public PetStatusShow(Summon summon)
	{
		_summonType = summon.getSummonType();
		_summonId = summon.getObjectId();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xb1);
		writeD(_summonType);
		writeD(_summonId);// L2WT GOD
	}
}