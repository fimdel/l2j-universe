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
import lineage2.gameserver.templates.SoulCrystal;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SoulCrystalHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final SoulCrystalHolder _instance = new SoulCrystalHolder();
	
	/**
	 * Method getInstance.
	 * @return SoulCrystalHolder
	 */
	public static SoulCrystalHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _crystals.
	 */
	private final TIntObjectHashMap<SoulCrystal> _crystals = new TIntObjectHashMap<>();
	
	/**
	 * Method addCrystal.
	 * @param crystal SoulCrystal
	 */
	public void addCrystal(SoulCrystal crystal)
	{
		_crystals.put(crystal.getItemId(), crystal);
	}
	
	/**
	 * Method getCrystal.
	 * @param item int
	 * @return SoulCrystal
	 */
	public SoulCrystal getCrystal(int item)
	{
		return _crystals.get(item);
	}
	
	/**
	 * Method getCrystals.
	 * @return SoulCrystal[]
	 */
	public SoulCrystal[] getCrystals()
	{
		return _crystals.values(new SoulCrystal[_crystals.size()]);
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _crystals.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_crystals.clear();
	}
}
