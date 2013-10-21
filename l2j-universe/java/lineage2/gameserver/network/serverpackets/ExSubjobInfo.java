package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;

import java.util.Collection;

/**
 * @author ALF
 * @author Darvin
 * @data 09.02.2012
 */

public class ExSubjobInfo extends L2GameServerPacket
{
	private Collection<SubClass> _subClasses;
	private int _raceId, _classId;
	private boolean _openStatus;

	public ExSubjobInfo(Player player, boolean openStatus)
	{
		_openStatus = openStatus;
		_raceId = player.getRace().ordinal();
		_classId = player.getClassId().ordinal();
		_subClasses = player.getSubClassList().values();
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xEA);
		writeC(_openStatus);
		writeD(_classId);
		writeD(_raceId);
		writeD(_subClasses.size());
		for (SubClass subClass : _subClasses)
		{
			writeD(subClass.getIndex());
			writeD(subClass.getClassId());
			writeD(subClass.getLevel());
			writeC(subClass.getType().ordinal());
		}
	}
}
