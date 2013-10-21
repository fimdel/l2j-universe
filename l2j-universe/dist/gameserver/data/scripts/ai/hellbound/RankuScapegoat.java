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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RankuScapegoat extends DefaultAI
{
	/**
	 * Field Eidolon_ID. (value is 25543)
	 */
	private static final int Eidolon_ID = 25543;
	
	/**
	 * Constructor for RankuScapegoat.
	 * @param actor NpcInstance
	 */
	public RankuScapegoat(NpcInstance actor)
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
		final NpcInstance mob = actor.getReflection().addSpawnWithoutRespawn(Eidolon_ID, actor.getLoc(), 0);
		final NpcInstance boss = getBoss();
		if ((mob != null) && (boss != null))
		{
			final Creature cha = boss.getAggroList().getTopDamager();
			if (cha != null)
			{
				mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, cha, 100000);
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method getBoss.
	 * @return NpcInstance
	 */
	private NpcInstance getBoss()
	{
		final Reflection r = getActor().getReflection();
		if (!r.isDefault())
		{
			for (NpcInstance n : r.getNpcs())
			{
				if ((n.getNpcId() == 25542) && !n.isDead())
				{
					return n;
				}
			}
		}
		return null;
	}
}
