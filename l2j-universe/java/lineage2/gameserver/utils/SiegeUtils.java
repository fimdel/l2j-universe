package lineage2.gameserver.utils;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author VISTALL
 * @date 12:23/21.02.2011
 */
public class SiegeUtils
{
	public static void addSiegeSkills(Player character)
	{
		character.addSkill(SkillTable.getInstance().getInfo(19034, 1), false); // Печать Света
		character.addSkill(SkillTable.getInstance().getInfo(19035, 1), false); // Печать Тьмы
		character.addSkill(SkillTable.getInstance().getInfo(247, 1), false);
		if (character.isNoble())
			character.addSkill(SkillTable.getInstance().getInfo(326, 1), false);

		if (character.getClan() != null && character.getClan().getCastle() > 0)
		{
			character.addSkill(SkillTable.getInstance().getInfo(844, 1), false);
			character.addSkill(SkillTable.getInstance().getInfo(845, 1), false);
		}
	}

	public static void removeSiegeSkills(Player character)
	{
		character.removeSkill(SkillTable.getInstance().getInfo(19034, 1), false); // Печать Света
		character.removeSkill(SkillTable.getInstance().getInfo(19035, 1), false); // Печать Тьмы
		character.removeSkill(SkillTable.getInstance().getInfo(247, 1), false);
		character.removeSkill(SkillTable.getInstance().getInfo(326, 1), false);

		if (character.getClan() != null && character.getClan().getCastle() > 0)
		{
			character.removeSkill(SkillTable.getInstance().getInfo(844, 1), false);
			character.removeSkill(SkillTable.getInstance().getInfo(845, 1), false);
		}
	}

	public static boolean getCanRide()
	{
		for (Residence residence : ResidenceHolder.getInstance().getResidences())
			if (residence != null && residence.getSiegeEvent().isInProgress())
				return false;
		return true;
	}
}
