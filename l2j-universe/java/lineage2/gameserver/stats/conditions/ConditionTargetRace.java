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
package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionTargetRace extends Condition
{
	/**
	 * Field _race.
	 */
	private final int _race;
	
	/**
	 * Constructor for ConditionTargetRace.
	 * @param race String
	 */
	public ConditionTargetRace(String race)
	{
		if (race.equalsIgnoreCase("Undead"))
		{
			_race = 1;
		}
		else if (race.equalsIgnoreCase("MagicCreatures"))
		{
			_race = 2;
		}
		else if (race.equalsIgnoreCase("Beasts"))
		{
			_race = 3;
		}
		else if (race.equalsIgnoreCase("Animals"))
		{
			_race = 4;
		}
		else if (race.equalsIgnoreCase("Plants"))
		{
			_race = 5;
		}
		else if (race.equalsIgnoreCase("Humanoids"))
		{
			_race = 6;
		}
		else if (race.equalsIgnoreCase("Spirits"))
		{
			_race = 7;
		}
		else if (race.equalsIgnoreCase("Angels"))
		{
			_race = 8;
		}
		else if (race.equalsIgnoreCase("Demons"))
		{
			_race = 9;
		}
		else if (race.equalsIgnoreCase("Dragons"))
		{
			_race = 10;
		}
		else if (race.equalsIgnoreCase("Giants"))
		{
			_race = 11;
		}
		else if (race.equalsIgnoreCase("Bugs"))
		{
			_race = 12;
		}
		else if (race.equalsIgnoreCase("Fairies"))
		{
			_race = 13;
		}
		else if (race.equalsIgnoreCase("Humans"))
		{
			_race = 14;
		}
		else if (race.equalsIgnoreCase("Elves"))
		{
			_race = 15;
		}
		else if (race.equalsIgnoreCase("DarkElves"))
		{
			_race = 16;
		}
		else if (race.equalsIgnoreCase("Orcs"))
		{
			_race = 17;
		}
		else if (race.equalsIgnoreCase("Dwarves"))
		{
			_race = 18;
		}
		else if (race.equalsIgnoreCase("Others"))
		{
			_race = 19;
		}
		else if (race.equalsIgnoreCase("NonLivingBeings"))
		{
			_race = 20;
		}
		else if (race.equalsIgnoreCase("SiegeWeapons"))
		{
			_race = 21;
		}
		else if (race.equalsIgnoreCase("DefendingArmy"))
		{
			_race = 22;
		}
		else if (race.equalsIgnoreCase("Mercenaries"))
		{
			_race = 23;
		}
		else if (race.equalsIgnoreCase("UnknownCreature"))
		{
			_race = 24;
		}
		else if (race.equalsIgnoreCase("Kamael"))
		{
			_race = 25;
		}
		else
		{
			throw new IllegalArgumentException("ConditionTargetRace: Invalid race name: " + race);
		}
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		Creature target = env.target;
		return (target != null) && (target.getTemplate() != null) && (target.isServitor() || target.isNpc()) && (_race == ((NpcTemplate) target.getTemplate()).getRace());
	}
}
