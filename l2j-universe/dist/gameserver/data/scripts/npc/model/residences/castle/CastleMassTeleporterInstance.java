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
package npc.model.residences.castle;

import java.util.List;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeToggleNpcObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleMassTeleporterInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	private class TeleportTask extends RunnableImpl
	{
		/**
		 * Constructor for TeleportTask.
		 */
		public TeleportTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Functions.npcShout(CastleMassTeleporterInstance.this, NpcString.THE_DEFENDERS_OF_S1_CASTLE_WILL_BE_TELEPORTED_TO_THE_INNER_CASTLE, "#" + getCastle().getNpcStringName().getId());
			for (Player p : World.getAroundPlayers(CastleMassTeleporterInstance.this, 200, 50))
			{
				p.teleToLocation(Location.findPointToStay(_teleportLoc, 10, 100, p.getGeoIndex()));
			}
			_teleportTask = null;
		}
	}
	
	/**
	 * Field _teleportTask.
	 */
	Future<?> _teleportTask = null;
	/**
	 * Field _teleportLoc.
	 */
	final Location _teleportLoc;
	
	/**
	 * Constructor for CastleMassTeleporterInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public CastleMassTeleporterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_teleportLoc = Location.parseLoc(template.getAIParams().getString("teleport_loc"));
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
		if (_teleportTask != null)
		{
			showChatWindow(player, "residence2/castle/CastleTeleportDelayed.htm");
			return;
		}
		_teleportTask = ThreadPoolManager.getInstance().schedule(new TeleportTask(), isAllTowersDead() ? 480000L : 30000L);
		showChatWindow(player, "residence2/castle/CastleTeleportDelayed.htm");
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
		if (_teleportTask != null)
		{
			showChatWindow(player, "residence2/castle/CastleTeleportDelayed.htm");
		}
		else
		{
			if (isAllTowersDead())
			{
				showChatWindow(player, "residence2/castle/gludio_mass_teleporter002.htm");
			}
			else
			{
				showChatWindow(player, "residence2/castle/gludio_mass_teleporter001.htm");
			}
		}
	}
	
	/**
	 * Method isAllTowersDead.
	 * @return boolean
	 */
	private boolean isAllTowersDead()
	{
		SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
		if ((siegeEvent == null) || !siegeEvent.isInProgress())
		{
			return false;
		}
		List<SiegeToggleNpcObject> towers = siegeEvent.getObjects(CastleSiegeEvent.CONTROL_TOWERS);
		for (SiegeToggleNpcObject t : towers)
		{
			if (t.isAlive())
			{
				return false;
			}
		}
		return true;
	}
}
