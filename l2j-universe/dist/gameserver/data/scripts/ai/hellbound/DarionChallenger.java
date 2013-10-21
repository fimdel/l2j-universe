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
public class DarionChallenger extends Fighter
{
	/**
	 * Field TeleportCube. (value is 32467)
	 */
	private static final int TeleportCube = 32467;
	
	/**
	 * Constructor for DarionChallenger.
	 * @param actor NpcInstance
	 */
	public DarionChallenger(NpcInstance actor)
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
		if (checkAllDestroyed())
		{
			try
			{
				final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(TeleportCube));
				sp.setLoc(new Location(-12527, 279714, -11622, 16384));
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
	 * Method checkAllDestroyed.
	 * @return boolean
	 */
	private static boolean checkAllDestroyed()
	{
		if (!GameObjectsStorage.getAllByNpcId(25600, true).isEmpty())
		{
			return false;
		}
		if (!GameObjectsStorage.getAllByNpcId(25601, true).isEmpty())
		{
			return false;
		}
		if (!GameObjectsStorage.getAllByNpcId(25602, true).isEmpty())
		{
			return false;
		}
		return true;
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
			for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(TeleportCube, true))
			{
				npc.deleteMe();
			}
		}
	}
}
