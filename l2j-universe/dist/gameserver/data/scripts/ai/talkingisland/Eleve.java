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
public class Eleve extends EleveSubAI
{
	/**
	 * Constructor for Eleve.
	 * @param actor NpcInstance
	 */
	public Eleve(NpcInstance actor)
	{
		super(actor);
		_points = new Location[]
		{
			new Location(-114929, 259518, -1192),
			new Location(-114584, 260056, -1224),
			new Location(-114952, 260088, -1224)
		};
	}
}
