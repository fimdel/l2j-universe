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
package ai.isle_of_prayer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WaterDragonDetractor extends Fighter
{
	/**
	 * Field SPIRIT_OF_LAKE. (value is 9689)
	 */
	private static final int SPIRIT_OF_LAKE = 9689;
	/**
	 * Field BLUE_CRYSTAL. (value is 9595)
	 */
	private static final int BLUE_CRYSTAL = 9595;
	
	/**
	 * Constructor for WaterDragonDetractor.
	 * @param actor NpcInstance
	 */
	public WaterDragonDetractor(NpcInstance actor)
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
		if (killer != null)
		{
			final Player player = killer.getPlayer();
			if (player != null)
			{
				final NpcInstance actor = getActor();
				actor.dropItem(player, SPIRIT_OF_LAKE, 1);
				if (Rnd.chance(10))
				{
					actor.dropItem(player, BLUE_CRYSTAL, 1);
				}
			}
		}
		super.onEvtDead(killer);
	}
}
