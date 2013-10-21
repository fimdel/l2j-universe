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
package lineage2.gameserver.model.entity.events.objects;

import lineage2.gameserver.model.pledge.Clan;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuctionSiegeClanObject extends SiegeClanObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _bid.
	 */
	private long _bid;
	
	/**
	 * Constructor for AuctionSiegeClanObject.
	 * @param type String
	 * @param clan Clan
	 * @param param long
	 */
	public AuctionSiegeClanObject(String type, Clan clan, long param)
	{
		this(type, clan, param, System.currentTimeMillis());
	}
	
	/**
	 * Constructor for AuctionSiegeClanObject.
	 * @param type String
	 * @param clan Clan
	 * @param param long
	 * @param date long
	 */
	public AuctionSiegeClanObject(String type, Clan clan, long param, long date)
	{
		super(type, clan, param, date);
		_bid = param;
	}
	
	/**
	 * Method getParam.
	 * @return long
	 */
	@Override
	public long getParam()
	{
		return _bid;
	}
	
	/**
	 * Method setParam.
	 * @param param long
	 */
	public void setParam(long param)
	{
		_bid = param;
	}
}
