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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Skill;
import lineage2.gameserver.stats.StatTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OptionDataTemplate extends StatTemplate
{
	/**
	 * Field _skills.
	 */
	private final List<Skill> _skills = new ArrayList<>(0);
	/**
	 * Field _id.
	 */
	private final int _id;
	
	/**
	 * Constructor for OptionDataTemplate.
	 * @param id int
	 */
	public OptionDataTemplate(int id)
	{
		_id = id;
	}
	
	/**
	 * Method addSkill.
	 * @param skill Skill
	 */
	public void addSkill(Skill skill)
	{
		_skills.add(skill);
	}
	
	/**
	 * Method getSkills.
	 * @return List<Skill>
	 */
	public List<Skill> getSkills()
	{
		return _skills;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
}
