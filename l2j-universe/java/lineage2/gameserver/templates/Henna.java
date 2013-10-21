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
package lineage2.gameserver.templates;

import gnu.trove.list.array.TIntArrayList;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Henna
{
	/**
	 * Field _symbolId.
	 */
	private final int _symbolId;
	/**
	 * Field _dyeId.
	 */
	private final int _dyeId;
	/**
	 * Field _price.
	 */
	private final long _price;
	/**
	 * Field _drawCount.
	 */
	private final long _drawCount;
	/**
	 * Field _statINT.
	 */
	private final int _statINT;
	/**
	 * Field _statSTR.
	 */
	private final int _statSTR;
	/**
	 * Field _statCON.
	 */
	private final int _statCON;
	/**
	 * Field _statMEN.
	 */
	private final int _statMEN;
	/**
	 * Field _statDEX.
	 */
	private final int _statDEX;
	/**
	 * Field _statWIT.
	 */
	private final int _statWIT;
	/**
	 * Field _skillId.
	 */
	private final int _skillId;
	/**
	 * Field _classes.
	 */
	private final TIntArrayList _classes;
	
	/**
	 * Constructor for Henna.
	 * @param symbolId int
	 * @param dyeId int
	 * @param price long
	 * @param drawCount long
	 * @param wit int
	 * @param intA int
	 * @param con int
	 * @param str int
	 * @param dex int
	 * @param men int
	 * @param skillId int
	 * @param classes TIntArrayList
	 */
	public Henna(int symbolId, int dyeId, long price, long drawCount, int wit, int intA, int con, int str, int dex, int men, int skillId, TIntArrayList classes)
	{
		_symbolId = symbolId;
		_dyeId = dyeId;
		_price = price;
		_drawCount = drawCount;
		_statINT = intA;
		_statSTR = str;
		_statCON = con;
		_statMEN = men;
		_statDEX = dex;
		_statWIT = wit;
		_classes = classes;
		_skillId = skillId;
	}
	
	/**
	 * Method getSymbolId.
	 * @return int
	 */
	public int getSymbolId()
	{
		return _symbolId;
	}
	
	/**
	 * Method getDyeId.
	 * @return int
	 */
	public int getDyeId()
	{
		return _dyeId;
	}
	
	/**
	 * Method getPrice.
	 * @return long
	 */
	public long getPrice()
	{
		return _price;
	}
	
	/**
	 * Method getStatINT.
	 * @return int
	 */
	public int getStatINT()
	{
		return _statINT;
	}
	
	/**
	 * Method getStatSTR.
	 * @return int
	 */
	public int getStatSTR()
	{
		return _statSTR;
	}
	
	/**
	 * Method getStatCON.
	 * @return int
	 */
	public int getStatCON()
	{
		return _statCON;
	}
	
	/**
	 * Method getStatMEN.
	 * @return int
	 */
	public int getStatMEN()
	{
		return _statMEN;
	}
	
	/**
	 * Method getStatDEX.
	 * @return int
	 */
	public int getStatDEX()
	{
		return _statDEX;
	}
	
	/**
	 * Method getStatWIT.
	 * @return int
	 */
	public int getStatWIT()
	{
		return _statWIT;
	}
	
	/**
	 * Method isForThisClass.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isForThisClass(Player player)
	{
		return _classes.contains(player.getActiveClassId());
	}
	
	/**
	 * Method getDrawCount.
	 * @return long
	 */
	public long getDrawCount()
	{
		return _drawCount;
	}
	
	/**
	 * Method getSkillId.
	 * @return int
	 */
	public int getSkillId()
	{
		return _skillId;
	}
}
