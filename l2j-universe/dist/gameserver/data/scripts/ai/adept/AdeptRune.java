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
public class AdeptRune extends Adept
{
	/**
	 * Constructor for AdeptRune.
	 * @param actor NpcInstance
	 */
	public AdeptRune(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(45948, -48190, -792),
			new Location(45253, -47988, -792),
			new Location(43516, -48105, -792),
			new Location(43318, -47420, -792),
			new Location(43386, -46879, -792),
			new Location(43318, -47420, -792),
			new Location(43516, -48105, -792),
			new Location(45253, -47988, -792)
		};
	}
}
