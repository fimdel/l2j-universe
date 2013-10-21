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
public class Mei extends MeiSubAI
{
	/**
	 * Constructor for Mei.
	 * @param actor NpcInstance
	 */
	public Mei(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-116117, 256879, -1512),
			new Location(-115544, 256424, -1537),
			new Location(-115016, 256024, -1538),
			new Location(-114392, 255784, -1537)
		};
	}
}
