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
package lineage2.gameserver.stats;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Env
{
	/**
	 * Field character.
	 */
	public Creature character;
	/**
	 * Field target.
	 */
	public Creature target;
	/**
	 * Field item.
	 */
	public ItemInstance item;
	/**
	 * Field skill.
	 */
	public Skill skill;
	/**
	 * Field value.
	 */
	public double value;
	
	/**
	 * Constructor for Env.
	 */
	public Env()
	{
	}
	
	/**
	 * Constructor for Env.
	 * @param cha Creature
	 * @param tar Creature
	 * @param sk Skill
	 */
	public Env(Creature cha, Creature tar, Skill sk)
	{
		character = cha;
		target = tar;
		skill = sk;
	}
}
