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
public class DrakeRunners extends Patrollers
{
	/**
	 * Constructor for DrakeRunners.
	 * @param actor NpcInstance
	 */
	public DrakeRunners(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(148984, 112952, -3720),
			new Location(149160, 114312, -3720),
			new Location(149096, 115480, -3720),
			new Location(147720, 116216, -3720),
			new Location(146536, 116296, -3720),
			new Location(145192, 115304, -3720),
			new Location(144888, 114504, -3720),
			new Location(145240, 113272, -3720),
			new Location(145960, 112696, -3720),
			new Location(147416, 112488, -3720)
		};
	}
}
