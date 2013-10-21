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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HekatonPrime extends Fighter
{
	/**
	 * Field _lastTimeAttacked.
	 */
	private long _lastTimeAttacked;
	
	/**
	 * Constructor for HekatonPrime.
	 * @param actor NpcInstance
	 */
	public HekatonPrime(NpcInstance actor)
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
		_lastTimeAttacked = System.currentTimeMillis();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if ((_lastTimeAttacked + 600000) < System.currentTimeMillis())
		{
			if (getActor().getMinionList().hasMinions())
			{
				getActor().getMinionList().deleteMinions();
			}
			getActor().deleteMe();
			return true;
		}
		return false;
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		_lastTimeAttacked = System.currentTimeMillis();
		super.onEvtAttacked(attacker, damage);
	}
}
