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
package lineage2.gameserver.templates.player;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClassData
{
	/**
	 * Field _classId.
	 */
	private final int _classId;
	/**
	 * Field _lvlsUpData.
	 */
	private final TIntObjectHashMap<LvlUpData> _lvlsUpData = new TIntObjectHashMap<>();
	
	/**
	 * Constructor for ClassData.
	 * @param classId int
	 */
	public ClassData(int classId)
	{
		_classId = classId;
	}
	
	/**
	 * Method addLvlUpData.
	 * @param lvl int
	 * @param hp double
	 * @param mp double
	 * @param cp double
	 */
	public void addLvlUpData(int lvl, double hp, double mp, double cp)
	{
		_lvlsUpData.put(lvl, new LvlUpData(hp, mp, cp));
	}
	
	/**
	 * Method getLvlUpData.
	 * @param lvl int
	 * @return LvlUpData
	 */
	public LvlUpData getLvlUpData(int lvl)
	{
		return _lvlsUpData.get(lvl);
	}
	
	/**
	 * Method getClassId.
	 * @return int
	 */
	public int getClassId()
	{
		return _classId;
	}
}
