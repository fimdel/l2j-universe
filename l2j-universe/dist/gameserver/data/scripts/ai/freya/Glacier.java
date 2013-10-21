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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Glacier extends Fighter
{
	/**
	 * Constructor for Glacier.
	 * @param actor NpcInstance
	 */
	public Glacier(NpcInstance actor)
	{
		super(actor);
		actor.block();
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		getActor().setNpcState(1);
		ThreadPoolManager.getInstance().schedule(new Freeze(), 800);
		ThreadPoolManager.getInstance().schedule(new Despawn(), 30000L);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		for (Creature cha : getActor().getAroundCharacters(350, 100))
		{
			if (cha.isPlayer())
			{
				cha.altOnMagicUseTimer(cha, SkillTable.getInstance().getInfo(6301, 1));
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * @author Mobius
	 */
	private class Freeze extends RunnableImpl
	{
		/**
		 * Constructor for Freeze.
		 */
		Freeze()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			getActor().setNpcState(2);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class Despawn extends RunnableImpl
	{
		/**
		 * Constructor for Despawn.
		 */
		Despawn()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			getActor().deleteMe();
		}
	}
}
