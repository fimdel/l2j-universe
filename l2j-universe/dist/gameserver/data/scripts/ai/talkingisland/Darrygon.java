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
package ai.talkingisland;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Darrygon extends DarrygonSubAI
{
	/**
	 * Constructor for Darrygon.
	 * @param actor NpcInstance
	 */
	public Darrygon(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114003, 253081, -1496),
			new Location(-114456, 253080, -1567),
			new Location(-113944, 253416, -1531),
			new Location(-114456, 253080, -1567)
		};
	}
}
