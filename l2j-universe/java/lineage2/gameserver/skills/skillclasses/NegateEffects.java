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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.skills.EffectType;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NegateEffects extends Skill
{
	/**
	 * Field _negateEffects.
	 */
	private final Map<EffectType, Integer> _negateEffects = new HashMap<>();
	/**
	 * Field _negateStackType.
	 */
	private final Map<String, Integer> _negateStackType = new HashMap<>();
	/**
	 * Field _onlyPhysical.
	 */
	private final boolean _onlyPhysical;
	/**
	 * Field _negateDebuffs.
	 */
	private final boolean _negateDebuffs;
	
	/**
	 * Constructor for NegateEffects.
	 * @param set StatsSet
	 */
	public NegateEffects(StatsSet set)
	{
		super(set);
		String[] negateEffectsString = set.getString("negateEffects", "").split(";");
		for (int i = 0; i < negateEffectsString.length; i++)
		{
			if (!negateEffectsString[i].isEmpty())
			{
				String[] entry = negateEffectsString[i].split(":");
				_negateEffects.put(Enum.valueOf(EffectType.class, entry[0]), entry.length > 1 ? Integer.decode(entry[1]) : Integer.MAX_VALUE);
			}
		}
		String[] negateStackTypeString = set.getString("negateStackType", "").split(";");
		for (int i = 0; i < negateStackTypeString.length; i++)
		{
			if (!negateStackTypeString[i].isEmpty())
			{
				String[] entry = negateStackTypeString[i].split(":");
				_negateStackType.put(entry[0], entry.length > 1 ? Integer.decode(entry[1]) : Integer.MAX_VALUE);
			}
		}
		_onlyPhysical = set.getBool("onlyPhysical", false);
		_negateDebuffs = set.getBool("negateDebuffs", true);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (!_negateDebuffs && !Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate()))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RESISTED_YOUR_S2).addString(target.getName()).addSkillName(getId(), getLevel()));
					continue;
				}
				if (!_negateEffects.isEmpty())
				{
					for (Map.Entry<EffectType, Integer> e : _negateEffects.entrySet())
					{
						negateEffectAtPower(target, e.getKey(), e.getValue().intValue());
					}
				}
				if (!_negateStackType.isEmpty())
				{
					for (Map.Entry<String, Integer> e : _negateStackType.entrySet())
					{
						negateEffectAtPower(target, e.getKey(), e.getValue().intValue());
					}
				}
				getEffects(activeChar, target, getActivateRate() > 0, false);
			}
		}
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
	
	/**
	 * Method negateEffectAtPower.
	 * @param target Creature
	 * @param type EffectType
	 * @param power int
	 */
	private void negateEffectAtPower(Creature target, EffectType type, int power)
	{
		for (Effect e : target.getEffectList().getAllEffects())
		{
			Skill skill = e.getSkill();
			if ((_onlyPhysical && skill.isMagic()) || !skill.isCancelable() || (skill.isOffensive() && !_negateDebuffs))
			{
				continue;
			}
			if (!skill.isOffensive() && (skill.getMagicLevel() > getMagicLevel()) && Rnd.chance(skill.getMagicLevel() - getMagicLevel()))
			{
				continue;
			}
			if ((e.getEffectType() == type) && (e.getStackOrder() <= power))
			{
				e.exit();
			}
		}
	}
	
	/**
	 * Method negateEffectAtPower.
	 * @param target Creature
	 * @param stackType String
	 * @param power int
	 */
	private void negateEffectAtPower(Creature target, String stackType, int power)
	{
		for (Effect e : target.getEffectList().getAllEffects())
		{
			Skill skill = e.getSkill();
			if ((_onlyPhysical && skill.isMagic()) || !skill.isCancelable() || (skill.isOffensive() && !_negateDebuffs))
			{
				continue;
			}
			if (!skill.isOffensive() && (skill.getMagicLevel() > getMagicLevel()) && Rnd.chance(skill.getMagicLevel() - getMagicLevel()))
			{
				continue;
			}
			if (e.checkStackType(stackType) && (e.getStackOrder() <= power))
			{
				e.exit();
			}
		}
	}
}
