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
package lineage2.gameserver.model;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PremiumItem
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _count.
	 */
	private long _count;
	/**
	 * Field _sender.
	 */
	private final String _sender;
	
	/**
	 * Constructor for PremiumItem.
	 * @param itemid int
	 * @param count long
	 * @param sender String
	 */
	public PremiumItem(int itemid, long count, String sender)
	{
		_itemId = itemid;
		_count = count;
		_sender = sender;
	}
	
	/**
	 * Method updateCount.
	 * @param newcount long
	 */
	public void updateCount(long newcount)
	{
		_count = newcount;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getCount.
	 * @return long
	 */
	public long getCount()
	{
		return _count;
	}
	
	/**
	 * Method getSender.
	 * @return String
	 */
	public String getSender()
	{
		return _sender;
	}
}
