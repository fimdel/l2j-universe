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
public class DustTracker extends Patrollers
{
	/**
	 * Constructor for DustTracker.
	 * @param actor NpcInstance
	 */
	public DustTracker(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(125176, 111896, -3168),
			new Location(124872, 109736, -3104),
			new Location(123608, 108712, -3024),
			new Location(122632, 108008, -2992),
			new Location(120504, 109000, -2944),
			new Location(118632, 109944, -2960),
			new Location(115208, 109928, -3040),
			new Location(112568, 110296, -2976),
			new Location(110264, 111320, -3152),
			new Location(109512, 113432, -3088),
			new Location(109272, 116104, -3104),
			new Location(108008, 117912, -3056)
		};
	}
}
