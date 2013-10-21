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
package npc.model.residences.clanhall;

import java.util.List;
import java.util.StringTokenizer;

import lineage2.commons.collections.CollectionUtils;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.dao.SiegePlayerDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.ClanHallTeamBattleEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.CTBSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.TimeUtils;

import org.apache.commons.lang3.StringUtils;

import quests._655_AGrandPlanForTamingWildBeasts;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FarmMessengerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for FarmMessengerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public FarmMessengerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(final Player player, final String command)
	{
		ClanHall clanHall = getClanHall();
		ClanHallTeamBattleEvent siegeEvent = clanHall.getSiegeEvent();
		Clan clan = player.getClan();
		if (command.equalsIgnoreCase("registrationMenu"))
		{
			if (!checkCond(player, true))
			{
				return;
			}
			showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_1.htm");
		}
		else if (command.equalsIgnoreCase("registerAsClan"))
		{
			if (!checkCond(player, false))
			{
				return;
			}
			List<CTBSiegeClanObject> siegeClans = siegeEvent.getObjects(SiegeEvent.ATTACKERS);
			CTBSiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, clan);
			if (siegeClan != null)
			{
				showFlagInfo(player, siegeClans.indexOf(siegeClan));
				return;
			}
			QuestState questState = player.getQuestState(_655_AGrandPlanForTamingWildBeasts.class);
			if ((questState == null) || (questState.getQuestItemsCount(8293) != 1))
			{
				showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_27.htm");
				return;
			}
			questState.exitCurrentQuest(true);
			register(player);
		}
		else if (command.equalsIgnoreCase("registerAsMember"))
		{
			CTBSiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
			if (siegeClan == null)
			{
				showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_7.htm");
				return;
			}
			if (siegeClan.getClan().getLeaderId() == player.getObjectId())
			{
				showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_5.htm");
				return;
			}
			if (siegeClan.getPlayers().contains(player.getObjectId()))
			{
				showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_9.htm");
			}
			else
			{
				if (siegeClan.getPlayers().size() >= 18)
				{
					showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_8.htm");
					return;
				}
				siegeClan.getPlayers().add(player.getObjectId());
				SiegePlayerDAO.getInstance().insert(clanHall, clan.getClanId(), player.getObjectId());
				showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_9.htm");
			}
		}
		else if (command.startsWith("formAlliance"))
		{
			CTBSiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
			if (siegeClan == null)
			{
				showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_7.htm");
				return;
			}
			if (siegeClan.getClan().getLeaderId() != player.getObjectId())
			{
				showChatWindow(player, "residence2/clanhall/agit_oel_mahum_messeger_10.htm");
				return;
			}
			if (siegeClan.getParam() > 0)
			{
				return;
			}
			StringTokenizer t = new StringTokenizer(command);
			t.nextToken();
			int npcId = Integer.parseInt(t.nextToken());
			siegeClan.setParam(npcId);
			SiegeClanDAO.getInstance().update(clanHall, siegeClan);
			showChatWindow(player, "residence2/clanhall/agit_oel_mahum_messeger_9.htm");
		}
		else if (command.equalsIgnoreCase("setNpc"))
		{
			CTBSiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
			if (siegeClan == null)
			{
				showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_7.htm");
				return;
			}
			if (siegeClan.getClan().getLeaderId() != player.getObjectId())
			{
				showChatWindow(player, "residence2/clanhall/agit_oel_mahum_messeger_10.htm");
				return;
			}
			showChatWindow(player, npcDialog(siegeClan));
		}
		else if (command.equalsIgnoreCase("viewNpc"))
		{
			CTBSiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
			if (siegeClan == null)
			{
				showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_7.htm");
				return;
			}
			String file;
			if (siegeClan.getParam() == 0)
			{
				file = "residence2/clanhall/agit_oel_mahum_messeger_10.htm";
			}
			else
			{
				file = npcDialog(siegeClan);
			}
			showChatWindow(player, file);
		}
		else if (command.equalsIgnoreCase("listClans"))
		{
			NpcHtmlMessage msg = new NpcHtmlMessage(player, this);
			msg.setFile("residence2/clanhall/farm_messenger003.htm");
			List<CTBSiegeClanObject> siegeClans = siegeEvent.getObjects(SiegeEvent.ATTACKERS);
			for (int i = 0; i < 5; i++)
			{
				CTBSiegeClanObject siegeClan = CollectionUtils.safeGet(siegeClans, i);
				if (siegeClan != null)
				{
					msg.replace("%clan_" + i + "%", siegeClan.getClan().getName());
				}
				else
				{
					msg.replaceNpcString("%clan_" + i + "%", NpcString.__UNREGISTERED__);
				}
				msg.replace("%clan_count_" + i + "%", siegeClan == null ? StringUtils.EMPTY : String.valueOf(siegeClan.getPlayers().size()));
			}
			player.sendPacket(msg);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method register.
	 * @param player Player
	 */
	private void register(Player player)
	{
		Clan clan = player.getClan();
		ClanHall clanHall = getClanHall();
		ClanHallTeamBattleEvent siegeEvent = clanHall.getSiegeEvent();
		CTBSiegeClanObject siegeClan = new CTBSiegeClanObject(SiegeEvent.ATTACKERS, clan, 0);
		siegeClan.getPlayers().add(player.getObjectId());
		siegeEvent.addObject(SiegeEvent.ATTACKERS, siegeClan);
		SiegeClanDAO.getInstance().insert(clanHall, siegeClan);
		SiegePlayerDAO.getInstance().insert(clanHall, clan.getClanId(), player.getObjectId());
		List<CTBSiegeClanObject> siegeClans = siegeEvent.getObjects(SiegeEvent.ATTACKERS);
		showFlagInfo(player, siegeClans.indexOf(siegeClan));
	}
	
	/**
	 * Method showFlagInfo.
	 * @param player Player
	 * @param index int
	 */
	private void showFlagInfo(Player player, int index)
	{
		String file = null;
		switch (index)
		{
			case 0:
				file = "residence2/clanhall/farm_kel_mahum_messenger_4a.htm";
				break;
			case 1:
				file = "residence2/clanhall/farm_kel_mahum_messenger_4b.htm";
				break;
			case 2:
				file = "residence2/clanhall/farm_kel_mahum_messenger_4c.htm";
				break;
			case 3:
				file = "residence2/clanhall/farm_kel_mahum_messenger_4d.htm";
				break;
			case 4:
				file = "residence2/clanhall/farm_kel_mahum_messenger_4e.htm";
				break;
			default:
				return;
		}
		showChatWindow(player, file);
	}
	
	/**
	 * Method npcDialog.
	 * @param siegeClanObject SiegeClanObject
	 * @return String
	 */
	private String npcDialog(SiegeClanObject siegeClanObject)
	{
		String file = null;
		switch ((int) siegeClanObject.getParam())
		{
			case 0:
				file = "residence2/clanhall/farm_kel_mahum_messenger_6.htm";
				break;
			case 35618:
				file = "residence2/clanhall/farm_kel_mahum_messenger_17.htm";
				break;
			case 35619:
				file = "residence2/clanhall/farm_kel_mahum_messenger_18.htm";
				break;
			case 35620:
				file = "residence2/clanhall/farm_kel_mahum_messenger_19.htm";
				break;
			case 35621:
				file = "residence2/clanhall/farm_kel_mahum_messenger_20.htm";
				break;
			case 35622:
				file = "residence2/clanhall/farm_kel_mahum_messenger_23.htm";
				break;
		}
		return file;
	}
	
	/**
	 * Method checkCond.
	 * @param player Player
	 * @param regMenu boolean
	 * @return boolean
	 */
	private boolean checkCond(Player player, boolean regMenu)
	{
		Clan clan = player.getClan();
		ClanHall clanHall = getClanHall();
		ClanHallTeamBattleEvent siegeEvent = clanHall.getSiegeEvent();
		List<CTBSiegeClanObject> siegeClans = siegeEvent.getObjects(SiegeEvent.ATTACKERS);
		SiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, clan);
		if (siegeEvent.isRegistrationOver())
		{
			showChatWindow(player, "quests/_655_AGrandPlanForTamingWildBeasts/farm_messenger_q0655_11.htm", "%siege_time%", TimeUtils.toSimpleFormat(clanHall.getSiegeDate()));
			return false;
		}
		if (regMenu && (siegeClan != null))
		{
			return true;
		}
		if ((clan == null) || (player.getObjectId() != clan.getLeaderId()))
		{
			showChatWindow(player, "quests/_655_AGrandPlanForTamingWildBeasts/farm_messenger_q0655_03.htm");
			return false;
		}
		if ((player.getObjectId() == clan.getLeaderId()) && (clan.getLevel() < 4))
		{
			showChatWindow(player, "quests/_655_AGrandPlanForTamingWildBeasts/farm_messenger_q0655_05.htm");
			return false;
		}
		if (clan.getHasHideout() == clanHall.getId())
		{
			showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_22.htm");
			return false;
		}
		if (clan.getHasHideout() > 0)
		{
			showChatWindow(player, "quests/_655_AGrandPlanForTamingWildBeasts/farm_messenger_q0655_04.htm");
			return false;
		}
		if (siegeClans.size() >= 5)
		{
			showChatWindow(player, "residence2/clanhall/farm_kel_mahum_messenger_21.htm");
			return false;
		}
		return true;
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
		Clan clan = getClanHall().getOwner();
		if (clan != null)
		{
			showChatWindow(player, "residence2/clanhall/farm_messenger001.htm", "%owner_name%", clan.getName());
		}
		else
		{
			showChatWindow(player, "residence2/clanhall/farm_messenger002.htm");
		}
	}
}
