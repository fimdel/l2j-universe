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
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectDamOverTimeLethal extends Effect
{
	/**
	 * Constructor for EffectDamOverTimeLethal.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectDamOverTimeLethal(Env env, EffectTemplate template)
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
		if (getSkill().isOffensive())
		{
			damage *= 2;
		}
		damage = _effector.calcStat(getSkill().isMagic() ? Stats.MAGIC_DAMAGE : Stats.PHYSICAL_DAMAGE, damage, _effected, getSkill());
		_effected.reduceCurrentHp(damage, 0, _effector, getSkill(), !_effected.isNpc() && (_effected != _effector), _effected != _effector, _effector.isNpc() || (_effected == _effector), false, false, true, false);
		return true;
	}
}
