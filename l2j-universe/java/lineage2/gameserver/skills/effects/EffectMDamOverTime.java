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
import lineage2.gameserver.stats.Formulas;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectMDamOverTime extends Effect
{
	
	/**
	 * Constructor for EffectDamOverTime.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectMDamOverTime(Env env, EffectTemplate template)
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
			if (getSkill().isUsingWhileCasting())
			{
				_effector.abortCast(true, true);
			}
			return false;
		}
		int sps = getSkill().isSSPossible() ? (getSkill().isMagic() ? _effector.getChargedSpiritShot() : _effector.getChargedSoulShot() ? 2 : 0) : 0;
		Formulas.AttackInfo info = Formulas.calcMagicDam(_effector, _effected, getSkill(), sps);
		
		if ((info.damage > (_effected.getCurrentHp() - 1)) && !_effected.isNpc())
		{
			if (!getSkill().isOffensive())
			{
				_effected.sendPacket(Msg.NOT_ENOUGH_HP);
			}
			if (getSkill().isUsingWhileCasting())
			{
				_effector.abortCast(true, true);
			}
			return false;
		}
		if (getSkill().getAbsorbPart() > 0)
		{
			_effector.setCurrentHp((getSkill().getAbsorbPart() * Math.min(_effected.getCurrentHp(), info.damage)) + _effector.getCurrentHp(), false);
		}
		_effected.reduceCurrentHp(info.damage, 0, _effector, getSkill(), !_effected.isNpc() && (_effected != _effector), _effected != _effector, _effector.isNpc() || (_effected == _effector), false, false, true, false);
		return true;
	}
}
