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
package ai.hellbound;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.TrapInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Darnel extends DefaultAI
{
	/**
	 * @author Mobius
	 */
	private class TrapTask extends RunnableImpl
	{
		/**
		 * Constructor for TrapTask.
		 */
		TrapTask()
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
			if (actor.isDead())
			{
				return;
			}
			TrapInstance trap;
			for (int i = 0; i < 10; i++)
			{
				trap = new TrapInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(13037), actor, trapSkills[Rnd.get(trapSkills.length)], new Location(Rnd.get(151896, 153608), Rnd.get(145032, 146808), -12584));
				trap.spawnMe();
			}
		}
	}
	
	/**
	 * Field trapSkills.
	 */
	final Skill[] trapSkills = new Skill[]
	{
		SkillTable.getInstance().getInfo(5267, 1),
		SkillTable.getInstance().getInfo(5268, 1),
		SkillTable.getInstance().getInfo(5269, 1),
		SkillTable.getInstance().getInfo(5270, 1)
	};
	/**
	 * Field Poison.
	 */
	final Skill Poison;
	/**
	 * Field Paralysis.
	 */
	final Skill Paralysis;
	
	/**
	 * Constructor for Darnel.
	 * @param actor NpcInstance
	 */
	public Darnel(NpcInstance actor)
	{
		super(actor);
		final TIntObjectHashMap<Skill> skills = getActor().getTemplate().getSkills();
		Poison = skills.get(4182);
		Paralysis = skills.get(4189);
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		final Creature target = prepareTarget();
		if (target == null)
		{
			return false;
		}
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		final int rnd_per = Rnd.get(100);
		if (rnd_per < 5)
		{
			actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 5440, 1, 3000, 0));
			ThreadPoolManager.getInstance().schedule(new TrapTask(), 3000);
			return true;
		}
		final double distance = actor.getDistance(target);
		if (!actor.isAMuted() && (rnd_per < 75))
		{
			return chooseTaskAndTargets(null, target, distance);
		}
		final Map<Skill, Integer> d_skill = new HashMap<>();
		addDesiredSkill(d_skill, target, distance, Poison);
		addDesiredSkill(d_skill, target, distance, Paralysis);
		final Skill r_skill = selectTopSkill(d_skill);
		return chooseTaskAndTargets(r_skill, target, distance);
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
