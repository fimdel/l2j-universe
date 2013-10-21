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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class IceCastleBreath extends Fighter
{
	/**
	 * Constructor for IceCastleBreath.
	 * @param actor NpcInstance
	 */
	public IceCastleBreath(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 6000;
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final Reflection r = getActor().getReflection();
		if ((r != null) && (r.getPlayers() != null))
		{
			for (Player p : r.getPlayers())
			{
				this.notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 5);
			}
		}
	}
	
	/**
	 * Method teleportHome.
	 */
	@Override
	protected void teleportHome()
	{
		return;
	}
}
