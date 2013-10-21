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

public class FourthGuard extends TIGuardSubAI
{
	public FourthGuard(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-116468, 255081, -1456),
			new Location(-115544, 254696, -1543),
			new Location(-114680, 254600, -1558),
			new Location(-114600, 254120, -1557),
			new Location(-114008, 254136, -1542),
			new Location(-113560, 254696, -1532),
			new Location(-112488, 254648, -1558),
			new Location(-113560, 254696, -1532),
			new Location(-114008, 254136, -1542),
			new Location(-114600, 254120, -1557),
			new Location(-114680, 254600, -1558),
			new Location(-115544, 254696, -1543)
		};
	}
}