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
package ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Rooney extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(184022, -117083, -3342),
		new Location(183516, -118815, -3093),
		new Location(185007, -115651, -1587),
		new Location(186191, -116465, -1587),
		new Location(189630, -115611, -1587)
	};
	/**
	 * Field TELEPORT_PERIOD.
	 */
	private static final long TELEPORT_PERIOD = 30 * 60 * 1000;
	/**
	 * Field _lastTeleport.
	 */
	private long _lastTeleport = System.currentTimeMillis();
	
	/**
	 * Constructor for Rooney.
	 * @param actor NpcInstance
	 */
	public Rooney(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((System.currentTimeMillis() - _lastTeleport) < TELEPORT_PERIOD)
		{
			return false;
		}
		for (Location point : points)
		{
			Location loc = points[Rnd.get(points.length)];
			if (actor.getLoc().equals(loc))
			{
				continue;
			}
			actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 4671, 1, 1000, 0));
			ThreadPoolManager.getInstance().schedule(new Teleport(loc), 1000);
			_lastTeleport = System.currentTimeMillis();
			break;
		}
		return true;
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
}
