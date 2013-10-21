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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ObtainTalisman extends Functions
{
	/**
	 * Method Obtain.
	 */
	public void Obtain()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		if (!player.isQuestContinuationPossible(false))
		{
			player.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
			return;
		}
		if (getItemCount(player, 9912) < 10)
		{
			show("scripts/services/ObtainTalisman-no.htm", player, npc);
			return;
		}
		final List<Integer> talismans = new ArrayList<>();
		for (int i = 9914; i <= 9965; i++)
		{
			if (i != 9923)
			{
				talismans.add(i);
			}
		}
		for (int i = 10416; i <= 10424; i++)
		{
			talismans.add(i);
		}
		for (int i = 10518; i <= 10519; i++)
		{
			talismans.add(i);
		}
		for (int i = 10533; i <= 10543; i++)
		{
			talismans.add(i);
		}
		removeItem(player, 9912, 10);
		addItem(player, talismans.get(Rnd.get(talismans.size())), 1);
		show("scripts/services/ObtainTalisman.htm", player, npc);
	}
}
