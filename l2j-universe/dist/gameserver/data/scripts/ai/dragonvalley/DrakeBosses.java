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
package ai.dragonvalley;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DrakeBosses extends Fighter
{
	/**
	 * Constructor for DrakeBosses.
	 * @param actor NpcInstance
	 */
	public DrakeBosses(NpcInstance actor)
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
		switch (getActor().getNpcId())
		{
			case 25725:
				NpcUtils.spawnSingle(32884, getActor().getLoc(), 300000);
				break;
			case 25726:
				NpcUtils.spawnSingle(32885, getActor().getLoc(), 300000);
				break;
			case 25727:
				NpcUtils.spawnSingle(32886, getActor().getLoc(), 300000);
				break;
		}
		super.onEvtDead(killer);
		getActor().endDecayTask();
	}
}
