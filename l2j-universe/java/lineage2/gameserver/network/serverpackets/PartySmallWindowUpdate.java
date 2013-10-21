package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.party.PartySubstitute;

public class PartySmallWindowUpdate extends L2GameServerPacket
{
	private int obj_id, class_id, level;
	private int curCp, maxCp, curHp, maxHp, curMp, maxMp, vitality;
	private String obj_name;
	private int replace;

	public PartySmallWindowUpdate(Player member)
	{
		obj_id = member.getObjectId();
		obj_name = member.getName();
		curCp = (int) member.getCurrentCp();
		maxCp = member.getMaxCp();
		curHp = (int) member.getCurrentHp();
		maxHp = member.getMaxHp();
		curMp = (int) member.getCurrentMp();
		maxMp = member.getMaxMp();
		level = member.getLevel();
		class_id = member.getClassId().getId();
		vitality = member.getVitality();
		replace = PartySubstitute.getInstance().isPlayerToReplace(member) ? 1 : 0;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x52);
		// dSdddddddd
		writeD(obj_id);
		writeS(obj_name);
		writeD(curCp);
		writeD(maxCp);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
		writeD(level);
		writeD(class_id);
		writeD(vitality);
		writeD(replace);
	}
}