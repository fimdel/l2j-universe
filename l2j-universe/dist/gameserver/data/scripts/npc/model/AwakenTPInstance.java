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
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AwakenTPInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field val.
	 */
	private final int val = 1;
	
	/**
	 * Constructor for AwakenTPInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public AwakenTPInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		switch (getNpcId())
		{
			case 33563:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33563.htm", val));
				break;
			case 33562:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33562.htm", val));
				break;
			case 33564:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33564.htm", val));
				break;
			case 33565:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33565.htm", val));
				break;
			case 33560:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33560.htm", val));
				break;
			case 33561:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33561.htm", val));
				break;
			case 33566:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33566.htm", val));
				break;
			case 33567:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33567.htm", val));
				break;
			case 33568:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33568.htm", val));
				break;
			case 33569:
				player.sendPacket(new NpcHtmlMessage(player, this, "default/33569.htm", val));
				break;
		}
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
		if (!player.isAwaking())
		{
			player.sendMessage("You do not mean? Put it? REBIRTH!");
			return;
		}
		if (command.equalsIgnoreCase("Town_of_Shuttgart"))
		{
			player.reduceAdena(150000);
			player.teleToLocation(new Location(86153, -143707, -1336));
			return;
		}
		else if (command.equalsIgnoreCase("Ruins_of_Ye_Sagira"))
		{
			if (player.getLevel() >= 85)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(-116021, 236167, -3088));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("Seed_of_Annihilation"))
		{
			if (player.getLevel() >= 85)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(-178455, 154057, 2568));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("Bloody_Swampland"))
		{
			if (player.getLevel() >= 85)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(-15826, 30477, -3616));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("Ancient_City_Arcan"))
		{
			player.reduceAdena(150000);
			player.teleToLocation(new Location(207951, 84475, -1144));
			return;
		}
		else if (command.equalsIgnoreCase("Garden_of_Genesis"))
		{
			if (player.getLevel() >= 90)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(207129, 111132, -2040));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("Fayry_Settlement"))
		{
			if (player.getLevel() >= 90)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(214432, 79587, 824));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("Seal_of_Shilen"))
		{
			if (player.getLevel() >= 95)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(187383, 20498, -3584));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("Guilloutine_Fortress"))
		{
			if (player.getLevel() >= 95)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(33446, 145486, -3432));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("Parnassus"))
		{
			if (player.getLevel() >= 97)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(149358, 172479, -952));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("Orbis_Temple_Entrance"))
		{
			if (player.getLevel() >= 97)
			{
				player.reduceAdena(150000);
				player.teleToLocation(new Location(198703, 86034, -192));
				return;
			}
			doNotLvl(player);
		}
		else if (command.equalsIgnoreCase("teleport"))
		{
			switch (getNpcId())
			{
				case 33563:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33563-1.htm", val));
					break;
				case 33562:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33562-1.htm", val));
					break;
				case 33564:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33564-1.htm", val));
					break;
				case 33565:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33565-1.htm", val));
					break;
				case 33560:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33560-1.htm", val));
					break;
				case 33561:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33561-1.htm", val));
					break;
				case 33566:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33566-1.htm", val));
					break;
				case 33567:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33567-1.htm", val));
					break;
				case 33568:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33568-1.htm", val));
					break;
				case 33569:
					player.sendPacket(new NpcHtmlMessage(player, this, "default/33569-1.htm", val));
					break;
			}
		}
		else
		{
			super.onBypassFeedback(/**
			 * Method doNotLvl.
			 * @param player Player
			 */
			player, command);
		}
	}
	
	public void doNotLvl(Player player)
	{
		player.sendMessage("Вы не �?оответ�?твуете необходимому уровн�?.");
	}
}
