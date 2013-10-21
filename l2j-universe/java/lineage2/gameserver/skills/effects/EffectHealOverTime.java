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
import lineage2.gameserver.network.serverpackets.ExRegenMax;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectHealOverTime extends Effect
{
	/**
	 * Field _ignoreHpEff.
	 */
	private final boolean _ignoreHpEff;
	
	/**
	 * Constructor for EffectHealOverTime.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectHealOverTime(Env env, EffectTemplate template)
	{
		super(env, template);
		_ignoreHpEff = template.getParam().getBool("ignoreHpEff", false);
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		if (getEffected().isPlayer() && (getCount() > 0) && (getPeriod() > 0))
		{
			getEffected().sendPacket(new ExRegenMax(calc(), (int) ((getCount() * getPeriod()) / 1000), Math.round(getPeriod() / 1000)));
		}
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		if (_effected.isHealBlocked())
		{
			return true;
		}
		double hp = calc();
		double newHp = (hp * (!_ignoreHpEff ? _effected.calcStat(Stats.HEAL_EFFECTIVNESS, 100., _effector, getSkill()) : 100.)) / 100.;
		double addToHp = Math.max(0, Math.min(newHp, ((_effected.calcStat(Stats.HP_LIMIT, null, null) * _effected.getMaxHp()) / 100.) - _effected.getCurrentHp()));
		if (addToHp > 0)
		{
			getEffected().setCurrentHp(_effected.getCurrentHp() + addToHp, false);
		}
		return true;
	}
}
