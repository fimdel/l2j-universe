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
package lineage2.gameserver.data.xml.holder;

import gnu.trove.map.hash.TIntDoubleHashMap;
import lineage2.commons.data.xml.AbstractHolder;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class LevelBonusHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final LevelBonusHolder _instance = new LevelBonusHolder();
	/**
	 * Field _bonusList.
	 */
	private final TIntDoubleHashMap _bonusList = new TIntDoubleHashMap();
	
	/**
	 * Method getInstance.
	 * @return LevelBonusHolder
	 */
	public static LevelBonusHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addLevelBonus.
	 * @param lvl int
	 * @param bonus double
	 */
	public void addLevelBonus(int lvl, double bonus)
	{
		_bonusList.put(lvl, bonus);
	}
	
	/**
	 * Method getLevelBonus.
	 * @param lvl int
	 * @return double
	 */
	public double getLevelBonus(int lvl)
	{
		return _bonusList.get(lvl);
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _bonusList.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_bonusList.clear();
	}
}
