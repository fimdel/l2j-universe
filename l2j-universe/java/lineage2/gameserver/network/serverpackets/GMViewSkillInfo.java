package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.tables.SkillTable;

import java.util.Collection;

public class GMViewSkillInfo extends L2GameServerPacket
{
	private String _charName;
	private Collection<Skill> _skills;
	private Player _targetChar;

	public GMViewSkillInfo(Player cha)
	{
		_charName = cha.getName();
		_skills = cha.getAllSkills();
		_targetChar = cha;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x97);
		writeS(_charName);
		writeD(_skills.size());
		for (Skill skill : _skills)
		{
			writeD(skill.isPassive() ? 1 : 0);
			writeD(skill.getDisplayLevel());
			writeD(skill.getId());
			writeC(_targetChar.isUnActiveSkill(skill.getId()) ? 0x01 : 0x00);
			writeC(SkillTable.getInstance().getMaxLevel(skill.getId()) > 100 ? 1 : 0);
		}
	}
}