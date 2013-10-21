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

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.CubicTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class CubicHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static CubicHolder _instance = new CubicHolder();
	/**
	 * Field _cubics.
	 */
	private final TIntObjectHashMap<CubicTemplate> _cubics = new TIntObjectHashMap<>(10);
	
	/**
	 * Method getInstance.
	 * @return CubicHolder
	 */
	public static CubicHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for CubicHolder.
	 */
	private CubicHolder()
	{
	}
	
	/**
	 * Method addCubicTemplate.
	 * @param template CubicTemplate
	 */
	public void addCubicTemplate(CubicTemplate template)
	{
		_cubics.put(hash(template.getId(), template.getLevel()), template);
	}
	
	/**
	 * Method getTemplate.
	 * @param id int
	 * @param level int
	 * @return CubicTemplate
	 */
	public CubicTemplate getTemplate(int id, int level)
	{
		return _cubics.get(hash(id, level));
	}
	
	/**
	 * Method hash.
	 * @param id int
	 * @param level int
	 * @return int
	 */
	public int hash(int id, int level)
	{
		return (id * 10000) + level;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _cubics.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_cubics.clear();
	}
}
