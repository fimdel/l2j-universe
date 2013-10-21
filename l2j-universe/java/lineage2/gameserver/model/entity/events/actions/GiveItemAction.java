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
package lineage2.gameserver.model.entity.events.actions;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.EventAction;
import lineage2.gameserver.model.entity.events.GlobalEvent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GiveItemAction implements EventAction
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _count.
	 */
	private final long _count;
	
	/**
	 * Constructor for GiveItemAction.
	 * @param itemId int
	 * @param count long
	 */
	public GiveItemAction(int itemId, long count)
	{
		_itemId = itemId;
		_count = count;
	}
	
	/**
	 * Method call.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.EventAction#call(GlobalEvent)
	 */
	@Override
	public void call(GlobalEvent event)
	{
		for (Player player : event.itemObtainPlayers())
		{
			event.giveItem(player, _itemId, _count);
		}
	}
}
