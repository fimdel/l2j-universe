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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import bosses.AntharasManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Antharas extends DefaultAI
{
	/**
	 * Field s_paralyze. Field s_curse. Field s_fear2. Field s_fear.
	 */
	final Skill s_fear = getSkill(4108, 1), s_fear2 = getSkill(5092, 1), s_curse = getSkill(4109, 1), s_paralyze = getSkill(4111, 1);
	/**
	 * Field s_breath. Field s_meteor. Field s_antharas_ordinary_attack2. Field s_antharas_ordinary_attack. Field s_shock2. Field s_shock.
	 */
	final Skill s_shock = getSkill(4106, 1), s_shock2 = getSkill(4107, 1), s_antharas_ordinary_attack = getSkill(4112, 1), s_antharas_ordinary_attack2 = getSkill(4113, 1), s_meteor = getSkill(5093, 1), s_breath = getSkill(4110, 1);
	/**
	 * Field s_regen3. Field s_regen2. Field s_regen1.
	 */
	final Skill s_regen1 = getSkill(4239, 1), s_regen2 = getSkill(4240, 1), s_regen3 = getSkill(4241, 1);
	/**
	 * Field _hpStage.
	 */
	private int _hpStage = 0;
	/**
	 * Field _minionsSpawnDelay.
	 */
	private static long _minionsSpawnDelay = 0;
	/**
	 * Field minions.
	 */
	private final List<NpcInstance> minions = new ArrayList<>();
	
	/**
	 * Constructor for Antharas.
	 * @param actor NpcInstance
	 */
	public Antharas(NpcInstance actor)
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
		AntharasManager.setLastAttackTime();
		for (Playable p : AntharasManager.getZone().getInsidePlayables())
		{
			notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1);
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		_minionsSpawnDelay = System.currentTimeMillis() + 120000L;
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
			actor.altOnMagicUseTimer(actor, s_regen1);
			_hpStage = 1;
		}
		else if ((chp < 75) && (_hpStage == 1))
		{
			actor.altOnMagicUseTimer(actor, s_regen2);
			_hpStage = 2;
		}
		else if ((chp < 50) && (_hpStage == 2))
		{
			actor.altOnMagicUseTimer(actor, s_regen3);
			_hpStage = 3;
		}
		else if ((chp < 30) && (_hpStage == 3))
		{
			actor.altOnMagicUseTimer(actor, s_regen3);
			_hpStage = 4;
		}
		if ((_minionsSpawnDelay < System.currentTimeMillis()) && (getAliveMinionsCount() < 30) && Rnd.chance(5))
		{
			final NpcInstance minion = Functions.spawn(Location.findPointToStay(actor.getLoc(), 400, 700, actor.getGeoIndex()), Rnd.chance(50) ? 29190 : 29069);
			minions.add(minion);
			AntharasManager.addSpawnedMinion(minion);
		}
		if (Rnd.chance(50))
		{
			return chooseTaskAndTargets(Rnd.chance(50) ? s_antharas_ordinary_attack : s_antharas_ordinary_attack2, target, distance);
		}
		final Map<Skill, Integer> d_skill = new HashMap<>();
		switch (_hpStage)
		{
			case 1:
				addDesiredSkill(d_skill, target, distance, s_curse);
				addDesiredSkill(d_skill, target, distance, s_paralyze);
				addDesiredSkill(d_skill, target, distance, s_meteor);
				break;
			case 2:
				addDesiredSkill(d_skill, target, distance, s_curse);
				addDesiredSkill(d_skill, target, distance, s_paralyze);
				addDesiredSkill(d_skill, target, distance, s_meteor);
				addDesiredSkill(d_skill, target, distance, s_fear2);
				break;
			case 3:
				addDesiredSkill(d_skill, target, distance, s_curse);
				addDesiredSkill(d_skill, target, distance, s_paralyze);
				addDesiredSkill(d_skill, target, distance, s_meteor);
				addDesiredSkill(d_skill, target, distance, s_fear2);
				addDesiredSkill(d_skill, target, distance, s_shock2);
				addDesiredSkill(d_skill, target, distance, s_breath);
				break;
			case 4:
				addDesiredSkill(d_skill, target, distance, s_curse);
				addDesiredSkill(d_skill, target, distance, s_paralyze);
				addDesiredSkill(d_skill, target, distance, s_meteor);
				addDesiredSkill(d_skill, target, distance, s_fear2);
				addDesiredSkill(d_skill, target, distance, s_shock2);
				addDesiredSkill(d_skill, target, distance, s_fear);
				addDesiredSkill(d_skill, target, distance, s_shock);
				addDesiredSkill(d_skill, target, distance, s_breath);
				break;
			default:
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
