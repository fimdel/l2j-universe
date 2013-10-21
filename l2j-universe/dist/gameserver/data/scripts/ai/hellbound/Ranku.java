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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.AggroList.AggroInfo;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Ranku extends Fighter
{
	/**
	 * Field TELEPORTATION_CUBIC_ID. (value is 32375)
	 */
	private static final int TELEPORTATION_CUBIC_ID = 32375;
	/**
	 * Field CUBIC_POSITION.
	 */
	private static final Location CUBIC_POSITION = new Location(-19056, 278732, -15000, 0);
	/**
	 * Field SCAPEGOAT_ID. (value is 32305)
	 */
	private static final int SCAPEGOAT_ID = 32305;
	/**
	 * Field _massacreTimer.
	 */
	private long _massacreTimer = 0;
	/**
	 * Field _massacreDelay.
	 */
	private static final long _massacreDelay = 30000L;
	
	/**
	 * Constructor for Ranku.
	 * @param actor NpcInstance
	 */
	public Ranku(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final Reflection r = getActor().getReflection();
		if (r != null)
		{
			for (int i = 0; i < 4; i++)
			{
				r.addSpawnWithRespawn(SCAPEGOAT_ID, getActor().getLoc(), 300, 60);
			}
		}
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return;
		}
		if ((_massacreTimer + _massacreDelay) < System.currentTimeMillis())
		{
			final NpcInstance victim = getScapegoat();
			_massacreTimer = System.currentTimeMillis();
			if (victim != null)
			{
				actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, victim, getMaximumHate() + 200000);
			}
		}
		super.thinkAttack();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		if (actor.getReflection() != null)
		{
			actor.getReflection().setReenterTime(System.currentTimeMillis());
			actor.getReflection().addSpawnWithoutRespawn(TELEPORTATION_CUBIC_ID, CUBIC_POSITION, 0);
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method getScapegoat.
	 * @return NpcInstance
	 */
	private NpcInstance getScapegoat()
	{
		for (NpcInstance n : getActor().getReflection().getNpcs())
		{
			if ((n.getNpcId() == SCAPEGOAT_ID) && !n.isDead())
			{
				return n;
			}
		}
		return null;
	}
	
	/**
	 * Method getMaximumHate.
	 * @return int
	 */
	private int getMaximumHate()
	{
		final NpcInstance actor = getActor();
		final Creature cha = actor.getAggroList().getMostHated();
		if (cha != null)
		{
			final AggroInfo ai = actor.getAggroList().get(cha);
			if (ai != null)
			{
				return ai.hate;
			}
		}
		return 0;
	}
}
