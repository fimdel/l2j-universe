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
package events.TheFlowOfTheHorror;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncMul;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GilmoreAI extends Fighter
{
	/**
	 * Field points_stage1.
	 */
	static final Location[] points_stage1 =
	{
		new Location(73195, 118483, -3722),
		new Location(73535, 117945, -3754),
		new Location(73446, 117334, -3752),
		new Location(72847, 117311, -3711),
		new Location(72296, 117720, -3694),
		new Location(72463, 118401, -3694),
		new Location(72912, 117895, -3723)
	};
	/**
	 * Field points_stage2.
	 */
	static final Location[] points_stage2 =
	{
		new Location(73615, 117629, -3765)
	};
	/**
	 * Field text_stage1.
	 */
	static final String[] text_stage1 =
	{
		"Text1",
		"Text2",
		"Text3",
		"Text4",
		"Text5",
		"Text6",
		"Text7"
	};
	/**
	 * Field text_stage2.
	 */
	static final String[] text_stage2 =
	{
		"Готовы?",
		"�?ачнем, нел�?з�? тер�?т�? ни минуты!"
	};
	/**
	 * Field wait_timeout.
	 */
	private long wait_timeout = 0;
	/**
	 * Field wait.
	 */
	private boolean wait = false;
	/**
	 * Field index.
	 */
	private int index;
	/**
	 * Field step_stage2.
	 */
	private int step_stage2 = 1;
	
	/**
	 * Constructor for GilmoreAI.
	 * @param actor NpcInstance
	 */
	public GilmoreAI(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 250;
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((actor == null) || actor.isDead())
		{
			return true;
		}
		if (_def_think)
		{
			doTask();
			return true;
		}
		if (System.currentTimeMillis() > wait_timeout)
		{
			if (!wait)
			{
				switch (TheFlowOfTheHorror.getStage())
				{
					case 1:
						if (Rnd.chance(30))
						{
							index = Rnd.get(text_stage1.length);
							Functions.npcSay(actor, text_stage1[index]);
							wait_timeout = System.currentTimeMillis() + 10000;
							wait = true;
							return true;
						}
						break;
					case 2:
						switch (step_stage2)
						{
							case 1:
								Functions.npcSay(actor, text_stage2[0]);
								wait_timeout = System.currentTimeMillis() + 10000;
								wait = true;
								return true;
							case 2:
								break;
						}
						break;
				}
			}
			wait_timeout = 0;
			wait = false;
			actor.setRunning();
			switch (TheFlowOfTheHorror.getStage())
			{
				case 1:
					index = Rnd.get(points_stage1.length);
					addTaskMove(points_stage1[index], true);
					doTask();
					return true;
				case 2:
					switch (step_stage2)
					{
						case 1:
							Functions.npcSay(actor, text_stage2[1]);
							addTaskMove(points_stage2[0], true);
							doTask();
							step_stage2 = 2;
							return true;
						case 2:
							actor.setHeading(0);
							actor.stopMove();
							actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 454, 1, 3000, 0));
							step_stage2 = 3;
							return true;
						case 3:
							actor.addStatFunc(new FuncMul(Stats.MAGIC_ATTACK_SPEED, 0x40, actor, 5));
							actor.addStatFunc(new FuncMul(Stats.MAGIC_DAMAGE, 0x40, actor, 10));
							actor.addStatFunc(new FuncMul(Stats.PHYSICAL_DAMAGE, 0x40, actor, 10));
							actor.addStatFunc(new FuncMul(Stats.RUN_SPEED, 0x40, actor, 3));
							actor.addSkill(SkillTable.getInstance().getInfo(1467, 1));
							actor.sendChanges();
							step_stage2 = 4;
							return true;
						case 4:
							setIntention(CtrlIntention.AI_INTENTION_ATTACK, null);
							return true;
						case 10:
							actor.removeStatsOwner(this);
							step_stage2 = 11;
							return true;
					}
			}
		}
		return false;
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return true;
		}
		for (NpcInstance npc : World.getAroundNpc(actor, 1000, 200))
		{
			if (Rnd.chance(10) && (npc != null) && (npc.getNpcId() == 20235))
			{
				MonsterInstance monster = (MonsterInstance) npc;
				if (Rnd.chance(20))
				{
					addTaskCast(monster, actor.getKnownSkill(1467));
				}
				else
				{
					addTaskAttack(monster);
				}
				return true;
			}
		}
		return true;
	}
}
