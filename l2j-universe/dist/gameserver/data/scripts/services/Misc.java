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
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Misc extends Functions
{
	/**
	 * Method assembleAntharasCrystal.
	 */
	public void assembleAntharasCrystal()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null) || !NpcInstance.canBypassCheck(player, player.getLastNpc()))
		{
			return;
		}
		if ((ItemFunctions.getItemCount(player, 17266) < 1) || (ItemFunctions.getItemCount(player, 17267) < 1))
		{
			show("teleporter/32864-2.htm", player);
			return;
		}
		if ((ItemFunctions.removeItem(player, 17266, 1, true) > 0) && (ItemFunctions.removeItem(player, 17267, 1, true) > 0))
		{
			ItemFunctions.addItem(player, 17268, 1, true);
			show("teleporter/32864-3.htm", player);
			return;
		}
	}
}
