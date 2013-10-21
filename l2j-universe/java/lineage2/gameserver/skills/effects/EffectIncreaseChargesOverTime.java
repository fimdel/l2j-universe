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
package lineage2.gameserver.skills.effects;

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectIncreaseChargesOverTime extends Effect
{
	/**
	 * Constructor for EffectIncreaseChargesOverTime.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectIncreaseChargesOverTime(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		if (_effected.isDead())
		{
			return false;
		}
		double damage = calc();
		if (damage >= (getEffected().getCurrentHp() - 1))
		{
			if (getSkill().isToggle())
			{
				getEffected().sendPacket(new SystemMessage(SystemMessage.YOUR_SKILL_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_HP));
				return false;
			}
		}
		int increaseCount = 1;
		int chargeMaxCount = 10;
		if (getEffected().getIncreasedForce() < chargeMaxCount)
		{
			getEffected().reduceCurrentHp(damage, 0, getEffector(), getSkill(), true, true, true, false, false, false, true);
			(getEffected()).setIncreasedForce(getEffected().getIncreasedForce() + increaseCount);
		}
		return true;
	}
}
