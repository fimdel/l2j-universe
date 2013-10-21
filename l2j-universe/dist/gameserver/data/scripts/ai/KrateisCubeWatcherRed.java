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

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KrateisCubeWatcherRed extends DefaultAI
{
	/**
	 * Field SKILLS.
	 */
	private static final int[][] SKILLS =
	{
		{
			1064,
			14
		},
		{
			1160,
			15
		},
		{
			1164,
			19
		},
		{
			1167,
			6
		},
		{
			1168,
			7
		}
	};
	/**
	 * Field SKILL_CHANCE. (value is 25)
	 */
	private static final int SKILL_CHANCE = 25;
	
	/**
	 * Constructor for KrateisCubeWatcherRed.
	 * @param actor NpcInstance
	 */
	public KrateisCubeWatcherRed(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 3000;
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method onEvtThink.
	 */
	@Override
	protected void onEvtThink()
	{
		final NpcInstance actor = getActor();
		final List<Creature> around = World.getAroundCharacters(actor, 600, 300);
		if (around.isEmpty())
		{
			return;
		}
		for (Creature cha : around)
		{
			if (cha.isPlayer() && !cha.isDead() && Rnd.chance(SKILL_CHANCE))
			{
				int rnd = Rnd.get(SKILLS.length);
				Skill skill = SkillTable.getInstance().getInfo(SKILLS[rnd][0], SKILLS[rnd][1]);
				if (skill != null)
				{
					skill.getEffects(cha, cha, false, false);
				}
			}
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	public void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		super.onEvtDead(killer);
		actor.deleteMe();
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				final NpcTemplate template = NpcHolder.getInstance().getTemplate(18602);
				if (template != null)
				{
					final NpcInstance a = template.getNewInstance();
					a.setCurrentHpMp(a.getMaxHp(), a.getMaxMp());
					a.spawnMe(actor.getLoc());
				}
			}
		}, 10000L);
	}
}
