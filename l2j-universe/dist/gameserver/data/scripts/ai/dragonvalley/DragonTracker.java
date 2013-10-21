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
package ai.dragonvalley;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DragonTracker extends Patrollers
{
	/**
	 * Constructor for DragonTracker.
	 * @param actor NpcInstance
	 */
	public DragonTracker(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(95896, 107832, -3136),
			new Location(97304, 109480, -3696),
			new Location(96296, 110312, -3728),
			new Location(93656, 109768, -3680),
			new Location(92008, 109896, -3784),
			new Location(90328, 111112, -3680),
			new Location(88584, 111064, -3760),
			new Location(86808, 110264, -3744),
			new Location(83928, 110504, -3744),
			new Location(82104, 110824, -3712),
			new Location(81128, 112312, -3664),
			new Location(79736, 114776, -3728),
			new Location(79288, 113608, -3376),
			new Location(78984, 112408, -3072),
			new Location(79192, 111416, -2984),
			new Location(80328, 110136, -3048),
			new Location(82968, 108968, -3136),
			new Location(85656, 108984, -3200),
			new Location(88296, 108440, -3056),
			new Location(91528, 107672, -3056),
			new Location(94104, 107592, -3040)
		};
	}
}
