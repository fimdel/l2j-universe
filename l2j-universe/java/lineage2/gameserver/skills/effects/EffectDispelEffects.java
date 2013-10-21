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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectDispelEffects extends Effect
{
	/**
	 * Field _dispelType.
	 */
	private final String _dispelType;
	/**
	 * Field _cancelRate.
	 */
	private final int _cancelRate;
	/**
	 * Field _stackTypes.
	 */
	private final String[] _stackTypes;
	/**
	 * Field _negateCount.
	 */
	private final int _negateCount;
	
	/**
	 * Constructor for EffectDispelEffects.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectDispelEffects(Env env, EffectTemplate template)
	{
		super(env, template);
		_dispelType = template.getParam().getString("dispelType", "");
		_cancelRate = template.getParam().getInteger("cancelRate", 0);
		_negateCount = template.getParam().getInteger("negateCount", 5);
		_stackTypes = template.getParam().getString("negateStackTypes", "").split(";");
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		List<Effect> _musicList = new ArrayList<>();
		List<Effect> _buffList = new ArrayList<>();
		for (Effect e : _effected.getEffectList().getAllEffects())
		{
			if (_dispelType.equals("cancellation"))
			{
				if (!e.isOffensive() && !e.getSkill().isToggle() && e.isCancelable())
				{
					if (e.getSkill().isMusic())
					{
						_musicList.add(e);
					}
					else
					{
						_buffList.add(e);
					}
				}
			}
			else if (_dispelType.equals("bane"))
			{
				if (!e.isOffensive() && ArrayUtils.contains(_stackTypes, e.getStackType()) && e.isCancelable())
				{
					_buffList.add(e);
				}
			}
			else if (_dispelType.equals("cleanse"))
			{
				if (e.isOffensive() && e.isCancelable())
				{
					_buffList.add(e);
				}
			}
		}
		List<Effect> _effectList = new ArrayList<>();
		Collections.reverse(_musicList);
		Collections.reverse(_buffList);
		_effectList.addAll(_musicList);
		_effectList.addAll(_buffList);
		if (_effectList.isEmpty())
		{
			return;
		}
		double prelimChance, eml, dml, cancel_res_multiplier = _effected.calcStat(Stats.CANCEL_RESIST, 0, null, null);
		int buffTime, negated = 0;
		for (Effect e : _effectList)
		{
			if (negated < _negateCount)
			{
				eml = e.getSkill().getMagicLevel();
				dml = getSkill().getMagicLevel() - (eml == 0 ? _effected.getLevel() : eml);
				buffTime = e.getTimeLeft();
				cancel_res_multiplier = 1 - (cancel_res_multiplier * .01);
				prelimChance = ((2. * dml) + _cancelRate + (buffTime / 120)) * cancel_res_multiplier;
				if (Rnd.chance(calcSkillChanceLimits(prelimChance, _effector.isPlayable())))
				{
					negated++;
					_effected.sendPacket(new SystemMessage2(SystemMsg.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(e.getSkill().getId(), e.getSkill().getLevel()));
					e.exit();
				}
			}
		}
	}
	
	/**
	 * Method calcSkillChanceLimits.
	 * @param prelimChance double
	 * @param isPlayable boolean
	 * @return double
	 */
	private double calcSkillChanceLimits(double prelimChance, boolean isPlayable)
	{
		if (_dispelType.equals("bane"))
		{
			if (prelimChance < 40)
			{
				return 40;
			}
			else if (prelimChance > 90)
			{
				return 90;
			}
		}
		else if (_dispelType.equals("cancellation"))
		{
			if (prelimChance < 25)
			{
				return 25;
			}
			else if (prelimChance > 75)
			{
				return 75;
			}
		}
		else if (_dispelType.equals("cleanse"))
		{
			return _cancelRate;
		}
		return prelimChance;
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	protected boolean onActionTime()
	{
		return false;
	}
}
