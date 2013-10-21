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
package ai.custom;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.ReflectionBossInstance;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LabyrinthLostWatcher extends Fighter
{
	/**
	 * Constructor for LabyrinthLostWatcher.
	 * @param actor NpcInstance
	 */
	public LabyrinthLostWatcher(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		final Reflection r = actor.getReflection();
		if (!r.isDefault())
		{
			if (checkMates(actor.getNpcId()))
			{
				if (findLostCaptain() != null)
				{
					findLostCaptain().addStatFunc(new FuncSet(Stats.POWER_DEFENCE, 0x30, this, findLostCaptain().getTemplate().getBasePDef() * 0.66));
				}
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method checkMates.
	 * @param id int
	 * @return boolean
	 */
	private boolean checkMates(int id)
	{
		for (NpcInstance n : getActor().getReflection().getNpcs())
		{
			if ((n.getNpcId() == id) && !n.isDead())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method findLostCaptain.
	 * @return NpcInstance
	 */
	private NpcInstance findLostCaptain()
	{
		for (NpcInstance n : getActor().getReflection().getNpcs())
		{
			if (n instanceof ReflectionBossInstance)
			{
				return n;
			}
		}
		return null;
	}
}
