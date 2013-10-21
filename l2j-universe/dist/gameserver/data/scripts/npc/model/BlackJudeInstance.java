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
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BlackJudeInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for BlackJudeInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BlackJudeInstance(int objectId, NpcTemplate template)
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
		if (command.equals("tryRemovePenalty"))
		{
			if (player.getDeathPenalty().getLevel(player) > 0)
			{
				showChatWindow(player, 2, "%price%", getPrice(player));
			}
			else
			{
				showChatWindow(player, 1);
			}
		}
		else if (command.equals("removePenalty"))
		{
			if (player.getDeathPenalty().getLevel(player) > 0)
			{
				if (player.getAdena() >= getPrice(player))
				{
					player.reduceAdena(getPrice(player), true);
					doCast(SkillTable.getInstance().getInfo(5077, 1), player, false);
				}
				else
				{
					player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				}
			}
			else
			{
				showChatWindow(player, 1);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getPrice.
	 * @param player Player
	 * @return int
	 */
	public int getPrice(Player player)
	{
		int playerLvl = player.getLevel();
		if (playerLvl <= 19)
		{
			return 3600;
		}
		else if ((playerLvl >= 20) && (playerLvl <= 39))
		{
			return 16400;
		}
		else if ((playerLvl >= 40) && (playerLvl <= 51))
		{
			return 36200;
		}
		else if ((playerLvl >= 52) && (playerLvl <= 60))
		{
			return 50400;
		}
		else if ((playerLvl >= 61) && (playerLvl <= 75))
		{
			return 78200;
		}
		else
		{
			return 102800;
		}
	}
}
