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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NegateStats extends Skill
{
	/**
	 * Field _negateStats.
	 */
	private final List<Stats> _negateStats;
	/**
	 * Field _negateOffensive.
	 */
	private final boolean _negateOffensive;
	/**
	 * Field _negateCount.
	 */
	private final int _negateCount;
	
	/**
	 * Constructor for NegateStats.
	 * @param set StatsSet
	 */
	public NegateStats(StatsSet set)
	{
		super(set);
		String[] negateStats = set.getString("negateStats", "").split(" ");
		_negateStats = new ArrayList<>(negateStats.length);
		for (String stat : negateStats)
		{
			if (!stat.isEmpty())
			{
				_negateStats.add(Stats.valueOfXml(stat));
			}
		}
		_negateOffensive = set.getBool("negateDebuffs", false);
		_negateCount = set.getInteger("negateCount", 0);
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
				if (!_negateOffensive && !Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate()))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.C1_HAS_RESISTED_YOUR_S2).addString(target.getName()).addSkillName(getId(), getLevel()));
					continue;
				}
				int count = 0;
				List<Effect> effects = target.getEffectList().getAllEffects();
				for (Stats stat : _negateStats)
				{
					for (Effect e : effects)
					{
						Skill skill = e.getSkill();
						if (!skill.isOffensive() && (skill.getMagicLevel() > getMagicLevel()) && Rnd.chance(skill.getMagicLevel() - getMagicLevel()))
						{
							count++;
							continue;
						}
						if ((skill.isOffensive() == _negateOffensive) && containsStat(e, stat) && skill.isCancelable())
						{
							target.sendPacket(new SystemMessage(SystemMessage.THE_EFFECT_OF_S1_HAS_BEEN_REMOVED).addSkillName(e.getSkill().getId(), e.getSkill().getDisplayLevel()));
							e.exit();
							count++;
						}
						if ((_negateCount > 0) && (count >= _negateCount))
						{
							break;
						}
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
	 * Method containsStat.
	 * @param e Effect
	 * @param stat Stats
	 * @return boolean
	 */
	private boolean containsStat(Effect e, Stats stat)
	{
		for (FuncTemplate ft : e.getTemplate().getAttachedFuncs())
		{
			if (ft._stat == stat)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method isOffensive.
	 * @return boolean
	 */
	@Override
	public boolean isOffensive()
	{
		return !_negateOffensive;
	}
	
	/**
	 * Method getNegateStats.
	 * @return List<Stats>
	 */
	public List<Stats> getNegateStats()
	{
		return _negateStats;
	}
}
