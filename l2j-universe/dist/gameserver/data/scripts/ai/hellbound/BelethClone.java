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

import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BelethClone extends Mystic
{
	/**
	 * Constructor for BelethClone.
	 * @param actor NpcInstance
	 */
	public BelethClone(NpcInstance actor)
	{
		super(actor);
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
	 * Method randomAnimation.
	 * @return boolean
	 */
	@Override
	protected boolean randomAnimation()
	{
		return false;
	}
	
	/**
	 * Method canSeeInSilentMove.
	 * @param target Playable
	 * @return boolean
	 */
	@Override
	public boolean canSeeInSilentMove(Playable target)
	{
		return true;
	}
	
	/**
	 * Method canSeeInHide.
	 * @param target Playable
	 * @return boolean
	 */
	@Override
	public boolean canSeeInHide(Playable target)
	{
		return true;
	}
	
	/**
	 * Method addTaskAttack.
	 * @param target Creature
	 */
	@Override
	public void addTaskAttack(Creature target)
	{
		return;
	}
}
