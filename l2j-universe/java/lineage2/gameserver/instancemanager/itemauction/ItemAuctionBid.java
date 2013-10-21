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
package lineage2.gameserver.instancemanager.itemauction;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ItemAuctionBid
{
	/**
	 * Field _charId.
	 */
	private final int _charId;
	/**
	 * Field _lastBid.
	 */
	private long _lastBid;
	
	/**
	 * Constructor for ItemAuctionBid.
	 * @param charId int
	 * @param lastBid long
	 */
	public ItemAuctionBid(int charId, long lastBid)
	{
		_charId = charId;
		_lastBid = lastBid;
	}
	
	/**
	 * Method getCharId.
	 * @return int
	 */
	public final int getCharId()
	{
		return _charId;
	}
	
	/**
	 * Method getLastBid.
	 * @return long
	 */
	public final long getLastBid()
	{
		return _lastBid;
	}
	
	/**
	 * Method setLastBid.
	 * @param lastBid long
	 */
	final void setLastBid(long lastBid)
	{
		_lastBid = lastBid;
	}
	
	/**
	 * Method cancelBid.
	 */
	final void cancelBid()
	{
		_lastBid = -1;
	}
	
	/**
	 * Method isCanceled.
	 * @return boolean
	 */
	final boolean isCanceled()
	{
		return _lastBid == -1;
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	final Player getPlayer()
	{
		return GameObjectsStorage.getPlayer(_charId);
	}
}
