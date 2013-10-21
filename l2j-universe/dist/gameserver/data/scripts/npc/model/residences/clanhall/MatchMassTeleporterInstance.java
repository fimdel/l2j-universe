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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.events.impl.ClanHallTeamBattleEvent;
import lineage2.gameserver.model.entity.events.objects.CTBTeamObject;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MatchMassTeleporterInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _flagId.
	 */
	private final int _flagId;
	/**
	 * Field _timeout.
	 */
	private long _timeout;
	
	/**
	 * Constructor for MatchMassTeleporterInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MatchMassTeleporterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_flagId = template.getAIParams().getInteger("flag");
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
		ClanHall clanHall = getClanHall();
		ClanHallTeamBattleEvent siegeEvent = clanHall.getSiegeEvent();
		
		if (_timeout > System.currentTimeMillis())
		{
			showChatWindow(player, "residence2/clanhall/agit_mass_teleporter001.htm");
			return;
		}
		
		if (isInRange(player, INTERACTION_DISTANCE))
		{
			_timeout = System.currentTimeMillis() + 60000L;
			
			List<CTBTeamObject> locs = siegeEvent.getObjects(ClanHallTeamBattleEvent.TRYOUT_PART);
			
			CTBTeamObject object = locs.get(_flagId);
			if (object.getFlag() != null)
			{
				for (Player $player : World.getAroundPlayers(this, 400, 100))
				{
					$player.teleToLocation(Location.findPointToStay(object.getFlag(), 100, 125));
				}
			}
		}
	}
}
