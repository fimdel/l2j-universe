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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OriginalSinWarden extends Fighter
{
	/**
	 * Field servants1.
	 */
	private static final int[] servants1 =
	{
		22424,
		22425,
		22426,
		22427,
		22428,
		22429,
		22430
	};
	/**
	 * Field servants2.
	 */
	private static final int[] servants2 =
	{
		22432,
		22433,
		22434,
		22435,
		22436,
		22437,
		22438
	};
	/**
	 * Field DarionsFaithfulServants.
	 */
	private static final int[] DarionsFaithfulServants =
	{
		22405,
		22406,
		22407
	};
	
	/**
	 * Constructor for OriginalSinWarden.
	 * @param actor NpcInstance
	 */
	public OriginalSinWarden(NpcInstance actor)
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
		final NpcInstance actor = getActor();
		switch (actor.getNpcId())
		{
			case 22423:
			{
				for (int element : servants1)
				{
					try
					{
						actor.getLoc();
						SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(element));
						sp.setLoc(Location.findPointToStay(actor, 150, 350));
						sp.doSpawn(true);
						sp.stopRespawn();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				break;
			}
			case 22431:
			{
				for (int element : servants2)
				{
					try
					{
						actor.getLoc();
						SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(element));
						sp.setLoc(Location.findPointToStay(actor, 150, 350));
						sp.doSpawn(true);
						sp.stopRespawn();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				break;
			}
			default:
				break;
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		if (Rnd.chance(15))
		{
			try
			{
				actor.getLoc();
				final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(DarionsFaithfulServants[Rnd.get(DarionsFaithfulServants.length - 1)]));
				sp.setLoc(Location.findPointToStay(actor, 150, 350));
				sp.doSpawn(true);
				sp.stopRespawn();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		super.onEvtDead(killer);
	}
}
