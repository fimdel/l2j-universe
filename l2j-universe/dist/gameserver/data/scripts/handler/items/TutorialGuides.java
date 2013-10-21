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
package handler.items;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TutorialGuides extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		32777,
		32778,
	};
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean * @see lineage2.gameserver.handler.items.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		final int itemId = item.getItemId();
		switch (itemId)
		{
			case 32777:
				playable.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_ADVENTURER, 0x02));
				break;
			case 32778:
				playable.getPlayer().sendPacket(new TutorialShowHtml(TutorialShowHtml.GUIDE_AWAKING, 0x02));
				break;
			default:
				return false;
		}
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public final int[] getItemIds()
	{
		return _itemIds;
	}
}
