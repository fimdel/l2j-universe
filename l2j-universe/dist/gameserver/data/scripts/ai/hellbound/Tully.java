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
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Tully extends Fighter
{
	/**
	 * Field locSD.
	 */
	private static final Location[] locSD =
	{
		new Location(-12524, 273932, -9014, 49151),
		new Location(-10831, 273890, -9040, 81895),
		new Location(-10817, 273986, -9040, -16452),
		new Location(-13773, 275119, -9040, 8428),
		new Location(-11547, 271772, -9040, -19124),
	};
	/**
	 * Field locFTT.
	 */
	private static final Location[] locFTT =
	{
		new Location(-10832, 273808, -9040, 0),
		new Location(-10816, 274096, -9040, 14964),
		new Location(-13824, 275072, -9040, -24644),
		new Location(-11504, 271952, -9040, 9328),
	};
	/**
	 * Field s.
	 */
	private boolean s = false;
	/**
	 * Field removable_ghost.
	 */
	static NpcInstance removable_ghost = null;
	
	/**
	 * Constructor for Tully.
	 * @param actor NpcInstance
	 */
	public Tully(NpcInstance actor)
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
		for (Location element : locSD)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(32371));
				sp.setLoc(element);
				sp.doSpawn(true);
				if (!s)
				{
					Functions.npcShout(sp.getLastSpawn(), "Self Destruction mechanism launched: 10 minutes to explosion");
					s = true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		for (Location element : locFTT)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(22392));
				sp.setLoc(element);
				sp.doSpawn(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(32370));
			sp.setLoc(new Location(-11984, 272928, -9040, 23644));
			sp.doSpawn(true);
			removable_ghost = sp.getLastSpawn();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		ThreadPoolManager.getInstance().schedule(new UnspawnAndExplode(), 600 * 1000L);
		super.onEvtDead(killer);
	}
	
	/**
	 * @author Mobius
	 */
	static private class UnspawnAndExplode extends RunnableImpl
	{
		/**
		 * Constructor for UnspawnAndExplode.
		 */
		UnspawnAndExplode()
		{
			// empty method
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(32371, true))
			{
				npc.deleteMe();
			}
			for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(22392, true))
			{
				npc.deleteMe();
			}
			if (removable_ghost != null)
			{
				removable_ghost.deleteMe();
			}
		}
	}
}
