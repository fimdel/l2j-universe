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
public class TakeBeastHandler extends Functions
{
	/**
	 * Field BEAST_WHIP.
	 */
	private final int BEAST_WHIP = 15473;
	
	/**
	 * Method show.
	 */
	public void show()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (player.getLevel() < 82)
		{
			htmltext = npc.getNpcId() + "-1.htm";
		}
		else if (Functions.getItemCount(player, BEAST_WHIP) > 0)
		{
			htmltext = npc.getNpcId() + "-2.htm";
		}
		else
		{
			Functions.addItem(player, BEAST_WHIP, 1);
			htmltext = npc.getNpcId() + "-3.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
}
