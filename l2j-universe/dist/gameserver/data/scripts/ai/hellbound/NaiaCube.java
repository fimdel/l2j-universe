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
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.instancemanager.naia.NaiaCoreManager;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NaiaCube extends DefaultAI
{
	/**
	 * Constructor for NaiaCube.
	 * @param actor NpcInstance
	 */
	public NaiaCube(NpcInstance actor)
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
		ThreadPoolManager.getInstance().schedule(new Despawn(getActor()), 120 * 1000L);
	}
	
	/**
	 * @author Mobius
	 */
	static private class Despawn extends RunnableImpl
	{
		/**
		 * Field _npc.
		 */
		NpcInstance _npc;
		
		/**
		 * Constructor for Despawn.
		 * @param npc NpcInstance
		 */
		Despawn(NpcInstance npc)
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
			NaiaCoreManager.setZoneActive(false);
			ReflectionUtils.getDoor(20240001).openMe();
			ReflectionUtils.getDoor(18250025).openMe();
		}
	}
}
