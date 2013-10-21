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
package lineage2.gameserver.templates.npc;

import gnu.trove.set.hash.TIntHashSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AbsorbInfo
{
	/**
	 * @author Mobius
	 */
	public static enum AbsorbType
	{
		/**
		 * Field LAST_HIT.
		 */
		LAST_HIT,
		/**
		 * Field PARTY_ONE.
		 */
		PARTY_ONE,
		/**
		 * Field PARTY_ALL.
		 */
		PARTY_ALL,
		/**
		 * Field PARTY_RANDOM.
		 */
		PARTY_RANDOM
	}
	
	/**
	 * Field _skill.
	 */
	private final boolean _skill;
	/**
	 * Field _absorbType.
	 */
	private final AbsorbType _absorbType;
	/**
	 * Field _chance.
	 */
	private final int _chance;
	/**
	 * Field _cursedChance.
	 */
	private final int _cursedChance;
	/**
	 * Field _levels.
	 */
	private final TIntHashSet _levels;
	
	/**
	 * Constructor for AbsorbInfo.
	 * @param skill boolean
	 * @param absorbType AbsorbType
	 * @param chance int
	 * @param cursedChance int
	 * @param min int
	 * @param max int
	 */
	public AbsorbInfo(boolean skill, AbsorbType absorbType, int chance, int cursedChance, int min, int max)
	{
		_skill = skill;
		_absorbType = absorbType;
		_chance = chance;
		_cursedChance = cursedChance;
		_levels = new TIntHashSet(max - min);
		for (int i = min; i <= max; i++)
		{
			_levels.add(i);
		}
	}
	
	/**
	 * Method isSkill.
	 * @return boolean
	 */
	public boolean isSkill()
	{
		return _skill;
	}
	
	/**
	 * Method getAbsorbType.
	 * @return AbsorbType
	 */
	public AbsorbType getAbsorbType()
	{
		return _absorbType;
	}
	
	/**
	 * Method getChance.
	 * @return int
	 */
	public int getChance()
	{
		return _chance;
	}
	
	/**
	 * Method getCursedChance.
	 * @return int
	 */
	public int getCursedChance()
	{
		return _cursedChance;
	}
	
	/**
	 * Method canAbsorb.
	 * @param le int
	 * @return boolean
	 */
	public boolean canAbsorb(int le)
	{
		return _levels.contains(le);
	}
}
