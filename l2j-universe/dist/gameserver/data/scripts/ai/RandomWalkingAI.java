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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RandomWalkingAI extends DefaultAI
{
	/**
	 * Field AI_WALK_RANGE.
	 */
	protected static final int AI_WALK_RANGE = Rnd.get(200, 700);
	/**
	 * Field Boy. (value is 33224)
	 */
	private static final int Boy = 33224;
	/**
	 * Field Girl. (value is 33217)
	 */
	private static final int Girl = 33217;
	/**
	 * Field Marsha. (value is 33109)
	 */
	private static final int Marsha = 33109;
	/**
	 * Field Rabbits. (value is 33203)
	 */
	private static final int Rabbits = 33203;
	/**
	 * Field Rabbits2. (value is 32971)
	 */
	private static final int Rabbits2 = 32971;
	
	/**
	 * Constructor for RandomWalkingAI.
	 * @param actor NpcInstance
	 */
	public RandomWalkingAI(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 2000;
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
		if (val < 80)
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
		final int x = (sloc.x + Rnd.get(2 * AI_WALK_RANGE)) - AI_WALK_RANGE;
		final int y = (sloc.y + Rnd.get(2 * AI_WALK_RANGE)) - AI_WALK_RANGE;
		final int z = GeoEngine.getHeight(x, y, sloc.z, actor.getGeoIndex());
		switch (actor.getNpcId())
		{
			case Boy:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Girl:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Rabbits:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Rabbits2:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			case Marsha:
				actor.setRunning();
				actor.moveToLocation(x, y, z, 0, true);
				break;
			default:
				actor.moveToLocation(x, y, z, 0, true);
				break;
		}
		return true;
	}
}
