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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Disablers extends Skill
{
	/**
	 * Field _skillInterrupt.
	 */
	private final boolean _skillInterrupt;
	
	/**
	 * Constructor for Disablers.
	 * @param set StatsSet
	 */
	public Disablers(StatsSet set)
	{
		super(set);
		_skillInterrupt = set.getBool("skillInterrupt", false);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		Creature realTarget;
		boolean reflected;
		for (Creature target : targets)
		{
			if (target != null)
			{
				reflected = target.checkReflectSkill(activeChar, this);
				realTarget = reflected ? activeChar : target;
				if (_skillInterrupt)
				{
					if ((realTarget.getCastingSkill() != null) && !realTarget.getCastingSkill().isMagic() && !realTarget.isRaid())
					{
						realTarget.abortCast(false, true);
					}
					if (!realTarget.isRaid())
					{
						realTarget.abortAttack(true, true);
					}
				}
				getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}
