package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;

/**
 * @author ALF
 * @date 22.08.2012 ddddddd
 */
public class ExRegistWaitingSubstituteOk extends L2GameServerPacket
{
	private int x, y, z;
	private int classId;

	public ExRegistWaitingSubstituteOk(Party _party, Player _player)
	{
		Player leader = _party.getPartyLeader();
		x = leader.getX();
		y = leader.getY();
		z = leader.getZ();
		classId = _player.getClassId().getId();
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x105);
		writeD(x); // x
		writeD(y); // y
		writeD(z); // z
		writeD(0x00); // unk
		writeD(classId); // ClassId
		writeD(0x00); // unk1
		writeD(0x00); // unk2
	}
}
