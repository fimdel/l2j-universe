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

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @version $Revision: 1.0 $
 */
public class KartiaGuard extends Fighter
{
	
	private long _ReuseTimer = 0;
	/**
	 * Field _followTask.
	 */
	ScheduledFuture<?> _followTask;
	Creature master = null;
	
	/**
	 * Constructor for FollowNpc.
	 * @param actor NpcInstance
	 */
	public KartiaGuard(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 9000;
	}
	
	/**
	 * Method canAttackCharacter.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean canAttackCharacter(Creature target)
	{
		return target.isMonster();
	}

	/**
	 * Method onEvtThink.
	 */
	@Override
	protected void onEvtThink()
	{
		final NpcInstance actor = getActor();
		if (master == null)
			master = getActor().getFollowTarget();
		//if master exit from instance delete me
		if (actor.getReflectionId() != master.getReflectionId())
			actor.deleteMe();
		//Check for Heal
		if (actor.getNpcId() == 33639 || actor.getNpcId() == 33628 || actor.getNpcId() == 33617)
		{
			if (master != null && !master.isDead() && master.getCurrentHpPercents() < 80)
			{
				if (!actor.isCastingNow() && (_ReuseTimer < System.currentTimeMillis()))
				{
					actor.doCast(SkillTable.getInstance().getInfo(698, 1), master, true);
					_ReuseTimer = System.currentTimeMillis() + (3 * 1000L);
				}
			}
		}
		//Check for Aggression
		if (actor.getNpcId() == 33609 || actor.getNpcId() == 33620 || actor.getNpcId() == 33631)
		{
			if (master != null && !master.isDead() && master.getTarget() != null)
			{
				if (!actor.isCastingNow() && (_ReuseTimer < System.currentTimeMillis()))
				{
					for (NpcInstance npc : actor.getAroundNpc(600, 100))
					{
						if (npc instanceof MonsterInstance)
						{
							if (npc.getTarget() != null && npc.getTarget().isPlayer())
							{
								actor.doCast(SkillTable.getInstance().getInfo(10060, 1), npc, true);							
								_ReuseTimer = System.currentTimeMillis() + (7 * 1000L);
							}
						}
					}
				}
			}
		}
		if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			//Check for Mobs to Attack
			int mobscount = 0;
			for (NpcInstance npc : actor.getAroundNpc(600, 100))
			{
				if (npc instanceof MonsterInstance)
				{
					actor.getAggroList().addDamageHate(npc, 10, 10);
					mobscount++;
				}
			}
			if (mobscount > 0 && !actor.getAggroList().isEmpty())
			{
				Attack(actor.getAggroList().getRandomHated(), true, false);
			}
			//Check for Follow
			else
			{
				if (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
				{
					setIntention(CtrlIntention.AI_INTENTION_FOLLOW);
				}
				if (master != null && actor.getDistance(master.getLoc()) > 300)
				{
					final Location loc = new Location(master.getX() + Rnd.get(-120, 120), master.getY() + Rnd.get(-120, 120), master.getZ());
					actor.followToCharacter(loc, master, Config.FOLLOW_RANGE, false);
					actor.setRunning();
				}
			}
		}
		super.onEvtThink();
	}
}
