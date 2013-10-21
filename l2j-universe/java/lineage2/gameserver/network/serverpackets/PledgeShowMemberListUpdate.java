package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;

public class PledgeShowMemberListUpdate extends L2GameServerPacket
{
	private String _name;
	private int _lvl;
	private int _classId;
	private int _sex;
	private int _race;
	private boolean _isOnline;
	private int _objectId;
	private int _pledgeType;
	private int _isApprentice;

	public PledgeShowMemberListUpdate(final Player player)
	{
		_name = player.getName();
		_lvl = player.getLevel();
		_classId = player.getClassId().getId();
		_sex = player.getSex();
		_race = player.getRace().ordinal();
		_objectId = player.getObjectId();
		_isOnline = player.isOnline();
		_pledgeType = player.getPledgeType();
		SubUnit subUnit = player.getSubUnit();
		UnitMember member = subUnit == null ? null : subUnit.getUnitMember(_objectId);
		if (member != null)
			_isApprentice = member.hasSponsor() ? 1 : 0;
	}

	public PledgeShowMemberListUpdate(final UnitMember cm)
	{
		_name = cm.getName();
		_lvl = cm.getLevel();
		_classId = cm.getClassId();
		_sex = cm.getSex();
		_race = cm.getRace();
		_objectId = cm.getObjectId();
		_isOnline = cm.isOnline();
		_pledgeType = cm.getPledgeType();
		_isApprentice = cm.hasSponsor() ? 1 : 0;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x5b);
		writeS(_name);
		writeD(_lvl);
		writeD(_classId);
		writeD(_sex);
		writeD(_race);
		if (_isOnline)
		{
			writeD(_objectId);
			writeD(_pledgeType);
		}
		else
		{
			writeD(0);
			writeD(0);
		}
		writeD(_isApprentice); // does a clan member have a sponsor
	}
}