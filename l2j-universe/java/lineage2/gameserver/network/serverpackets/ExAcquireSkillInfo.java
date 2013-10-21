package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.tables.SkillTable;

public class ExAcquireSkillInfo extends L2GameServerPacket
{
	/**
	 * Field skillLearn.
	 */
	private final SkillLearn skillLearn;

	/**
	 * Constructor for ExAcquireSkillInfo.
	 * @param player Player
	 * @param skillLearn SkillLearn
	 */
	public ExAcquireSkillInfo(Player player, SkillLearn skillLearn)
	{
		this.skillLearn = skillLearn;
	}

    @Override
    protected void writeImpl()
    {
		writeEx(0xFC);
		writeD(skillLearn.getId());
		writeD(skillLearn.getLevel());
		writeD(skillLearn.getCost());
		writeH(skillLearn.getMinLevel());
        writeH(0);  // Tauti
		writeD(skillLearn.getRequiredItems().size());
		for (int itemId : skillLearn.getRequiredItems().keySet())
		{
			writeD(itemId);
			writeQ(skillLearn.getRequiredItems().get(itemId));
		}
		Skill skkill = SkillTable.getInstance().getInfo(skillLearn.getId(), skillLearn.getLevel());
		if (skkill.isRelationSkill())
		{
			int[] _ss = skkill.getRelationSkills();
			writeD(_ss.length);
			for (int skillId : _ss)
			{
				writeD(skillId);
				writeD(SkillTable.getInstance().getBaseLevel(skillId));
			}
		}
		else
		{
			writeD(0x00);
		}
    }
}
