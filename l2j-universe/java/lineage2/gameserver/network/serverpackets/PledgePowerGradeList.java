package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.pledge.RankPrivs;

public class PledgePowerGradeList extends L2GameServerPacket
{
	private RankPrivs[] _privs;

	public PledgePowerGradeList(RankPrivs[] privs)
	{
		_privs = privs;
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x3d);
		writeD(_privs.length);
		for (RankPrivs element : _privs)
		{
			writeD(element.getRank());
			writeD(element.getParty());
		}
	}
}