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

import gnu.trove.list.array.TIntArrayList;
import lineage2.commons.util.TroveUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Faction
{
	/**
	 * Field none. (value is ""none"")
	 */
	public final static String none = "none";
	/**
	 * Field NONE.
	 */
	public final static Faction NONE = new Faction(none);
	/**
	 * Field factionId.
	 */
	public final String factionId;
	/**
	 * Field factionRange.
	 */
	public int factionRange;
	/**
	 * Field ignoreId.
	 */
	public TIntArrayList ignoreId = TroveUtils.EMPTY_INT_ARRAY_LIST;
	
	/**
	 * Constructor for Faction.
	 * @param factionId String
	 */
	public Faction(String factionId)
	{
		this.factionId = factionId;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return factionId;
	}
	
	/**
	 * Method setRange.
	 * @param factionRange int
	 */
	public void setRange(int factionRange)
	{
		this.factionRange = factionRange;
	}
	
	/**
	 * Method getRange.
	 * @return int
	 */
	public int getRange()
	{
		return factionRange;
	}
	
	/**
	 * Method addIgnoreNpcId.
	 * @param npcId int
	 */
	public void addIgnoreNpcId(int npcId)
	{
		if (ignoreId.isEmpty())
		{
			ignoreId = new TIntArrayList();
		}
		ignoreId.add(npcId);
	}
	
	/**
	 * Method isIgnoreNpcId.
	 * @param npcId int
	 * @return boolean
	 */
	public boolean isIgnoreNpcId(int npcId)
	{
		return ignoreId.contains(npcId);
	}
	
	/**
	 * Method isNone.
	 * @return boolean
	 */
	public boolean isNone()
	{
		return factionId.isEmpty() || factionId.equals(none);
	}
	
	/**
	 * Method equals.
	 * @param faction Faction
	 * @return boolean
	 */
	public boolean equals(Faction faction)
	{
		if (isNone() || !faction.getName().equalsIgnoreCase(factionId))
		{
			return false;
		}
		return true;
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
		return equals((Faction) o);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return isNone() ? none : factionId;
	}
}
