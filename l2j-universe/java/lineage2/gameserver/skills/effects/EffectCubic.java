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
import java.util.Map;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.data.xml.holder.CubicHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.network.serverpackets.MagicSkillLaunched;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.templates.CubicTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EffectCubic extends Effect
{
	/**
	 * @author Mobius
	 */
	private class ActionTask extends RunnableImpl
	{
		/**
		 * Constructor for ActionTask.
		 */
		public ActionTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!isActive())
			{
				return;
			}
			Player player = (_effected != null) && _effected.isPlayer() ? (Player) _effected : null;
			if (player == null)
			{
				return;
			}
			doAction(player);
		}
	}
	
	/**
	 * Field _template.
	 */
	private final CubicTemplate _template;
	/**
	 * Field _task.
	 */
	private Future<?> _task = null;
	
	/**
	 * Constructor for EffectCubic.
	 * @param env Env
	 * @param template EffectTemplate
	 */
	public EffectCubic(Env env, EffectTemplate template)
	{
		super(env, template);
		_template = CubicHolder.getInstance().getTemplate(getTemplate().getParam().getInteger("cubicId"), getTemplate().getParam().getInteger("cubicLevel"));
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		Player player = _effected.getPlayer();
		if (player == null)
		{
			return;
		}
		player.addCubic(this);
		_task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new ActionTask(), 1000L, 1000L);
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		Player player = _effected.getPlayer();
		if (player == null)
		{
			return;
		}
		player.removeCubic(getId());
		_task.cancel(true);
		_task = null;
	}
	
	/**
	 * Method doAction.
	 * @param player Player
	 */
	public void doAction(Player player)
	{
		for (Map.Entry<Integer, List<CubicTemplate.SkillInfo>> entry : _template.getSkills())
		{
			if (Rnd.chance(entry.getKey()))
			{
				for (CubicTemplate.SkillInfo skillInfo : entry.getValue())
				{
					if (player.isSkillDisabled(skillInfo.getSkill()))
					{
						continue;
					}
					switch (skillInfo.getActionType())
					{
						case ATTACK:
							doAttack(player, skillInfo, _template.getDelay());
							break;
						case DEBUFF:
							doDebuff(player, skillInfo, _template.getDelay());
							break;
						case HEAL:
							doHeal(player, skillInfo, _template.getDelay());
							break;
						case CANCEL:
							doCancel(player, skillInfo, _template.getDelay());
							break;
					}
				}
				break;
			}
		}
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
	
	/**
	 * Method isHidden.
	 * @return boolean
	 */
	@Override
	public boolean isHidden()
	{
		return true;
	}
	
	/**
	 * Method isCancelable.
	 * @return boolean
	 */
	@Override
	public boolean isCancelable()
	{
		return false;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _template.getId();
	}
	
	/**
	 * Method doHeal.
	 * @param player Player
	 * @param info CubicTemplate.SkillInfo
	 * @param delay int
	 */
	private static void doHeal(final Player player, CubicTemplate.SkillInfo info, final int delay)
	{
		final Skill skill = info.getSkill();
		Creature target = null;
		if (player.getParty() == null)
		{
			if (!player.isCurrentHpFull() && !player.isDead())
			{
				target = player;
			}
		}
		else
		{
			double currentHp = Integer.MAX_VALUE;
			for (Player member : player.getParty().getPartyMembers())
			{
				if (member == null)
				{
					continue;
				}
				if (player.isInRange(member, info.getSkill().getCastRange()) && !member.isCurrentHpFull() && !member.isDead() && (member.getCurrentHp() < currentHp))
				{
					currentHp = member.getCurrentHp();
					target = member;
				}
			}
		}
		if (target == null)
		{
			return;
		}
		int chance = info.getChance((int) target.getCurrentHpPercents());
		if (!Rnd.chance(chance))
		{
			return;
		}
		final Creature aimTarget = target;
		player.broadcastPacket(new MagicSkillUse(player, aimTarget, skill.getDisplayId(), skill.getDisplayLevel(), skill.getHitTime(), 0));
		player.disableSkill(skill, delay * 1000L);
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				final List<Creature> targets = new ArrayList<>(1);
				targets.add(aimTarget);
				player.broadcastPacket(new MagicSkillLaunched(player.getObjectId(), skill.getDisplayId(), skill.getDisplayLevel(), targets));
				player.callSkill(skill, targets, false);
			}
		}, skill.getHitTime());
	}
	
	/**
	 * Method doAttack.
	 * @param player Player
	 * @param info CubicTemplate.SkillInfo
	 * @param delay int
	 */
	private static void doAttack(final Player player, final CubicTemplate.SkillInfo info, final int delay)
	{
		if (!Rnd.chance(info.getChance()))
		{
			return;
		}
		final Skill skill = info.getSkill();
		Creature target = null;
		if (player.isInCombat())
		{
			GameObject object = player.getTarget();
			target = (object != null) && object.isCreature() ? (Creature) object : null;
		}
		if ((target == null) || target.isDead() || (target.isDoor() && !info.isCanAttackDoor()) || !player.isInRangeZ(target, skill.getCastRange()) || !target.isAutoAttackable(player))
		{
			return;
		}
		final Creature aimTarget = target;
		player.broadcastPacket(new MagicSkillUse(player, target, skill.getDisplayId(), skill.getDisplayLevel(), skill.getHitTime(), 0));
		player.disableSkill(skill, delay * 1000L);
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				final List<Creature> targets = new ArrayList<>(1);
				targets.add(aimTarget);
				player.broadcastPacket(new MagicSkillLaunched(player.getObjectId(), skill.getDisplayId(), skill.getDisplayLevel(), targets));
				player.callSkill(skill, targets, false);
				if (aimTarget.isNpc())
				{
					if (aimTarget.paralizeOnAttack(player))
					{
						if (Config.PARALIZE_ON_RAID_DIFF)
						{
							player.paralizeMe(aimTarget);
						}
					}
					else
					{
						int damage = skill.getEffectPoint() != 0 ? skill.getEffectPoint() : (int) skill.getPower();
						aimTarget.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, player, damage);
					}
				}
			}
		}, skill.getHitTime());
	}
	
	/**
	 * Method doDebuff.
	 * @param player Player
	 * @param info CubicTemplate.SkillInfo
	 * @param delay int
	 */
	private static void doDebuff(final Player player, final CubicTemplate.SkillInfo info, final int delay)
	{
		if (!Rnd.chance(info.getChance()))
		{
			return;
		}
		final Skill skill = info.getSkill();
		Creature target = null;
		if (player.isInCombat())
		{
			GameObject object = player.getTarget();
			target = (object != null) && object.isCreature() ? (Creature) object : null;
		}
		if ((target == null) || target.isDead() || (target.isDoor() && !info.isCanAttackDoor()) || !player.isInRangeZ(target, skill.getCastRange()) || !target.isAutoAttackable(player))
		{
			return;
		}
		final Creature aimTarget = target;
		player.broadcastPacket(new MagicSkillUse(player, target, skill.getDisplayId(), skill.getDisplayLevel(), skill.getHitTime(), 0));
		player.disableSkill(skill, delay * 1000L);
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				final List<Creature> targets = new ArrayList<>(1);
				targets.add(aimTarget);
				player.broadcastPacket(new MagicSkillLaunched(player.getObjectId(), skill.getDisplayId(), skill.getDisplayLevel(), targets));
				final boolean succ = Formulas.calcSkillSuccess(player, aimTarget, skill, info.getChance());
				if (succ)
				{
					player.callSkill(skill, targets, false);
				}
				if (aimTarget.isNpc())
				{
					if (aimTarget.paralizeOnAttack(player))
					{
						if (Config.PARALIZE_ON_RAID_DIFF)
						{
							player.paralizeMe(aimTarget);
						}
					}
					else
					{
						int damage = skill.getEffectPoint() != 0 ? skill.getEffectPoint() : (int) skill.getPower();
						aimTarget.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, player, damage);
					}
				}
			}
		}, skill.getHitTime());
	}
	
	/**
	 * Method doCancel.
	 * @param player Player
	 * @param info CubicTemplate.SkillInfo
	 * @param delay int
	 */
	private static void doCancel(final Player player, final CubicTemplate.SkillInfo info, final int delay)
	{
		if (!Rnd.chance(info.getChance()))
		{
			return;
		}
		final Skill skill = info.getSkill();
		boolean hasDebuff = false;
		for (Effect e : player.getEffectList().getAllEffects())
		{
			if ((e != null) && e.isOffensive() && e.isCancelable() && !e.getTemplate()._applyOnCaster)
			{
				hasDebuff = true;
			}
		}
		if (!hasDebuff)
		{
			return;
		}
		player.broadcastPacket(new MagicSkillUse(player, player, skill.getDisplayId(), skill.getDisplayLevel(), skill.getHitTime(), 0));
		player.disableSkill(skill, delay * 1000L);
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				final List<Creature> targets = new ArrayList<>(1);
				targets.add(player);
				player.broadcastPacket(new MagicSkillLaunched(player.getObjectId(), skill.getDisplayId(), skill.getDisplayLevel(), targets));
				player.callSkill(skill, targets, false);
			}
		}, skill.getHitTime());
	}
}
