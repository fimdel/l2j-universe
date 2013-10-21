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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Baylor extends DefaultAI
{
	/**
	 * Field Berserk.
	 */
	final Skill Berserk;
	/**
	 * Field Invincible.
	 */
	final Skill Invincible;
	/**
	 * Field Imprison.
	 */
	final Skill Imprison;
	/**
	 * Field GroundStrike.
	 */
	final Skill GroundStrike;
	/**
	 * Field JumpAttack.
	 */
	final Skill JumpAttack;
	/**
	 * Field StrongPunch.
	 */
	final Skill StrongPunch;
	/**
	 * Field Stun1.
	 */
	final Skill Stun1;
	/**
	 * Field Stun2.
	 */
	final Skill Stun2;
	/**
	 * Field Stun3.
	 */
	final Skill Stun3;
	/**
	 * Field PresentationBalor2.
	 */
	static final int PresentationBalor2 = 5402;
	/**
	 * Field PresentationBalor3.
	 */
	static final int PresentationBalor3 = 5403;
	/**
	 * Field PresentationBalor4.
	 */
	static final int PresentationBalor4 = 5404;
	/**
	 * Field PresentationBalor10.
	 */
	static final int PresentationBalor10 = 5410;
	/**
	 * Field PresentationBalor11.
	 */
	static final int PresentationBalor11 = 5411;
	/**
	 * Field PresentationBalor12.
	 */
	static final int PresentationBalor12 = 5412;
	/**
	 * Field Water_Dragon_Claw. (value is 2360)
	 */
	static private final int Water_Dragon_Claw = 2360;
	/**
	 * Field _isUsedInvincible.
	 */
	private boolean _isUsedInvincible = false;
	/**
	 * Field _claw_count.
	 */
	private int _claw_count = 0;
	/**
	 * Field _last_claw_time.
	 */
	private long _last_claw_time = 0;
	
	/**
	 * @author Mobius
	 */
	private class SpawnSocial extends RunnableImpl
	{
		/**
		 * Constructor for SpawnSocial.
		 */
		SpawnSocial()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance actor = getActor();
			if (actor != null)
			{
				actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, PresentationBalor2, 1, 4000, 0));
			}
		}
	}
	
	/**
	 * Constructor for Baylor.
	 * @param actor NpcInstance
	 */
	public Baylor(NpcInstance actor)
	{
		super(actor);
		final TIntObjectHashMap<Skill> skills = getActor().getTemplate().getSkills();
		Berserk = skills.get(5224);
		Invincible = skills.get(5225);
		Imprison = skills.get(5226);
		GroundStrike = skills.get(5227);
		JumpAttack = skills.get(5228);
		StrongPunch = skills.get(5229);
		Stun1 = skills.get(5230);
		Stun2 = skills.get(5231);
		Stun3 = skills.get(5232);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		ThreadPoolManager.getInstance().schedule(new SpawnSocial(), 20000);
		super.onEvtSpawn();
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final NpcInstance actor = getActor();
		if (actor.isDead() || (skill == null) || (caster == null))
		{
			return;
		}
		if ((System.currentTimeMillis() - _last_claw_time) > 5000)
		{
			_claw_count = 0;
		}
		if (skill.getId() == Water_Dragon_Claw)
		{
			_claw_count++;
			_last_claw_time = System.currentTimeMillis();
		}
		final Player player = caster.getPlayer();
		if (player == null)
		{
			return;
		}
		int count = 1;
		final Party party = player.getParty();
		if (party != null)
		{
			count = party.getMemberCount();
		}
		if (_claw_count >= count)
		{
			_claw_count = 0;
			actor.getEffectList().stopEffect(Invincible);
			Functions.npcSay(actor, "Да как вы по�?мели! Я непоб��дим!!!");
		}
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
		final double actor_hp_precent = actor.getCurrentHpPercents();
		if ((actor_hp_precent < 30) && !_isUsedInvincible)
		{
			_isUsedInvincible = true;
			addTaskBuff(actor, Invincible);
			Functions.npcSay(actor, "�?хаха! Тепер�? вы в�?е умрете.");
			return true;
		}
		final int rnd_per = Rnd.get(100);
		if ((rnd_per < 7) && (actor.getEffectList().getEffectsBySkill(Berserk) == null))
		{
			addTaskBuff(actor, Berserk);
			Functions.npcSay(actor, "Beleth, дай мне �?илу!");
			return true;
		}
		if ((rnd_per < 15) || ((rnd_per < 33) && (actor.getEffectList().getEffectsBySkill(Berserk) != null)))
		{
			return chooseTaskAndTargets(StrongPunch, target, distance);
		}
		if (!actor.isAMuted() && (rnd_per < 50))
		{
			return chooseTaskAndTargets(null, target, distance);
		}
		final Map<Skill, Integer> skills = new HashMap<>();
		addDesiredSkill(skills, target, distance, GroundStrike);
		addDesiredSkill(skills, target, distance, JumpAttack);
		addDesiredSkill(skills, target, distance, StrongPunch);
		addDesiredSkill(skills, target, distance, Stun1);
		addDesiredSkill(skills, target, distance, Stun2);
		addDesiredSkill(skills, target, distance, Stun3);
		final Skill skill = selectTopSkill(skills);
		if ((skill != null) && !skill.isOffensive())
		{
			target = actor;
		}
		return chooseTaskAndTargets(skill, target, distance);
	}
	
	/**
	 * Method maybeMoveToHome.
	 * @return boolean
	 */
	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		if (HellboundManager.getConfidence() < 1)
		{
			HellboundManager.setConfidence(1);
		}
		super.onEvtDead(killer);
	}
}
