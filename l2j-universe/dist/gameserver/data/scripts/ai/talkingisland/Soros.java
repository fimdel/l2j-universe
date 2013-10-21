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
public class Soros extends SorosSubAI
{
	/**
	 * Constructor for Soros.
	 * @param actor NpcInstance
	 */
	public Soros(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114296, 252408, -1591),
			new Location(-114232, 251160, -1748),
			new Location(-113992, 250776, -1841),
			new Location(-112376, 250488, -2087),
			new Location(-111576, 249640, -2394),
			new Location(-109896, 248392, -2728),
			new Location(-109496, 247064, -3024),
			new Location(-108776, 247320, -3228),
			new Location(-107800, 248792, -3245),
			new Location(-108776, 247320, -3228),
			new Location(-109496, 247064, -3024),
			new Location(-109896, 248392, -2728),
			new Location(-111576, 249640, -2394),
			new Location(-112376, 250488, -2087),
			new Location(-113992, 250776, -1841),
			new Location(-114232, 251160, -1748)
		};
	}
}
