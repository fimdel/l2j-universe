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
public class Morgan extends MorganSubAI
{
	/**
	 * Constructor for Morgan.
	 * @param actor NpcInstance
	 */
	public Morgan(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114021, 255537, -1512),
			new Location(-113752, 256136, -1536),
			new Location(-113832, 255672, -1531),
			new Location(-114296, 255672, -1537)
		};
	}
}
