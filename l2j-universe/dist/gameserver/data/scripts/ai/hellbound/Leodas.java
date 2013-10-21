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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Leodas extends Fighter
{
	/**
	 * Constructor for Leodas.
	 * @param actor NpcInstance
	 */
	public Leodas(NpcInstance actor)
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
		ReflectionUtils.getDoor(19250003).openMe();
		ReflectionUtils.getDoor(19250004).openMe();
		ThreadPoolManager.getInstance().schedule(new CloseDoor(), 60 * 1000L);
		super.onEvtDead(killer);
	}
	
	/**
	 * @author Mobius
	 */
	static private class CloseDoor extends RunnableImpl
	{
		/**
		 * Constructor for CloseDoor.
		 */
		CloseDoor()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			ReflectionUtils.getDoor(19250003).closeMe();
			ReflectionUtils.getDoor(19250004).closeMe();
		}
	}
}
