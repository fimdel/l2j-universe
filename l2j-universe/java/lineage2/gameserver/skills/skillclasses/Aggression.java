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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Aggression extends Skill
{
	/**
	 * Field _unaggring.
	 */
	private final boolean _unaggring;
	/**
	 * Field _silent.
	 */
	private final boolean _silent;
	
	/**
	 * Constructor for Aggression.
	 * @param set StatsSet
	 */
	public Aggression(StatsSet set)
	{
		super(set);
		_unaggring = set.getBool("unaggroing", false);
		_silent = set.getBool("silent", false);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		int effect = _effectPoint;
		if (isSSPossible() && (activeChar.getChargedSoulShot() || (activeChar.getChargedSpiritShot() > 0)))
		{
			effect *= 2;
		}
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (!target.isAutoAttackable(activeChar))
				{
					continue;
				}
				if (target.isNpc())
				{
					if (_unaggring)
					{
						if (target.isNpc() && activeChar.isPlayable())
						{
							((NpcInstance) target).getAggroList().addDamageHate(activeChar, 0, -effect);
						}
					}
					else
					{
						target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, effect);
						if (!_silent)
						{
							target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar, 0);
						}
					}
				}
				else if (target.isPlayable() && !target.isDebuffImmune())
				{
					target.setTarget(activeChar);
				}
				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}
