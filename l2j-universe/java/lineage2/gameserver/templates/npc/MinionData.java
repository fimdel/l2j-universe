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
package lineage2.gameserver.templates.npc;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MinionData
{
	/**
	 * Field _minionId.
	 */
	private final int _minionId;
	/**
	 * Field _minionAmount.
	 */
	private final int _minionAmount;
	
	/**
	 * Constructor for MinionData.
	 * @param minionId int
	 * @param minionAmount int
	 */
	public MinionData(int minionId, int minionAmount)
	{
		_minionId = minionId;
		_minionAmount = minionAmount;
	}
	
	/**
	 * Method getMinionId.
	 * @return int
	 */
	public int getMinionId()
	{
		return _minionId;
	}
	
	/**
	 * Method getAmount.
	 * @return int
	 */
	public int getAmount()
	{
		return _minionAmount;
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != this.getClass())
		{
			return false;
		}
		return ((MinionData) o).getMinionId() == getMinionId();
	}
}
