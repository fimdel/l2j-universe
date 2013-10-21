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
public class Remons extends RemonsSubAI
{
	/**
	 * Constructor for Remons.
	 * @param actor NpcInstance
	 */
	public Remons(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114424, 252408, -1592),
			new Location(-114456, 251064, -1761),
			new Location(-114072, 250632, -1850),
			new Location(-112392, 250328, -2096),
			new Location(-111672, 249448, -2400),
			new Location(-109960, 248232, -2742),
			new Location(-109720, 246808, -3021),
			new Location(-108664, 247288, -3243),
			new Location(-107752, 248664, -3255),
			new Location(-108664, 247288, -3243),
			new Location(-109720, 246808, -3021),
			new Location(-109960, 248232, -2742),
			new Location(-111672, 249448, -2400),
			new Location(-112392, 250328, -2096),
			new Location(-114072, 250632, -1850),
			new Location(-114456, 251064, -1761)
		};
	}
}
