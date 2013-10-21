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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleToGracia extends Functions
{
	/**
	 * Method tele.
	 */
	public void tele()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player != null) && (npc != null))
		{
			if (player.getLevel() < 75)
			{
				show("teleporter/" + npc.getNpcId() + "-4.htm", player);
			}
			else if (player.getAdena() >= 150000)
			{
				player.reduceAdena(150000, true);
				player.teleToLocation(-149406, 255247, -80);
			}
			else
			{
				show("teleporter/" + npc.getNpcId() + "-2.htm", player);
			}
		}
	}
}
