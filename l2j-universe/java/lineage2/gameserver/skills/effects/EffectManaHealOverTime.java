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
public class EffectManaHealOverTime extends Effect
{
	/**
	 * Field _ignoreMpEff.
	 */
	private final boolean _ignoreMpEff;
	
	/**
	 * Constructor for EffectManaHealOverTime.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectManaHealOverTime(Env env, EffectTemplate template)
	{
		super(env, template);
		_ignoreMpEff = template.getParam().getBool("ignoreMpEff", false);
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
		double mp = calc();
		double newMp = (mp * (!_ignoreMpEff ? _effected.calcStat(Stats.MANAHEAL_EFFECTIVNESS, 100., _effector, getSkill()) : 100.)) / 100.;
		double addToMp = Math.max(0, Math.min(newMp, ((_effected.calcStat(Stats.MP_LIMIT, null, null) * _effected.getMaxMp()) / 100.) - _effected.getCurrentMp()));
		if (addToMp > 0)
		{
			_effected.setCurrentMp(_effected.getCurrentMp() + addToMp);
		}
		return true;
	}
}
