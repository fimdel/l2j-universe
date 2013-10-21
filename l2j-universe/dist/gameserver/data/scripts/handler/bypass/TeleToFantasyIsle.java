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
package handler.bypass;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleToFantasyIsle extends ScriptBypassHandler
{
	/**
	 * Field POINTS.
	 */
	public static final Location[] POINTS =
	{
		new Location(-60695, -56896, -2032),
		new Location(-59716, -55920, -2032),
		new Location(-58752, -56896, -2032),
		new Location(-59716, -57864, -2032)
	};
	
	/**
	 * Method getBypasses.
	 * @return String[] * @see lineage2.gameserver.handler.bypass.IBypassHandler#getBypasses()
	 */
	@Override
	public String[] getBypasses()
	{
		return new String[]
		{
			"teleToFantasyIsle"
		};
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param command String
	 * @see lineage2.gameserver.handler.bypass.IBypassHandler#onBypassFeedback(NpcInstance, Player, String)
	 */
	@Override
	public void onBypassFeedback(NpcInstance npc, Player player, String command)
	{
		player.teleToLocation(Rnd.get(POINTS));
	}
}
