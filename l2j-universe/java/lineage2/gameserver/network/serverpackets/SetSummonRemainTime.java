package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Summon;

public class SetSummonRemainTime extends L2GameServerPacket
{
	private final int _maxFed;
	private final int _curFed;

	public SetSummonRemainTime(Summon summon)
	{
		_curFed = summon.getCurrentFed();
		_maxFed = summon.getMaxFed();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xD1);
		writeD(_maxFed);
		writeD(_curFed);
	}
}