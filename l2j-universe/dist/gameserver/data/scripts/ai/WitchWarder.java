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

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WitchWarder extends Fighter
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
	private static final int DESPAWN_TIME = 3 * 60 * 1000;
	
	/**
	 * Constructor for WitchWarder.
	 * @param actor NpcInstance
	 */
	public WitchWarder(NpcInstance actor)
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
}
