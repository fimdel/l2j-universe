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
package npc.model.events;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import services.TeleToFantasyIsle;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ColiseumHelperInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field LOCS.
	 */
	private final Location[][] LOCS = new Location[][]
	{
		{
			new Location(-84451, -45452, -10728),
			new Location(-84580, -45587, -10728)
		},
		{
			new Location(-86154, -50429, -10728),
			new Location(-86118, -50624, -10728)
		},
		{
			new Location(-82009, -53652, -10728),
			new Location(-81802, -53665, -10728)
		},
		{
			new Location(-77603, -50673, -10728),
			new Location(-77586, -50503, -10728)
		},
		{
			new Location(-79186, -45644, -10728),
			new Location(-79309, -45561, -10728)
		}
	};
	
	/**
	 * Constructor for ColiseumHelperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ColiseumHelperInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
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
		if (command.equals("teleOut"))
		{
			player.teleToLocation(TeleToFantasyIsle.POINTS[Rnd.get(TeleToFantasyIsle.POINTS.length)]);
		}
		else if (command.startsWith("coliseum"))
		{
			int a = Integer.parseInt(String.valueOf(command.charAt(9)));
			Location[] locs = LOCS[a];
			player.teleToLocation(locs[Rnd.get(locs.length)]);
		}
		else
		{
			super.onBypassFeedback(player, command);
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
		showChatWindow(player, "events/guide_gcol001.htm");
	}
}
