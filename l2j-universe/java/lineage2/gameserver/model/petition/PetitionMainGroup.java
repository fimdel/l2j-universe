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
package lineage2.gameserver.model.petition;

import java.util.Collection;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetitionMainGroup extends PetitionGroup
{
	/**
	 * Field _subGroups.
	 */
	private final IntObjectMap<PetitionSubGroup> _subGroups = new HashIntObjectMap<>();
	
	/**
	 * Constructor for PetitionMainGroup.
	 * @param id int
	 */
	public PetitionMainGroup(int id)
	{
		super(id);
	}
	
	/**
	 * Method addSubGroup.
	 * @param subGroup PetitionSubGroup
	 */
	public void addSubGroup(PetitionSubGroup subGroup)
	{
		_subGroups.put(subGroup.getId(), subGroup);
	}
	
	/**
	 * Method getSubGroup.
	 * @param val int
	 * @return PetitionSubGroup
	 */
	public PetitionSubGroup getSubGroup(int val)
	{
		return _subGroups.get(val);
	}
	
	/**
	 * Method getSubGroups.
	 * @return Collection<PetitionSubGroup>
	 */
	public Collection<PetitionSubGroup> getSubGroups()
	{
		return _subGroups.values();
	}
}
