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
package ai.selmahum;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SocialAction;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DrillSergeant extends Fighter
{
	/**
	 * Field recruits.
	 */
	private static final int[] recruits =
	{
		22780,
		22782,
		22783,
		22784,
		22785
	};
	/**
	 * Field _wait_timeout.
	 */
	private long _wait_timeout = 0;
	
	/**
	 * Constructor for DrillSergeant.
	 * @param actor NpcInstance
	 */
	public DrillSergeant(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 1000;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	public boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (System.currentTimeMillis() > _wait_timeout)
		{
			_wait_timeout = System.currentTimeMillis() + (Rnd.get(10, 30) * 1000L);
			final List<NpcInstance> around = actor.getAroundNpc(700, 100);
			if ((around != null) && !around.isEmpty())
			{
				switch (Rnd.get(1, 3))
				{
					case 1:
						actor.broadcastPacket(new SocialAction(actor.getObjectId(), 7));
						for (NpcInstance mob : around)
						{
							if (ArrayUtils.contains(recruits, mob.getNpcId()))
							{
								mob.broadcastPacket(new SocialAction(mob.getObjectId(), 7));
							}
						}
						break;
					case 2:
						actor.broadcastPacket(new SocialAction(actor.getObjectId(), 7));
						for (NpcInstance mob : around)
						{
							if (ArrayUtils.contains(recruits, mob.getNpcId()))
							{
								mob.broadcastPacket(new SocialAction(mob.getObjectId(), 4));
							}
						}
						break;
					case 3:
						actor.broadcastPacket(new SocialAction(actor.getObjectId(), 7));
						for (NpcInstance mob : around)
						{
							if (ArrayUtils.contains(recruits, mob.getNpcId()))
							{
								mob.broadcastPacket(new SocialAction(mob.getObjectId(), 5));
							}
						}
						break;
				}
			}
		}
		return false;
	}
}
