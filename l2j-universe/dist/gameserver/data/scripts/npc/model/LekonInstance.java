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
package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LekonInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ENERGY_STAR_STONE. (value is 13277)
	 */
	private static final int ENERGY_STAR_STONE = 13277;
	/**
	 * Field AIRSHIP_SUMMON_LICENSE. (value is 13559)
	 */
	private static final int AIRSHIP_SUMMON_LICENSE = 13559;
	
	/**
	 * Constructor for LekonInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public LekonInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.equals("get_license"))
		{
			if ((player.getClan() == null) || !player.isClanLeader() || (player.getClan().getLevel() < 5))
			{
				showChatWindow(player, 2);
				return;
			}
			if (player.getClan().isHaveAirshipLicense() || (Functions.getItemCount(player, AIRSHIP_SUMMON_LICENSE) > 0))
			{
				showChatWindow(player, 4);
				return;
			}
			if (Functions.removeItem(player, ENERGY_STAR_STONE, 10) != 10)
			{
				showChatWindow(player, 3);
				return;
			}
			Functions.addItem(player, AIRSHIP_SUMMON_LICENSE, 1);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
