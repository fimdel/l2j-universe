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

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.utils.Language;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class PetitionGroup
{
	/**
	 * Field _name.
	 */
	private final Map<Language, String> _name = new HashMap<>(Language.VALUES.length);
	/**
	 * Field _description.
	 */
	private final Map<Language, String> _description = new HashMap<>(Language.VALUES.length);
	/**
	 * Field _id.
	 */
	private final int _id;
	
	/**
	 * Constructor for PetitionGroup.
	 * @param id int
	 */
	public PetitionGroup(int id)
	{
		_id = id;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getName.
	 * @param lang Language
	 * @return String
	 */
	public String getName(Language lang)
	{
		return _name.get(lang);
	}
	
	/**
	 * Method setName.
	 * @param lang Language
	 * @param name String
	 */
	public void setName(Language lang, String name)
	{
		_name.put(lang, name);
	}
	
	/**
	 * Method getDescription.
	 * @param lang Language
	 * @return String
	 */
	public String getDescription(Language lang)
	{
		return _description.get(lang);
	}
	
	/**
	 * Method setDescription.
	 * @param lang Language
	 * @param name String
	 */
	public void setDescription(Language lang, String name)
	{
		_description.put(lang, name);
	}
}
