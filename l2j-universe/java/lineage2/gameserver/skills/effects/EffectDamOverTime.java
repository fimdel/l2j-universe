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

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectDamOverTime extends Effect
{
	/**
	 * Field bleed.
	 */
	private static int[] bleed = new int[]
	{
		12,
		17,
		25,
		34,
		44,
		54,
		62,
		67,
		72,
		77,
		82,
		87
	};
	/**
	 * Field poison.
	 */
	private static int[] poison = new int[]
	{
		11,
		16,
		24,
		32,
		41,
		50,
		58,
		63,
		68,
		72,
		77,
		82
	};
	/**
	 * Field _percent.
	 */
	private final boolean _percent;
	
	/**
	 * Constructor for EffectDamOverTime.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectDamOverTime(Env env, EffectTemplate template)
	{
		super(env, template);
		_percent = getTemplate().getParam().getBool("percent", false);
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
		if (_percent)
		{
			damage = _effected.getMaxHp() * _template._value * 0.01;
		}
		if ((damage < 2) && (getStackOrder() != -1))
		{
			switch (getEffectType())
			{
				case Poison:
					damage = (poison[getStackOrder() - 1] * getPeriod()) / 1000;
					break;
				case Bleed:
					damage = (bleed[getStackOrder() - 1] * getPeriod()) / 1000;
					break;
				default:
					break;
			}
		}
		damage = _effector.calcStat(getSkill().isMagic() ? Stats.MAGIC_DAMAGE : Stats.PHYSICAL_DAMAGE, damage, _effected, getSkill());
		if ((damage > (_effected.getCurrentHp() - 1)) && !_effected.isNpc())
		{
			if (!getSkill().isOffensive())
			{
				_effected.sendPacket(Msg.NOT_ENOUGH_HP);
			}
			return false;
		}
		if(_effected.isNpc() && _effected.getAI().getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			NpcInstance npcAggro = (NpcInstance)_effected;
			npcAggro.getAggroList().addDamageHate(_effector, (int)damage, 200);
			npcAggro.setRunning();
			npcAggro.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, _effector);	
		}
		if (getSkill().getAbsorbPart() > 0)
		{
			_effector.setCurrentHp((getSkill().getAbsorbPart() * Math.min(_effected.getCurrentHp(), damage)) + _effector.getCurrentHp(), false);
		}
		_effected.reduceCurrentHp(damage, 0, _effector, getSkill(), !_effected.isNpc() && (_effected != _effector), _effected != _effector, _effector.isNpc() || (_effected == _effector), false, false, true, false);
		return true;
	}
}
