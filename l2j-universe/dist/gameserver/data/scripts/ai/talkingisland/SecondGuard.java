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

public class SecondGuard extends TIGuardSubAI
{
	public SecondGuard(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114680, 255944, -1537),
			new Location(-114728, 254760, -1558),
			new Location(-114696, 253624, -1558),
			new Location(-114680, 252760, -1571),
			new Location(-114696, 253624, -1558),
			new Location(-114728, 254760, -1558)
		};
	}
}