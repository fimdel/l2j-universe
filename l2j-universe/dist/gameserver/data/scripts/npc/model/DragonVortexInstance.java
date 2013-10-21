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
package npc.model;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class DragonVortexInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field bosses.
	 */
	private final int[] bosses =
	{
		25718,
		25719,
		25720,
		25721,
		25722,
		25723,
		25724
	};
	/**
	 * Field boss.
	 */
	private NpcInstance boss;
	
	/**
	 * Constructor for DragonVortexInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public DragonVortexInstance(int objectId, NpcTemplate template)
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
		if (command.startsWith("request_boss"))
		{
			if ((boss != null) && !boss.isDead())
			{
				showChatWindow(player, "default/32871-3.htm");
				return;
			}
			if (ItemFunctions.getItemCount(player, 17248) > 0)
			{
				ItemFunctions.removeItem(player, 17248, 1, true);
				boss = NpcUtils.spawnSingle(bosses[Rnd.get(bosses.length)], Location.coordsRandomize(getLoc(), 300, 600), getReflection());
				showChatWindow(player, "default/32871-1.htm");
			}
			else
			{
				showChatWindow(player, "default/32871-2.htm");
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
