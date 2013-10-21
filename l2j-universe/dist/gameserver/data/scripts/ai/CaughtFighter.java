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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CaughtFighter extends Fighter
{
	/**
	 * Field TIME_TO_LIVE. (value is 60000)
	 */
	private static final int TIME_TO_LIVE = 60000;
	/**
	 * Field TIME_TO_DIE.
	 */
	private final long TIME_TO_DIE = System.currentTimeMillis() + TIME_TO_LIVE;
	
	/**
	 * Constructor for CaughtFighter.
	 * @param actor NpcInstance
	 */
	public CaughtFighter(NpcInstance actor)
	{
		super(actor);
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
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		if (Rnd.chance(75))
		{
			Functions.npcSayCustomMessage(getActor(), "scripts.ai.CaughtMob.spawn");
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		if (Rnd.chance(75))
		{
			Functions.npcSayCustomMessage(getActor(), "scripts.ai.CaughtMob.death");
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((actor != null) && (System.currentTimeMillis() >= TIME_TO_DIE))
		{
			actor.deleteMe();
			return false;
		}
		return super.thinkActive();
	}
}
