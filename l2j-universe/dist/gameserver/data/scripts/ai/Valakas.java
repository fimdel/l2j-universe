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
package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import bosses.ValakasManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Valakas extends DefaultAI
{
	/**
	 * Field s_regen. Field s_berserk. Field s_defence_down. Field s_fear. Field s_lava_skin.
	 */
	final Skill s_lava_skin = getSkill(4680, 1), s_fear = getSkill(4689, 1), s_defence_down = getSkill(5864, 1), s_berserk = getSkill(5865, 1), s_regen = getSkill(4691, 1);
	/**
	 * Field s_breath_high. Field s_breath_low. Field s_meteor. Field s_tail_lash. Field s_tail_stomp_a. Field s_tremple_right. Field s_tremple_left.
	 */
	final Skill s_tremple_left = getSkill(4681, 1), s_tremple_right = getSkill(4682, 1), s_tail_stomp_a = getSkill(4685, 1), s_tail_lash = getSkill(4688, 1), s_meteor = getSkill(4690, 1), s_breath_low = getSkill(4683, 1), s_breath_high = getSkill(4684, 1);
	/**
	 * Field s_destroy_soul2. Field s_destroy_body2. Field s_destroy_soul. Field s_destroy_body.
	 */
	final Skill s_destroy_body = getSkill(5860, 1), s_destroy_soul = getSkill(5861, 1), s_destroy_body2 = getSkill(5862, 1), s_destroy_soul2 = getSkill(5863, 1);
	/**
	 * Field defenceDownTimer.
	 */
	private long defenceDownTimer = Long.MAX_VALUE;
	/**
	 * Field defenceDownReuse.
	 */
	private static final long defenceDownReuse = 120000L;
	/**
	 * Field _attacksIndex. Field _counterAttackIndex. Field _rangedAttacksIndex.
	 */
	private double _rangedAttacksIndex, _counterAttackIndex, _attacksIndex;
	/**
	 * Field _hpStage.
	 */
	private int _hpStage = 0;
	/**
	 * Field minions.
	 */
	private final List<NpcInstance> minions = new ArrayList<>();
	
	/**
	 * Constructor for Valakas.
	 * @param actor NpcInstance
	 */
	public Valakas(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		ValakasManager.setLastAttackTime();
		for (Playable p : ValakasManager.getZone().getInsidePlayables())
		{
			notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1);
		}
		if (damage > 100)
		{
			if (attacker.getDistance(actor) > 400)
			{
				_rangedAttacksIndex += damage / 1000D;
			}
			else
			{
				_counterAttackIndex += damage / 1000D;
			}
		}
		_attacksIndex += damage / 1000D;
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		Creature target = prepareTarget();
		if (target == null)
		{
			return false;
		}
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		final double distance = actor.getDistance(target);
		final double chp = actor.getCurrentHpPercents();
		if (_hpStage == 0)
		{
			actor.altOnMagicUseTimer(actor, getSkill(4691, 1));
			_hpStage = 1;
		}
		else if ((chp < 80) && (_hpStage == 1))
		{
			actor.altOnMagicUseTimer(actor, getSkill(4691, 2));
			defenceDownTimer = System.currentTimeMillis();
			_hpStage = 2;
		}
		else if ((chp < 50) && (_hpStage == 2))
		{
			actor.altOnMagicUseTimer(actor, getSkill(4691, 3));
			_hpStage = 3;
		}
		else if ((chp < 30) && (_hpStage == 3))
		{
			actor.altOnMagicUseTimer(actor, getSkill(4691, 4));
			_hpStage = 4;
		}
		else if ((chp < 10) && (_hpStage == 4))
		{
			actor.altOnMagicUseTimer(actor, getSkill(4691, 5));
			_hpStage = 5;
		}
		if ((getAliveMinionsCount() < 100) && Rnd.chance(5))
		{
			final NpcInstance minion = Functions.spawn(Location.findPointToStay(actor.getLoc(), 400, 700, actor.getGeoIndex()), 29029);
			minions.add(minion);
			ValakasManager.addValakasMinion(minion);
		}
		if (_counterAttackIndex > 2000)
		{
			ValakasManager.broadcastScreenMessage(NpcString.VALAKAS_HEIGHTENED_BY_COUNTERATTACKS);
			_counterAttackIndex = 0;
			return chooseTaskAndTargets(s_berserk, actor, 0);
		}
		else if (_rangedAttacksIndex > 2000)
		{
			if (Rnd.chance(60))
			{
				final Creature randomHated = actor.getAggroList().getRandomHated();
				if (randomHated != null)
				{
					setAttackTarget(randomHated);
					actor.startConfused();
					ThreadPoolManager.getInstance().schedule(new RunnableImpl()
					{
						@Override
						public void runImpl()
						{
							final NpcInstance actor = getActor();
							if (actor != null)
							{
								actor.stopConfused();
							}
							_madnessTask = null;
						}
					}, 20000L);
				}
				ValakasManager.broadcastScreenMessage(NpcString.VALAKAS_RANGED_ATTACKS_ENRAGED_TARGET_FREE);
				_rangedAttacksIndex = 0;
			}
			else
			{
				ValakasManager.broadcastScreenMessage(NpcString.VALAKAS_RANGED_ATTACKS_PROVOKED);
				_rangedAttacksIndex = 0;
				return chooseTaskAndTargets(s_berserk, actor, 0);
			}
		}
		else if (_attacksIndex > 3000)
		{
			ValakasManager.broadcastScreenMessage(NpcString.VALAKAS_PDEF_ISM_DECREACED_SLICED_DASH);
			_attacksIndex = 0;
			return chooseTaskAndTargets(s_defence_down, actor, 0);
		}
		else if (defenceDownTimer < System.currentTimeMillis())
		{
			ValakasManager.broadcastScreenMessage(NpcString.VALAKAS_FINDS_YOU_ATTACKS_ANNOYING_SILENCE);
			defenceDownTimer = System.currentTimeMillis() + defenceDownReuse + (Rnd.get(60) * 1000L);
			return chooseTaskAndTargets(s_fear, target, distance);
		}
		if (Rnd.chance(50))
		{
			return chooseTaskAndTargets(Rnd.chance(50) ? s_tremple_left : s_tremple_right, target, distance);
		}
		final Map<Skill, Integer> d_skill = new HashMap<>();
		switch (_hpStage)
		{
			case 1:
				addDesiredSkill(d_skill, target, distance, s_breath_low);
				addDesiredSkill(d_skill, target, distance, s_tail_stomp_a);
				addDesiredSkill(d_skill, target, distance, s_meteor);
				addDesiredSkill(d_skill, target, distance, s_fear);
				break;
			case 2:
			case 3:
				addDesiredSkill(d_skill, target, distance, s_breath_low);
				addDesiredSkill(d_skill, target, distance, s_tail_stomp_a);
				addDesiredSkill(d_skill, target, distance, s_breath_high);
				addDesiredSkill(d_skill, target, distance, s_tail_lash);
				addDesiredSkill(d_skill, target, distance, s_destroy_body);
				addDesiredSkill(d_skill, target, distance, s_destroy_soul);
				addDesiredSkill(d_skill, target, distance, s_meteor);
				addDesiredSkill(d_skill, target, distance, s_fear);
				break;
			case 4:
			case 5:
				addDesiredSkill(d_skill, target, distance, s_breath_low);
				addDesiredSkill(d_skill, target, distance, s_tail_stomp_a);
				addDesiredSkill(d_skill, target, distance, s_breath_high);
				addDesiredSkill(d_skill, target, distance, s_tail_lash);
				addDesiredSkill(d_skill, target, distance, s_destroy_body);
				addDesiredSkill(d_skill, target, distance, s_destroy_soul);
				addDesiredSkill(d_skill, target, distance, s_meteor);
				addDesiredSkill(d_skill, target, distance, s_fear);
				addDesiredSkill(d_skill, target, distance, Rnd.chance(60) ? s_destroy_soul2 : s_destroy_body2);
				break;
		}
		final Skill r_skill = selectTopSkill(d_skill);
		if ((r_skill != null) && !r_skill.isOffensive())
		{
			target = actor;
		}
		return chooseTaskAndTargets(r_skill, target, distance);
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		final NpcInstance actor = getActor();
		if (actor.isInZone(Zone.ZoneType.poison))
		{
			if ((actor.getEffectList() != null) && (actor.getEffectList().getEffectsBySkill(s_lava_skin) == null))
			{
				actor.altOnMagicUseTimer(actor, s_lava_skin);
			}
		}
		super.thinkAttack();
	}
	
	/**
	 * Method getSkill.
	 * @param id int
	 * @param level int
	 * @return Skill
	 */
	private Skill getSkill(int id, int level)
	{
		return SkillTable.getInstance().getInfo(id, level);
	}
	
	/**
	 * Method getAliveMinionsCount.
	 * @return int
	 */
	private int getAliveMinionsCount()
	{
		int i = 0;
		for (NpcInstance n : minions)
		{
			if ((n != null) && !n.isDead())
			{
				i++;
			}
		}
		return i;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		if ((minions != null) && !minions.isEmpty())
		{
			for (NpcInstance n : minions)
			{
				n.deleteMe();
			}
		}
		super.onEvtDead(killer);
	}
}
