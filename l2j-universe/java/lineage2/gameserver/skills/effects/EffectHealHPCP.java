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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectHealHPCP extends Effect
{
	/**
	 * Constructor for EffectHealHPCP.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectHealHPCP(Env env, EffectTemplate template)
	{
		super(env, template);
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
		if (_effected.isDead() || _effected.isHealBlocked())
		{
			return false;
		}
		double newHp = (calc() * _effected.calcStat(Stats.HEAL_EFFECTIVNESS, 100, _effector, getSkill())) / 100;
		double addToHp = Math.max(0, Math.min(newHp, ((_effected.calcStat(Stats.HP_LIMIT, null, null) * _effected.getMaxHp()) / 100.) - _effected.getCurrentHp()));
		_effected.sendPacket(new SystemMessage(SystemMessage.S1_HPS_HAVE_BEEN_RESTORED).addNumber(Math.round(addToHp)));
		if (addToHp > 0)
		{
			_effected.setCurrentHp(addToHp + _effected.getCurrentHp(), false);
		}
		else
		{
			double newCp = (calc() * _effected.getMaxCp()) / 100;
			double addToCp = Math.max(0, Math.min(newCp, ((_effected.calcStat(Stats.CP_LIMIT, null, null) * _effected.getMaxCp()) / 100.) - _effected.getCurrentCp()));
			_effected.sendPacket(new SystemMessage(SystemMessage.S1_WILL_RESTORE_S2S_CP).addNumber((long) addToCp));
			if (addToCp > 0)
			{
				_effected.setCurrentCp(addToCp + _effected.getCurrentCp());
			}
		}
		return true;
	}
}
