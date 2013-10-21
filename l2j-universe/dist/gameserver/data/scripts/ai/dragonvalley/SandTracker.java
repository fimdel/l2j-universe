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
public class SandTracker extends Patrollers
{
	/**
	 * Constructor for SandTracker.
	 * @param actor NpcInstance
	 */
	public SandTracker(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(122360, 114312, -3792),
			new Location(125032, 114872, -3728),
			new Location(127304, 114040, -3520),
			new Location(128216, 113480, -3696),
			new Location(130248, 114296, -3776),
			new Location(130136, 114888, -3792),
			new Location(128568, 115848, -3776),
			new Location(125816, 115288, -3728),
			new Location(123640, 115800, -3632),
			new Location(122872, 116888, -3664),
			new Location(120648, 116888, -3632),
			new Location(118312, 116888, -3728),
			new Location(117832, 117960, -3728),
			new Location(116696, 119832, -3680),
			new Location(115224, 120200, -3664),
			new Location(113384, 121768, -3712),
			new Location(110936, 123368, -3680),
			new Location(107208, 122136, -3680),
			new Location(103688, 121560, -3776),
			new Location(101768, 121400, -3680),
			new Location(101240, 119448, -3512),
			new Location(101320, 116728, -3696),
			new Location(101256, 114856, -3728),
			new Location(101496, 112472, -3696),
			new Location(102968, 113256, -3656),
			new Location(103128, 114776, -3168),
			new Location(103400, 116040, -3056),
			new Location(104408, 117112, -3056),
			new Location(105880, 117992, -3024),
			new Location(107384, 117864, -3056),
			new Location(108552, 117912, -3048),
			new Location(109912, 119416, -3088),
			new Location(111352, 119256, -3056),
			new Location(112328, 118264, -3072),
			new Location(114008, 115784, -3280),
			new Location(115768, 114440, -3104)
		};
	}
}
