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
public class Rubentis extends RubentisSubAI
{
	/**
	 * Constructor for Rubentis.
	 * @param actor NpcInstance
	 */
	public Rubentis(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114392, 255128, -1551),
			new Location(-115208, 255032, -1523),
			new Location(-114904, 254936, -1555)
		};
	}
}
