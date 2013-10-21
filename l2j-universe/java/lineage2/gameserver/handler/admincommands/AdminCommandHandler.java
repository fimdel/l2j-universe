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
package lineage2.gameserver.handler.admincommands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.handler.admincommands.impl.AdminAdmin;
import lineage2.gameserver.handler.admincommands.impl.AdminAnnouncements;
import lineage2.gameserver.handler.admincommands.impl.AdminBan;
import lineage2.gameserver.handler.admincommands.impl.AdminCamera;
import lineage2.gameserver.handler.admincommands.impl.AdminCancel;
import lineage2.gameserver.handler.admincommands.impl.AdminChangeAccessLevel;
import lineage2.gameserver.handler.admincommands.impl.AdminClanHall;
import lineage2.gameserver.handler.admincommands.impl.AdminCreateItem;
import lineage2.gameserver.handler.admincommands.impl.AdminCursedWeapons;
import lineage2.gameserver.handler.admincommands.impl.AdminDelete;
import lineage2.gameserver.handler.admincommands.impl.AdminDisconnect;
import lineage2.gameserver.handler.admincommands.impl.AdminDoorControl;
import lineage2.gameserver.handler.admincommands.impl.AdminEditChar;
import lineage2.gameserver.handler.admincommands.impl.AdminEffects;
import lineage2.gameserver.handler.admincommands.impl.AdminEnchant;
import lineage2.gameserver.handler.admincommands.impl.AdminEvents;
import lineage2.gameserver.handler.admincommands.impl.AdminGeodata;
import lineage2.gameserver.handler.admincommands.impl.AdminGm;
import lineage2.gameserver.handler.admincommands.impl.AdminGmChat;
import lineage2.gameserver.handler.admincommands.impl.AdminHeal;
import lineage2.gameserver.handler.admincommands.impl.AdminHellbound;
import lineage2.gameserver.handler.admincommands.impl.AdminHelpPage;
import lineage2.gameserver.handler.admincommands.impl.AdminIP;
import lineage2.gameserver.handler.admincommands.impl.AdminInstance;
import lineage2.gameserver.handler.admincommands.impl.AdminKill;
import lineage2.gameserver.handler.admincommands.impl.AdminLevel;
import lineage2.gameserver.handler.admincommands.impl.AdminMammon;
import lineage2.gameserver.handler.admincommands.impl.AdminManor;
import lineage2.gameserver.handler.admincommands.impl.AdminMenu;
import lineage2.gameserver.handler.admincommands.impl.AdminMonsterRace;
import lineage2.gameserver.handler.admincommands.impl.AdminNochannel;
import lineage2.gameserver.handler.admincommands.impl.AdminOlympiad;
import lineage2.gameserver.handler.admincommands.impl.AdminPetition;
import lineage2.gameserver.handler.admincommands.impl.AdminPledge;
import lineage2.gameserver.handler.admincommands.impl.AdminPolymorph;
import lineage2.gameserver.handler.admincommands.impl.AdminQuests;
import lineage2.gameserver.handler.admincommands.impl.AdminReload;
import lineage2.gameserver.handler.admincommands.impl.AdminRepairChar;
import lineage2.gameserver.handler.admincommands.impl.AdminRes;
import lineage2.gameserver.handler.admincommands.impl.AdminRide;
import lineage2.gameserver.handler.admincommands.impl.AdminScripts;
import lineage2.gameserver.handler.admincommands.impl.AdminServer;
import lineage2.gameserver.handler.admincommands.impl.AdminShop;
import lineage2.gameserver.handler.admincommands.impl.AdminShutdown;
import lineage2.gameserver.handler.admincommands.impl.AdminSkill;
import lineage2.gameserver.handler.admincommands.impl.AdminSpawn;
import lineage2.gameserver.handler.admincommands.impl.AdminTarget;
import lineage2.gameserver.handler.admincommands.impl.AdminTeleport;
import lineage2.gameserver.handler.admincommands.impl.AdminZone;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminCommandHandler extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final AdminCommandHandler _instance = new AdminCommandHandler();
	
	/**
	 * Method getInstance.
	 * @return AdminCommandHandler
	 */
	public static AdminCommandHandler getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _datatable.
	 */
	private final Map<String, IAdminCommandHandler> _datatable = new HashMap<String, IAdminCommandHandler>();
	
	/**
	 * Constructor for AdminCommandHandler.
	 */
	private AdminCommandHandler()
	{
		registerAdminCommandHandler(new AdminAdmin());
		registerAdminCommandHandler(new AdminAnnouncements());
		registerAdminCommandHandler(new AdminBan());
		registerAdminCommandHandler(new AdminCamera());
		registerAdminCommandHandler(new AdminCancel());
		registerAdminCommandHandler(new AdminChangeAccessLevel());
		registerAdminCommandHandler(new AdminClanHall());
		registerAdminCommandHandler(new AdminCreateItem());
		registerAdminCommandHandler(new AdminCursedWeapons());
		registerAdminCommandHandler(new AdminDelete());
		registerAdminCommandHandler(new AdminDisconnect());
		registerAdminCommandHandler(new AdminDoorControl());
		registerAdminCommandHandler(new AdminEditChar());
		registerAdminCommandHandler(new AdminEffects());
		registerAdminCommandHandler(new AdminEnchant());
		registerAdminCommandHandler(new AdminEvents());
		registerAdminCommandHandler(new AdminGeodata());
		registerAdminCommandHandler(new AdminGm());
		registerAdminCommandHandler(new AdminGmChat());
		registerAdminCommandHandler(new AdminHeal());
		registerAdminCommandHandler(new AdminHellbound());
		registerAdminCommandHandler(new AdminHelpPage());
		registerAdminCommandHandler(new AdminInstance());
		registerAdminCommandHandler(new AdminIP());
		registerAdminCommandHandler(new AdminLevel());
		registerAdminCommandHandler(new AdminMammon());
		registerAdminCommandHandler(new AdminManor());
		registerAdminCommandHandler(new AdminMenu());
		registerAdminCommandHandler(new AdminMonsterRace());
		registerAdminCommandHandler(new AdminNochannel());
		registerAdminCommandHandler(new AdminOlympiad());
		registerAdminCommandHandler(new AdminPetition());
		registerAdminCommandHandler(new AdminPledge());
		registerAdminCommandHandler(new AdminPolymorph());
		registerAdminCommandHandler(new AdminQuests());
		registerAdminCommandHandler(new AdminReload());
		registerAdminCommandHandler(new AdminRepairChar());
		registerAdminCommandHandler(new AdminRes());
		registerAdminCommandHandler(new AdminRide());
		registerAdminCommandHandler(new AdminServer());
		registerAdminCommandHandler(new AdminShop());
		registerAdminCommandHandler(new AdminShutdown());
		registerAdminCommandHandler(new AdminSkill());
		registerAdminCommandHandler(new AdminScripts());
		registerAdminCommandHandler(new AdminSpawn());
		registerAdminCommandHandler(new AdminTarget());
		registerAdminCommandHandler(new AdminTeleport());
		registerAdminCommandHandler(new AdminZone());
		registerAdminCommandHandler(new AdminKill());
	}
	
	/**
	 * Method registerAdminCommandHandler.
	 * @param handler IAdminCommandHandler
	 */
	public void registerAdminCommandHandler(IAdminCommandHandler handler)
	{
		for (Enum<?> e : handler.getAdminCommandEnum())
		{
			_datatable.put(e.toString().toLowerCase(), handler);
		}
	}
	
	/**
	 * Method getAdminCommandHandler.
	 * @param adminCommand String
	 * @return IAdminCommandHandler
	 */
	public IAdminCommandHandler getAdminCommandHandler(String adminCommand)
	{
		String command = adminCommand;
		if (adminCommand.contains(" "))
		{
			command = adminCommand.substring(0, adminCommand.indexOf(" "));
		}
		return _datatable.get(command);
	}
	
	/**
	 * Method useAdminCommandHandler.
	 * @param activeChar Player
	 * @param adminCommand String
	 */
	public void useAdminCommandHandler(Player activeChar, String adminCommand)
	{
		if (!(activeChar.isGM() || activeChar.getPlayerAccess().CanUseGMCommand))
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.SendBypassBuildCmd.NoCommandOrAccess", activeChar).addString(adminCommand));
			return;
		}
		String[] wordList = adminCommand.split(" ");
		IAdminCommandHandler handler = _datatable.get(wordList[0]);
		if (handler != null)
		{
			boolean success = false;
			try
			{
				for (Enum<?> e : handler.getAdminCommandEnum())
				{
					if (e.toString().equalsIgnoreCase(wordList[0]))
					{
						success = handler.useAdminCommand(e, wordList, adminCommand, activeChar);
						break;
					}
				}
			}
			catch (Exception e)
			{
				error("", e);
			}
			Log.LogCommand(activeChar, activeChar.getTarget(), adminCommand, success);
		}
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _datatable.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_datatable.clear();
	}
	
	/**
	 * Method getAllCommands.
	 * @return Set<String>
	 */
	public Set<String> getAllCommands()
	{
		return _datatable.keySet();
	}
}
