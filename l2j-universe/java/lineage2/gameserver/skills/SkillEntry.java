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
package lineage2.gameserver.skills;

import java.util.AbstractMap;

import lineage2.gameserver.model.Skill;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SkillEntry extends AbstractMap.SimpleImmutableEntry<SkillEntryType, Skill>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _disabled.
	 */
	private boolean _disabled;
	
	/**
	 * Constructor for SkillEntry.
	 * @param key SkillEntryType
	 * @param value Skill
	 */
	public SkillEntry(SkillEntryType key, Skill value)
	{
		super(key, value);
	}
	
	/**
	 * Method isDisabled.
	 * @return boolean
	 */
	public boolean isDisabled()
	{
		return _disabled;
	}
	
	/**
	 * Method setDisabled.
	 * @param disabled boolean
	 */
	public void setDisabled(boolean disabled)
	{
		_disabled = disabled;
	}
}
