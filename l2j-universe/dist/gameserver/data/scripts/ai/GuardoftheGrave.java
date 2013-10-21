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
package ai;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GuardoftheGrave extends Fighter
{
	/**
	 * Field DESPAWN_TIME.
	 */
	private static final int DESPAWN_TIME = 2 * 45 * 1000;
	/**
	 * Field CHIEFTAINS_TREASURE_CHEST. (value is 18816)
	 */
	private static final int CHIEFTAINS_TREASURE_CHEST = 18816;
	
	/**
	 * Constructor for GuardoftheGrave.
	 * @param actor NpcInstance
	 */
	public GuardoftheGrave(NpcInstance actor)
	{
		super(actor);
		actor.setIsInvul(true);
		actor.startImmobilized();
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		ThreadPoolManager.getInstance().schedule(new DeSpawnTask(), DESPAWN_TIME + Rnd.get(1, 30));
	}
	
	/**
	 * Method checkTarget.
	 * @param target Creature
	 * @param range int
	 * @return boolean
	 */
	@Override
	protected boolean checkTarget(Creature target, int range)
	{
		final NpcInstance actor = getActor();
		if ((actor != null) && (target != null) && !actor.isInRange(target, actor.getAggroRange()))
		{
			actor.getAggroList().remove(target, true);
			return false;
		}
		return super.checkTarget(target, range);
	}
	
	/**
	 * Method spawnChest.
	 * @param actor NpcInstance
	 */
	protected void spawnChest(NpcInstance actor)
	{
		try
		{
			final NpcInstance npc = NpcHolder.getInstance().getTemplate(CHIEFTAINS_TREASURE_CHEST).getNewInstance();
			npc.setSpawnedLoc(actor.getLoc());
			npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp(), true);
			npc.spawnMe(npc.getSpawnedLoc());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class DeSpawnTask extends RunnableImpl
	{
		/**
		 * Constructor for DeSpawnTask.
		 */
		DeSpawnTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance actor = getActor();
			spawnChest(actor);
			actor.deleteMe();
		}
	}
}
