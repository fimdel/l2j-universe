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
public class Marsha extends MarshaSubAI
{
	/**
	 * Constructor for Marsha.
	 * @param actor NpcInstance
	 */
	public Marsha(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114762, 253192, -1504),
			new Location(-114936, 253496, -1522),
			new Location(-115032, 252920, -1547),
			new Location(-114936, 253496, -1522)
		};
	}
}
