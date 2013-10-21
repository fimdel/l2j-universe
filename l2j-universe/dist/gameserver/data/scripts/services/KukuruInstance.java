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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KukuruInstance extends Functions
{
	/**
	 * Method getgokukuru.
	 */
	public void getgokukuru()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		gokukuru(npc, player, false);
	}
	
	/**
	 * Method gokukuru.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param servitor boolean
	 */
	public static void gokukuru(NpcInstance npc, Player player, boolean servitor)
	{
		List<Creature> target = new ArrayList<>();
		target.add(player);
		if (player.isCursedWeaponEquipped())
		{
			return;
		}
		npc.broadcastPacket(new MagicSkillUse(npc, player, 9209, 1, 0, 0));
		npc.callSkill(SkillTable.getInstance().getInfo(9209, 1), target, true);
	}
}
