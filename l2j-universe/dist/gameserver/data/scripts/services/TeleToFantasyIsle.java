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
package services;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleToFantasyIsle extends Functions
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
	 * Method toFantasyIsle.
	 */
	public void toFantasyIsle()
	{
		Player player = getSelf();
		if (!NpcInstance.canBypassCheck(player, player.getLastNpc()))
		{
			return;
		}
		player.setVar("backCoords", player.getLoc().toXYZString(), -1);
		player.teleToLocation(POINTS[Rnd.get(POINTS.length)]);
	}
	
	/**
	 * Method fromFantasyIsle.
	 */
	public void fromFantasyIsle()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		String var = player.getVar("backCoords");
		if ((var == null) || var.equals(""))
		{
			teleOut();
			return;
		}
		player.teleToLocation(Location.parseLoc(var));
	}
	
	/**
	 * Method teleOut.
	 */
	public void teleOut()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		player.teleToLocation(-44316, -113136, -80);
		show("I don't know from where you came here, but I can teleport you the nearest town.", player, npc);
	}
}
