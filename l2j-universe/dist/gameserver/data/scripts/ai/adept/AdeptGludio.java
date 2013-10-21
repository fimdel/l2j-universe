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
public class AdeptGludio extends Adept
{
	/**
	 * Constructor for AdeptGludio.
	 * @param actor NpcInstance
	 */
	public AdeptGludio(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-14601, 121243, -2984),
			new Location(-14174, 121266, -2984),
			new Location(-14551, 121247, -2984),
			new Location(-13702, 121246, -2984),
			new Location(-14134, 121250, -2984),
			new Location(-14145, 121680, -2984),
			new Location(-13896, 122250, -2984),
			new Location(-13096, 122259, -2984),
			new Location(-13885, 122272, -2984),
			new Location(-14153, 121682, -2984),
			new Location(-14156, 121261, -2984),
			new Location(-13683, 121252, -2984)
		};
	}
}
