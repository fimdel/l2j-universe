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
import lineage2.gameserver.model.ArmorSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ArmorSetsHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final ArmorSetsHolder _instance = new ArmorSetsHolder();
	/**
	 * Field _armorSets.
	 */
	private final TIntObjectHashMap<ArmorSet> _armorSets = new TIntObjectHashMap<>();
	
	/**
	 * Method getInstance.
	 * @return ArmorSetsHolder
	 */
	public static ArmorSetsHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addArmorSet.
	 * @param armorset ArmorSet
	 */
	public void addArmorSet(ArmorSet armorset)
	{
		for (int id : armorset.getChestIds())
		{
			_armorSets.put(id, armorset);
		}
		for (int id : armorset.getLegIds())
		{
			_armorSets.put(id, armorset);
		}
		for (int id : armorset.getHeadIds())
		{
			_armorSets.put(id, armorset);
		}
		for (int id : armorset.getGlovesIds())
		{
			_armorSets.put(id, armorset);
		}
		for (int id : armorset.getFeetIds())
		{
			_armorSets.put(id, armorset);
		}
		for (int id : armorset.getShieldIds())
		{
			_armorSets.put(id, armorset);
		}
	}
	
	/**
	 * Method getArmorSet.
	 * @param id int
	 * @return ArmorSet
	 */
	public ArmorSet getArmorSet(int id)
	{
		return _armorSets.get(id);
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _armorSets.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_armorSets.clear();
	}
}
