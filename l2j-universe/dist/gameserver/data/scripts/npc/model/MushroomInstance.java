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
package npc.model;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MushroomInstance extends MonsterInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field FANTASY_MUSHROOM. (value is 18864)
	 */
	private static final int FANTASY_MUSHROOM = 18864;
	/**
	 * Field FANTASY_MUSHROOM_SKILL. (value is 6427)
	 */
	private static final int FANTASY_MUSHROOM_SKILL = 6427;
	/**
	 * Field RAINBOW_FROG. (value is 18866)
	 */
	private static final int RAINBOW_FROG = 18866;
	/**
	 * Field RAINBOW_FROG_SKILL. (value is 6429)
	 */
	private static final int RAINBOW_FROG_SKILL = 6429;
	/**
	 * Field STICKY_MUSHROOM. (value is 18865)
	 */
	private static final int STICKY_MUSHROOM = 18865;
	/**
	 * Field STICKY_MUSHROOM_SKILL. (value is 6428)
	 */
	private static final int STICKY_MUSHROOM_SKILL = 6428;
	/**
	 * Field ENERGY_PLANT. (value is 18868)
	 */
	private static final int ENERGY_PLANT = 18868;
	/**
	 * Field ENERGY_PLANT_SKILL. (value is 6430)
	 */
	private static final int ENERGY_PLANT_SKILL = 6430;
	/**
	 * Field ABYSS_WEED. (value is 18867)
	 */
	private static final int ABYSS_WEED = 18867;
	
	/**
	 * Constructor for MushroomInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MushroomInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method canChampion.
	 * @return boolean
	 */
	@Override
	public boolean canChampion()
	{
		return false;
	}
	
	/**
	 * Method reduceCurrentHp.
	 * @param i double
	 * @param reflectableDamage double
	 * @param attacker Creature
	 * @param skill Skill
	 * @param awake boolean
	 * @param standUp boolean
	 * @param directHp boolean
	 * @param canReflect boolean
	 * @param transferDamage boolean
	 * @param isDot boolean
	 * @param sendMessage boolean
	 */
	@Override
	public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage)
	{
		if (isDead())
		{
			return;
		}
		Creature killer = attacker;
		if (killer.isPet() || killer.isServitor())
		{
			killer = killer.getPlayer();
		}
		if (getNpcId() == RAINBOW_FROG)
		{
			ThreadPoolManager.getInstance().schedule(new TaskAfterDead(this, killer, RAINBOW_FROG_SKILL), 3000);
			doDie(killer);
		}
		else if (getNpcId() == STICKY_MUSHROOM)
		{
			ThreadPoolManager.getInstance().schedule(new TaskAfterDead(this, killer, STICKY_MUSHROOM_SKILL), 3000);
			doDie(killer);
		}
		else if (getNpcId() == ENERGY_PLANT)
		{
			ThreadPoolManager.getInstance().schedule(new TaskAfterDead(this, killer, ENERGY_PLANT_SKILL), 3000);
			doDie(killer);
		}
		else if (getNpcId() == ABYSS_WEED)
		{
			doDie(killer);
		}
		else if (getNpcId() == FANTASY_MUSHROOM)
		{
			List<NpcInstance> around = getAroundNpc(700, 300);
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if (npc.isMonster() && (npc.getNpcId() >= 22768) && (npc.getNpcId() <= 22774))
					{
						npc.setRunning();
						npc.moveToLocation(Location.findPointToStay(this, 20, 50), 0, true);
					}
				}
			}
			ThreadPoolManager.getInstance().schedule(new TaskAfterDead(this, killer, FANTASY_MUSHROOM_SKILL), 4000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class TaskAfterDead extends RunnableImpl
	{
		/**
		 * Field _actor.
		 */
		private final NpcInstance _actor;
		/**
		 * Field _killer.
		 */
		private final Creature _killer;
		/**
		 * Field _skill.
		 */
		private final Skill _skill;
		
		/**
		 * Constructor for TaskAfterDead.
		 * @param actor NpcInstance
		 * @param killer Creature
		 * @param skillId int
		 */
		public TaskAfterDead(NpcInstance actor, Creature killer, int skillId)
		{
			_actor = actor;
			_killer = killer;
			_skill = SkillTable.getInstance().getInfo(skillId, 1);
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_skill == null)
			{
				return;
			}
			if ((_actor != null) && (_actor.getNpcId() == FANTASY_MUSHROOM))
			{
				_actor.broadcastPacket(new MagicSkillUse(_actor, _actor, _skill.getId(), _skill.getLevel(), 0, 0));
				List<NpcInstance> around = _actor.getAroundNpc(200, 300);
				if ((around != null) && !around.isEmpty())
				{
					for (NpcInstance npc : around)
					{
						if (npc.isMonster() && (npc.getNpcId() >= 22768) && (npc.getNpcId() <= 22774))
						{
							_skill.getEffects(npc, npc, false, false);
						}
					}
				}
				_actor.doDie(_killer);
				return;
			}
			if ((_killer != null) && _killer.isPlayer() && !_killer.isDead())
			{
				List<Creature> targets = new ArrayList<>();
				targets.add(_killer);
				_killer.broadcastPacket(new MagicSkillUse(_killer, _killer, _skill.getId(), _skill.getLevel(), 0, 0));
				_skill.useSkill(_killer, targets);
			}
		}
	}
}
