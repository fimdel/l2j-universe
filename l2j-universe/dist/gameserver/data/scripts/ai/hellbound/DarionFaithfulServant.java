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

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DarionFaithfulServant extends Fighter
{
	/**
	 * Field MysteriousAgent. (value is 32372)
	 */
	private static final int MysteriousAgent = 32372;
	
	/**
	 * Constructor for DarionFaithfulServant.
	 * @param actor NpcInstance
	 */
	public DarionFaithfulServant(NpcInstance actor)
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
		if (Rnd.chance(15))
		{
			try
			{
				final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(MysteriousAgent));
				sp.setLoc(new Location(-11984, 278880, -13599, -4472));
				sp.doSpawn(true);
				sp.stopRespawn();
				ThreadPoolManager.getInstance().schedule(new Unspawn(), 600 * 1000L);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * @author Mobius
	 */
	static private class Unspawn extends RunnableImpl
	{
		/**
		 * Constructor for Unspawn.
		 */
		Unspawn()
		{
			// empty method
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(MysteriousAgent, true))
			{
				npc.deleteMe();
			}
		}
	}
}
