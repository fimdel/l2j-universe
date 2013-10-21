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

import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PaganGuard extends Mystic
{
	/**
	 * Constructor for PaganGuard.
	 * @param actor NpcInstance
	 */
	public PaganGuard(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
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
		if ((target != null) && !actor.isInRange(target, actor.getAggroRange()))
		{
			actor.getAggroList().remove(target, true);
			return false;
		}
		return super.checkTarget(target, range);
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
