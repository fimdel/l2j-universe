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

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ZakenAnchor extends DefaultAI
{
	/**
	 * Field DayZaken. (value is 29176)
	 */
	private static final int DayZaken = 29176;
	/**
	 * Field UltraDayZaken. (value is 29181)
	 */
	private static final int UltraDayZaken = 29181;
	/**
	 * Field Candle. (value is 32705)
	 */
	private static final int Candle = 32705;
	/**
	 * Field i.
	 */
	private int i = 0;
	
	/**
	 * Constructor for ZakenAnchor.
	 * @param actor NpcInstance
	 */
	public ZakenAnchor(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		for (NpcInstance npc : actor.getAroundNpc(1000, 100))
		{
			if ((npc.getNpcId() == Candle) && (npc.getRightHandItem() == 15302))
			{
				i++;
			}
		}
		if (i >= 4)
		{
			if (actor.getReflection().getInstancedZoneId() == 133)
			{
				actor.getReflection().addSpawnWithoutRespawn(DayZaken, actor.getLoc(), 0);
				for (int i = 0; i < 4; i++)
				{
					actor.getReflection().addSpawnWithoutRespawn(20845, actor.getLoc(), 200);
					actor.getReflection().addSpawnWithoutRespawn(20847, actor.getLoc(), 200);
				}
				actor.deleteMe();
				return true;
			}
			else if (actor.getReflection().getInstancedZoneId() == 135)
			{
				for (NpcInstance npc : actor.getReflection().getNpcs())
				{
					if (npc.getNpcId() == UltraDayZaken)
					{
						npc.setIsInvul(false);
						npc.teleToLocation(actor.getLoc());
					}
				}
				for (int i = 0; i < 4; i++)
				{
					actor.getReflection().addSpawnWithoutRespawn(29184, actor.getLoc(), 300);
					actor.getReflection().addSpawnWithoutRespawn(29183, actor.getLoc(), 300);
				}
				actor.deleteMe();
				return true;
			}
		}
		else
		{
			i = 0;
		}
		return false;
	}
}
