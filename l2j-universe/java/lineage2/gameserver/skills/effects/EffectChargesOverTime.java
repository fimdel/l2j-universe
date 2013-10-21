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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectChargesOverTime extends Effect
{
	/**
	 * Field _maxCharges.
	 */
	private final int _maxCharges;
	
	/**
	 * Constructor for EffectChargesOverTime.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectChargesOverTime(Env env, EffectTemplate template)
	{
		super(env, template);
		_maxCharges = getTemplate().getParam().getInteger("maxCharges", 10);
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
		if (damage > (_effected.getCurrentHp() - 1))
		{
			if (!getSkill().isOffensive())
			{
				_effected.sendPacket(Msg.NOT_ENOUGH_HP);
			}
			return false;
		}
		if (_effected.getIncreasedForce() >= _maxCharges)
		{
			_effected.sendPacket(Msg.YOUR_FORCE_HAS_REACHED_MAXIMUM_CAPACITY_);
			return false;
		}
		_effected.setIncreasedForce(_effected.getIncreasedForce() + 1);
		_effected.reduceCurrentHp(damage, 0, _effector, getSkill(), false, false, true, false, false, true, false);
		return true;
	}
}
