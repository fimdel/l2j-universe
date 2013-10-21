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
package ai.isle_of_prayer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Shade extends Fighter
{
	/**
	 * Field _wait_timeout.
	 */
	private long _wait_timeout = 0;
	/**
	 * Field _wait.
	 */
	private boolean _wait = false;
	/**
	 * Field DESPAWN_TIME.
	 */
	private static final int DESPAWN_TIME = 5 * 60 * 1000;
	/**
	 * Field BLUE_CRYSTAL. (value is 9595)
	 */
	private static final int BLUE_CRYSTAL = 9595;
	
	/**
	 * Constructor for Shade.
	 * @param actor NpcInstance
	 */
	public Shade(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (_def_think)
		{
			doTask();
			_wait = false;
			return true;
		}
		if (!_wait)
		{
			_wait = true;
			_wait_timeout = System.currentTimeMillis() + DESPAWN_TIME;
		}
		if ((_wait_timeout != 0) && _wait && (_wait_timeout < System.currentTimeMillis()))
		{
			actor.deleteMe();
			return true;
		}
		return super.thinkActive();
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		if (killer != null)
		{
			final Player player = killer.getPlayer();
			if (player != null)
			{
				final NpcInstance actor = getActor();
				if (Rnd.chance(10))
				{
					actor.dropItem(player, BLUE_CRYSTAL, 1);
				}
			}
		}
		super.onEvtDead(killer);
	}
}
