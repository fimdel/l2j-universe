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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.ClanHallMiniGameEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.CMGSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RainbowCoordinatorInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for RainbowCoordinatorInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RainbowCoordinatorInstance(int objectId, NpcTemplate template)
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
		if (!canBypassCheck(player, this))
		{
			return;
		}
		ClanHall clanHall = getClanHall();
		ClanHallMiniGameEvent miniGameEvent = clanHall.getSiegeEvent();
		if (miniGameEvent == null)
		{
			return;
		}
		if (miniGameEvent.isArenaClosed())
		{
			showChatWindow(player, "residence2/clanhall/game_manager003.htm");
			return;
		}
		List<CMGSiegeClanObject> siegeClans = miniGameEvent.getObjects(SiegeEvent.ATTACKERS);
		CMGSiegeClanObject siegeClan = miniGameEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
		if (siegeClan == null)
		{
			showChatWindow(player, "residence2/clanhall/game_manager014.htm");
			return;
		}
		if (siegeClan.getPlayers().isEmpty())
		{
			Party party = player.getParty();
			if (party == null)
			{
				showChatWindow(player, player.isClanLeader() ? "residence2/clanhall/game_manager005.htm" : "residence2/clanhall/game_manager002.htm");
				return;
			}
			if (!player.isClanLeader())
			{
				showChatWindow(player, "residence2/clanhall/game_manager004.htm");
				return;
			}
			if (party.getMemberCount() < 5)
			{
				showChatWindow(player, "residence2/clanhall/game_manager003.htm");
				return;
			}
			if (party.getPartyLeader() != player)
			{
				showChatWindow(player, "residence2/clanhall/game_manager006.htm");
				return;
			}
			for (Player member : party.getPartyMembers())
			{
				if (member.getClan() != player.getClan())
				{
					showChatWindow(player, "residence2/clanhall/game_manager007.htm");
					return;
				}
			}
			int index = siegeClans.indexOf(siegeClan);
			SpawnExObject spawnEx = miniGameEvent.getFirstObject("arena_" + index);
			Location loc = (Location) spawnEx.getSpawns().get(0).getCurrentSpawnRange();
			for (Player member : party.getPartyMembers())
			{
				siegeClan.addPlayer(member.getObjectId());
				member.teleToLocation(Location.coordsRandomize(loc, 100, 200));
			}
		}
		else
		{
			showChatWindow(player, "residence2/clanhall/game_manager013.htm");
		}
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
		showChatWindow(player, "residence2/clanhall/game_manager001.htm");
	}
}
