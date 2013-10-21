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
public class Guardian extends GuardianSubAI
{
	/**
	 * Constructor for Guardian.
	 * @param actor NpcInstance
	 */
	public Guardian(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-112424, 257813, -1534),
			new Location(-112934, 257783, -1310),
			new Location(-113890, 257765, -1165),
			new Location(-114137, 257385, -1164),
			new Location(-113890, 257793, -1164),
			new Location(-113311, 257791, -1164),
			new Location(-112910, 257796, -1310),
			new Location(-112401, 257794, -1538),
			new Location(-112297, 257690, -1534),
			new Location(-112609, 257329, -1529),
			new Location(-112761, 256812, -1510),
			new Location(-113451, 256512, -1533),
			new Location(-113640, 256292, -1533),
			new Location(-113790, 255997, -1540),
			new Location(-114207, 255827, -1538),
			new Location(-114476, 255815, -1538),
			new Location(-115063, 256176, -1538),
			new Location(-115548, 256638, -1538),
			new Location(-116077, 256793, -1542),
			new Location(-116278, 257493, -1538),
			new Location(-116413, 257801, -1539),
			new Location(-115845, 257795, -1310),
			new Location(-115758, 257799, -1310),
			new Location(-115428, 257793, -1164),
			new Location(-114903, 257597, -1164),
			new Location(-114570, 257332, -1164)
		};
	}
}
