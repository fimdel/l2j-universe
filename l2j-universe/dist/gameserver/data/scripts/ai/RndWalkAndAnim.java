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
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RndWalkAndAnim extends DefaultAI
{
	/**
	 * Field PET_WALK_RANGE. (value is 100)
	 */
	protected static final int PET_WALK_RANGE = 100;
	
	/**
	 * Constructor for RndWalkAndAnim.
	 * @param actor NpcInstance
	 */
	public RndWalkAndAnim(NpcInstance actor)
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
		if (actor.isMoving)
		{
			return false;
		}
		final int val = Rnd.get(100);
		if (val < 10)
		{
			randomWalk();
		}
		else if (val < 20)
		{
			actor.onRandomAnimation();
		}
		return false;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return false;
		}
		final Location sloc = actor.getSpawnedLoc();
		final int x = (sloc.x + Rnd.get(2 * PET_WALK_RANGE)) - PET_WALK_RANGE;
		final int y = (sloc.y + Rnd.get(2 * PET_WALK_RANGE)) - PET_WALK_RANGE;
		final int z = GeoEngine.getHeight(x, y, sloc.z, actor.getGeoIndex());
		actor.setRunning();
		actor.moveToLocation(x, y, z, 0, true);
		return true;
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// empty method
	}
}
