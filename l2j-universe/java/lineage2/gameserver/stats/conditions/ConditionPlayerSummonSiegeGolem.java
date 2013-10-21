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
package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionPlayerSummonSiegeGolem extends Condition
{
	/**
	 * Constructor for ConditionPlayerSummonSiegeGolem.
	 */
	public ConditionPlayerSummonSiegeGolem()
	{
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		Player player = env.character.getPlayer();
		if (player == null)
		{
			return false;
		}
		Zone zone = player.getZone(Zone.ZoneType.RESIDENCE);
		if (zone != null)
		{
			return false;
		}
		zone = player.getZone(Zone.ZoneType.SIEGE);
		if (zone == null)
		{
			return false;
		}
		SiegeEvent<?, ?> event = player.getEvent(SiegeEvent.class);
		if (event == null)
		{
			return false;
		}
		if (event instanceof CastleSiegeEvent)
		{
			if (zone.getParams().getInteger("residence") != event.getId())
			{
				return false;
			}
			if (event.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan()) == null)
			{
				return false;
			}
		}
		else if (event.getSiegeClan(SiegeEvent.DEFENDERS, player.getClan()) == null)
		{
			return false;
		}
		return true;
	}
}
