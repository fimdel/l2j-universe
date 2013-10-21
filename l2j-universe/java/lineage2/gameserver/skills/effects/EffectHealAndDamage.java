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
import java.util.List;

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectHealAndDamage extends Effect
{
	/**
	 * Constructor for EffectAddSkills.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectHealAndDamage(Env env, EffectTemplate template)
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
		List <Creature> targetsDamage = new ArrayList<Creature>();
		List <Creature> targetsHeal = new ArrayList<Creature>();
		getSkill().addTargetsToLakcis(targetsDamage, _effected, false);
		getSkill().addTargetsToLakcis(targetsHeal, _effected, true);
		for(Creature targetHeal : targetsHeal)
		{
			double hp = getSkill().getPower();
			hp = (hp*(targetHeal.calcStat(Stats.HEAL_EFFECTIVNESS, 100., _effected,getSkill()))) / 100;
			hp = Math.max(0, Math.min(hp, ((targetHeal.calcStat(Stats.HP_LIMIT, null, null) * targetHeal.getMaxHp()) / 100.) - targetHeal.getCurrentHp()));
			targetHeal.setCurrentHp(hp + targetHeal.getCurrentHp(), false);
			if(hp > 0)
			{
				if (_effected == targetHeal)
				{
					_effected.sendPacket(new SystemMessage(SystemMessage.S1_HPS_HAVE_BEEN_RESTORED).addNumber(Math.round(hp)));
				}
				else
				{
					getEffected().broadcastPacket(new MagicSkillUse(_effected, targetHeal, getSkill().getId(), getSkill().getLevel(), 0, 0));
					targetHeal.sendPacket(new SystemMessage(SystemMessage.XS2S_HP_HAS_BEEN_RESTORED_BY_S1).addString(_effected.getName()).addNumber(Math.round(hp)));
				}
			}
		}	
		for(Creature targetDamage : targetsDamage)
		{
			double damage = getSkill().getPower2();
			damage = targetDamage.calcStat(Stats.MAGIC_DAMAGE, damage, _effected, getSkill());
			if ((damage > (targetDamage.getCurrentHp() - 1)) && !targetDamage.isNpc())
			{
				return;
			}
			if(targetDamage.isNpc())
			{
				NpcInstance npcAggro = (NpcInstance)targetDamage;
				npcAggro.getAggroList().addDamageHate(_effected, (int)damage, 200);
				npcAggro.setRunning();
				npcAggro.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, _effected);				
			}
			targetDamage.reduceCurrentHp(damage, 0, _effected, getSkill(), false, false, targetDamage.isNpc(), false, false, true, false);
			getEffected().broadcastPacket(new MagicSkillUse(_effected, targetDamage, getSkill().getId(), getSkill().getLevel(), 0, 0));
		}
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
	}
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		return false;	
	}
}
