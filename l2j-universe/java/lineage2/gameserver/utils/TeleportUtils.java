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
package lineage2.gameserver.utils;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.MapRegionManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.RestartType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.mapregion.RestartArea;
import lineage2.gameserver.templates.mapregion.RestartPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleportUtils
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TeleportUtils.class);
	/**
	 * Field DEFAULT_RESTART.
	 */
	public final static Location DEFAULT_RESTART = new Location(17817, 170079, -3530);
	
	/**
	 * Constructor for TeleportUtils.
	 */
	private TeleportUtils()
	{
	}
	
	/**
	 * Method getRestartLocation.
	 * @param player Player
	 * @param restartType RestartType
	 * @return Location
	 */
	public static Location getRestartLocation(Player player, RestartType restartType)
	{
		return getRestartLocation(player, player.getLoc(), restartType);
	}
	
	/**
	 * Method getRestartLocation.
	 * @param player Player
	 * @param from Location
	 * @param restartType RestartType
	 * @return Location
	 */
	public static Location getRestartLocation(Player player, Location from, RestartType restartType)
	{
		Reflection r = player.getReflection();
		if (r != ReflectionManager.DEFAULT)
		{
			if (r.getCoreLoc() != null)
			{
				return r.getCoreLoc();
			}
			else if (r.getReturnLoc() != null)
			{
				return r.getReturnLoc();
			}
		}
		Clan clan = player.getClan();
		if (clan != null)
		{
			if ((restartType == RestartType.TO_CLANHALL) && (clan.getHasHideout() != 0))
			{
				return ResidenceHolder.getInstance().getResidence(clan.getHasHideout()).getOwnerRestartPoint();
			}
			if ((restartType == RestartType.TO_CASTLE) && (clan.getCastle() != 0))
			{
				return ResidenceHolder.getInstance().getResidence(clan.getCastle()).getOwnerRestartPoint();
			}
			if ((restartType == RestartType.TO_FORTRESS) && (clan.getHasFortress() != 0))
			{
				return ResidenceHolder.getInstance().getResidence(clan.getHasFortress()).getOwnerRestartPoint();
			}
		}
		if (player.isChaotic())
		{
			if (player.getPKRestartPoint() != null)
			{
				return player.getPKRestartPoint();
			}
		}
		else
		{
			if (player.getRestartPoint() != null)
			{
				return player.getRestartPoint();
			}
		}
		RestartArea ra = MapRegionManager.getInstance().getRegionData(RestartArea.class, from);
		if (ra != null)
		{
			RestartPoint rp = ra.getRestartPoint().get(player.getRace());
			Location restartPoint = Rnd.get(rp.getRestartPoints());
			Location PKrestartPoint = Rnd.get(rp.getPKrestartPoints());
			return player.isChaotic() ? PKrestartPoint : restartPoint;
		}
		_log.warn("Cannot find restart location from coordinates: " + from + "!");
		return DEFAULT_RESTART;
	}
}
