/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package services;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GrandIsleofPrayerRace extends Functions
{
	/**
	 * Field RACE_STAMP. (value is 10013)
	 */
	private static final int RACE_STAMP = 10013;
	/**
	 * Field SECRET_KEY. (value is 9694)
	 */
	private static final int SECRET_KEY = 9694;
	
	/**
	 * Method startRace.
	 */
	public void startRace()
	{
		Skill skill = SkillTable.getInstance().getInfo(Skill.SKILL_EVENT_TIMER, 1);
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((skill == null) || (player == null) || (npc == null))
		{
			return;
		}
		getNpc().altUseSkill(skill, player);
		removeItem(player, RACE_STAMP, getItemCount(player, RACE_STAMP));
		show("default/32349-2.htm", player, npc);
	}
	
	/**
	 * Method DialogAppend_32349.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_32349(Integer val)
	{
		Player player = getSelf();
		if (player == null)
		{
			return "";
		}
		if (player.getEffectList().getEffectsBySkillId(Skill.SKILL_EVENT_TIMER) == null)
		{
			return "<br>[scripts_services.GrandIsleofPrayerRace:startRace|Start the Race.]";
		}
		long raceStampsCount = getItemCount(player, RACE_STAMP);
		if (raceStampsCount < 4)
		{
			return "<br>*Race in progress, hurry!*";
		}
		removeItem(player, RACE_STAMP, raceStampsCount);
		addItem(player, SECRET_KEY, 3);
		player.getEffectList().stopEffect(Skill.SKILL_EVENT_TIMER);
		return "<br>Good job, here is your keys.";
	}
}
