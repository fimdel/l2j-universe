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
public class DragonScout extends Patrollers
{
	/**
	 * Constructor for DragonScout.
	 * @param actor NpcInstance
	 */
	public DragonScout(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(116792, 116936, -3728),
			new Location(116056, 118984, -3728),
			new Location(114856, 120040, -3712),
			new Location(114184, 121464, -3776),
			new Location(115640, 122856, -3352),
			new Location(116600, 123304, -3136),
			new Location(118248, 122824, -3072),
			new Location(119800, 121656, -3024),
			new Location(120904, 119912, -3072),
			new Location(121720, 119384, -3136),
			new Location(124168, 118968, -3104),
			new Location(125864, 117832, -3056),
			new Location(126680, 117688, -3136),
			new Location(126728, 115256, -3728),
			new Location(123720, 114456, -3712),
			new Location(121208, 112536, -3792),
			new Location(120168, 114024, -3704),
			new Location(120232, 115368, -3712)
		};
	}
}
