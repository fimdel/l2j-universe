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
package ai.freya;

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Maguen extends Fighter
{
	/**
	 * Field maguenStatsSkills.
	 */
	private static final int[] maguenStatsSkills =
	{
		6343,
		6365,
		6366
	};
	/**
	 * Field maguenRaceSkills.
	 */
	private static final int[] maguenRaceSkills =
	{
		6367,
		6368,
		6369
	};
	
	/**
	 * Constructor for Maguen.
	 * @param actor NpcInstance
	 */
	public Maguen(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		ThreadPoolManager.getInstance().schedule(new Plasma(), 2000L);
		ThreadPoolManager.getInstance().schedule(new Despawn(), 10000L);
		final List<Creature> around = getActor().getAroundCharacters(800, 300);
		if (!getActor().isInZone(ZoneType.dummy) && (around != null) && !around.isEmpty())
		{
			final ExShowScreenMessage sm = new ExShowScreenMessage(NpcString.MAGUEN_APPEARANCE, 5000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true);
			for (Creature character : around)
			{
				if (character.isPlayer())
				{
					character.sendPacket(sm);
				}
			}
		}
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		if (skill.getId() != 9060)
		{
			return;
		}
		final NpcInstance actor = getActor();
		if (actor.isInZone(ZoneType.dummy))
		{
			switch (actor.getNpcState())
			{
				case 1:
					if (Rnd.chance(80))
					{
						actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[0], Rnd.get(2, 3)), caster, true);
					}
					else
					{
						actor.doCast(SkillTable.getInstance().getInfo(maguenStatsSkills[0], Rnd.get(1, 2)), caster, true);
					}
					break;
				case 2:
					if (Rnd.chance(80))
					{
						actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[1], Rnd.get(2, 3)), caster, true);
					}
					else
					{
						actor.doCast(SkillTable.getInstance().getInfo(maguenStatsSkills[1], Rnd.get(1, 2)), caster, true);
					}
					break;
				case 3:
					if (Rnd.chance(80))
					{
						actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[2], Rnd.get(2, 3)), caster, true);
					}
					else
					{
						actor.doCast(SkillTable.getInstance().getInfo(maguenStatsSkills[2], Rnd.get(1, 2)), caster, true);
					}
					break;
				default:
					break;
			}
		}
		else
		{
			switch (actor.getNpcState())
			{
				case 1:
					actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[0], 1), caster, true);
					break;
				case 2:
					actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[1], 1), caster, true);
					break;
				case 3:
					actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[2], 1), caster, true);
					break;
				default:
					break;
			}
		}
		getActor().setNpcState(4);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (attacker == null)
		{
			return;
		}
		if (attacker.isPlayable())
		{
			return;
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		if (target.isPlayable())
		{
			return false;
		}
		return super.checkAggression(target);
	}
	
	/**
	 * @author Mobius
	 */
	private class Plasma extends RunnableImpl
	{
		/**
		 * Constructor for Plasma.
		 */
		Plasma()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			getActor().setNpcState(Rnd.get(1, 3));
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class Despawn extends RunnableImpl
	{
		/**
		 * Constructor for Despawn.
		 */
		Despawn()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			getActor().setNpcState(4);
			getActor().doDie(null);
		}
	}
}
