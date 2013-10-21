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

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Fireplace extends DefaultAI
{
	/**
	 * Field delay.
	 */
	private static final long delay = 5 * 60 * 1000L;
	
	/**
	 * Constructor for Fireplace.
	 * @param actor NpcInstance
	 */
	public Fireplace(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if (Rnd.chance(60))
		{
			getActor().setNpcState(1);
		}
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Switch(), 10000L, delay);
	}
	
	/**
	 * @author Mobius
	 */
	public class Switch extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance actor = getActor();
			if (actor.getNpcState() == 1)
			{
				actor.setNpcState(0);
			}
			else
			{
				actor.setNpcState(1);
				if (Rnd.chance(70))
				{
					NpcUtils.spawnSingle(18933, actor.getLoc(), delay >> 1);
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static public class DeleteCauldron extends RunnableImpl
	{
		/**
		 * Field _npc.
		 */
		NpcInstance _npc;
		
		/**
		 * Constructor for DeleteCauldron.
		 * @param npc NpcInstance
		 */
		public DeleteCauldron(NpcInstance npc)
		{
			_npc = npc;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_npc.deleteMe();
		}
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
