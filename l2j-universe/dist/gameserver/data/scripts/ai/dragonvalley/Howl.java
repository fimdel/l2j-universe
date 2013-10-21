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
public class Howl extends Patrollers
{
	/**
	 * Constructor for Howl.
	 * @param actor NpcInstance
	 */
	public Howl(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(94664, 117368, -3056),
			new Location(93400, 118600, -2992),
			new Location(91256, 118536, -3056),
			new Location(90376, 119640, -3056),
			new Location(88904, 119352, -3056),
			new Location(87208, 120264, -3056),
			new Location(86040, 119576, -3008),
			new Location(84264, 118280, -3008),
			new Location(85016, 116360, -3056),
			new Location(86200, 115208, -3040),
			new Location(87352, 114632, -3008),
			new Location(89160, 114984, -3056),
			new Location(90056, 115976, -3056),
			new Location(91000, 117832, -3088),
			new Location(93224, 118360, -3024)
		};
	}
}
