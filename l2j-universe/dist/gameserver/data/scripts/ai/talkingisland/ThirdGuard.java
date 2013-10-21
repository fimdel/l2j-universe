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

public class ThirdGuard extends TIGuardSubAI
{
	public ThirdGuard(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-112552, 254712, -1558),
			new Location(-113800, 254760, -1548),
			new Location(-114376, 255208, -1546),
			new Location(-114744, 255128, -1550),
			new Location(-115160, 254728, -1547),
			new Location(-116056, 254824, -1534),
			new Location(-116536, 255192, -1475),
			new Location(-116056, 254824, -1534),
			new Location(-115160, 254728, -1547),
			new Location(-114744, 255128, -1550),
			new Location(-114376, 255208, -1546),
			new Location(-113800, 254760, -1548)
		};
	}
}