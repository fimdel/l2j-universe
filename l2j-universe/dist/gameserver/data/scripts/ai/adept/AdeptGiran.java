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
package ai.adept;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdeptGiran extends Adept
{
	/**
	 * Constructor for AdeptGiran.
	 * @param actor NpcInstance
	 */
	public AdeptGiran(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(84856, 147760, -3400),
			new Location(83625, 147707, -3400),
			new Location(83617, 149544, -3400),
			new Location(83816, 149541, -3400),
			new Location(83632, 149559, -3400),
			new Location(83616, 147708, -3400)
		};
	}
}
