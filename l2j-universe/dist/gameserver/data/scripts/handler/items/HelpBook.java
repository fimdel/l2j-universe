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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HelpBook extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		34767,
		5588,
		6317,
		7561,
		7063,
		7064,
		7065,
		7066,
		7082,
		7083,
		7084,
		7085,
		7086,
		7087,
		7088,
		7089,
		7090,
		7091,
		7092,
		7093,
		7094,
		7095,
		7096,
		7097,
		7098,
		7099,
		7100,
		7101,
		7102,
		7103,
		7104,
		7105,
		7106,
		7107,
		7108,
		7109,
		7110,
		7111,
		7112,
		8059,
		13130,
		13131,
		13132,
		13133,
		13134,
		13135,
		13136,
		17213
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
		if (!playable.isPlayer())
		{
			return false;
		}
		final Player activeChar = (Player) playable;
		Functions.show("help/" + item.getItemId() + ".htm", activeChar, null);
		if (item.getItemId() == 7063)
		{
			activeChar.sendPacket(new RadarControl(0, 2, new Location(51995, -51265, -3104)));
		}
		activeChar.sendActionFailed();
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
