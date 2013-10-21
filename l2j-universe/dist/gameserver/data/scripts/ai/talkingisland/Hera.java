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
public class Hera extends HeraSubAI
{
	/**
	 * Constructor for Hera.
	 * @param actor NpcInstance
	 */
	public Hera(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114153, 255089, -1528),
			new Location(-114360, 256536, -1280),
			new Location(-114805, 256797, -1200),
			new Location(-114749, 257049, -1136),
			new Location(-114309, 257443, -1152),
			new Location(-114749, 257049, -1136),
			new Location(-114805, 256797, -1200),
			new Location(-114360, 256536, -1280)
		};
	}
}
