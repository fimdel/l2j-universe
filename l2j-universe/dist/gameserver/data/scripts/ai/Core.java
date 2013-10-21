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
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Core extends Fighter
{
	/**
	 * Field _firstTimeAttacked.
	 */
	private boolean _firstTimeAttacked = true;
	/**
	 * Field TELEPORTATION_CUBIC_ID. (value is 31842)
	 */
	private static final int TELEPORTATION_CUBIC_ID = 31842;
	/**
	 * Field CUBIC_1_POSITION.
	 */
	private static final Location CUBIC_1_POSITION = new Location(16502, 110165, -6394, 0);
	/**
	 * Field CUBIC_2_POSITION.
	 */
	private static final Location CUBIC_2_POSITION = new Location(18948, 110165, -6394, 0);
	/**
	 * Field CUBIC_DESPAWN_TIME.
	 */
	private static final int CUBIC_DESPAWN_TIME = 15 * 60 * 1000;
	
	/**
	 * Constructor for Core.
	 * @param actor NpcInstance
	 */
	public Core(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if (_firstTimeAttacked)
		{
			Functions.npcSay(actor, "A non-permitted target has been discovered.");
			Functions.npcSay(actor, "Starting intruder removal system.");
			_firstTimeAttacked = false;
		}
		else if (Rnd.chance(1))
		{
			Functions.npcSay(actor, "Removing intruders.");
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		actor.broadcastPacket(new PlaySound(PlaySound.Type.MUSIC, "BS02_D", 1, 0, actor.getLoc()));
		Functions.npcSay(actor, "A fatal error has occurred");
		Functions.npcSay(actor, "System is being shut down...");
		Functions.npcSay(actor, "......");
		try
		{
			final NpcInstance cubic1 = NpcHolder.getInstance().getTemplate(TELEPORTATION_CUBIC_ID).getNewInstance();
			cubic1.setReflection(actor.getReflection());
			cubic1.setCurrentHpMp(cubic1.getMaxHp(), cubic1.getMaxMp(), true);
			cubic1.spawnMe(CUBIC_1_POSITION);
			final NpcInstance cubic2 = NpcHolder.getInstance().getTemplate(TELEPORTATION_CUBIC_ID).getNewInstance();
			cubic2.setReflection(actor.getReflection());
			cubic2.setCurrentHpMp(cubic1.getMaxHp(), cubic1.getMaxMp(), true);
			cubic2.spawnMe(CUBIC_2_POSITION);
			ThreadPoolManager.getInstance().schedule(new DeSpawnScheduleTimerTask(cubic1, cubic2), CUBIC_DESPAWN_TIME);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
	}
	
	/**
	 * @author Mobius
	 */
	static class DeSpawnScheduleTimerTask extends RunnableImpl
	{
		/**
		 * Field cubic1.
		 */
		final NpcInstance cubic1;
		/**
		 * Field cubic2.
		 */
		final NpcInstance cubic2;
		
		/**
		 * Constructor for DeSpawnScheduleTimerTask.
		 * @param cubic1 NpcInstance
		 * @param cubic2 NpcInstance
		 */
		DeSpawnScheduleTimerTask(NpcInstance cubic1, NpcInstance cubic2)
		{
			this.cubic1 = cubic1;
			this.cubic2 = cubic2;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			cubic1.deleteMe();
			cubic2.deleteMe();
		}
	}
}
