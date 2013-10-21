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
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RafortyInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field FREYA_NECKLACE. (value is 16025)
	 */
	private static final int FREYA_NECKLACE = 16025;
	/**
	 * Field BLESSED_FREYA_NECKLACE. (value is 16026)
	 */
	private static final int BLESSED_FREYA_NECKLACE = 16026;
	/**
	 * Field BOTTLE_OF_FREYAS_SOUL. (value is 16027)
	 */
	private static final int BOTTLE_OF_FREYAS_SOUL = 16027;
	
	/**
	 * Constructor for RafortyInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RafortyInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("exchange_necklace_1"))
		{
			if (ItemFunctions.getItemCount(player, FREYA_NECKLACE) > 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-ex4.htm");
			}
			else
			{
				showChatWindow(player, "default/" + getNpcId() + "-ex6.htm");
			}
		}
		else if (command.equalsIgnoreCase("exchange_necklace_2"))
		{
			if (ItemFunctions.getItemCount(player, BOTTLE_OF_FREYAS_SOUL) > 0)
			{
				showChatWindow(player, "default/" + getNpcId() + "-ex8.htm");
			}
			else
			{
				showChatWindow(player, "default/" + getNpcId() + "-ex7.htm");
			}
		}
		else if (command.equalsIgnoreCase("exchange_necklace_3"))
		{
			if ((ItemFunctions.getItemCount(player, FREYA_NECKLACE) > 0) && (ItemFunctions.getItemCount(player, BOTTLE_OF_FREYAS_SOUL) > 0))
			{
				ItemFunctions.removeItem(player, FREYA_NECKLACE, 1, true);
				ItemFunctions.removeItem(player, BOTTLE_OF_FREYAS_SOUL, 1, true);
				ItemFunctions.addItem(player, BLESSED_FREYA_NECKLACE, 1, true);
				showChatWindow(player, "default/" + getNpcId() + "-ex9.htm");
			}
			else
			{
				showChatWindow(player, "default/" + getNpcId() + "-ex11.htm");
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
