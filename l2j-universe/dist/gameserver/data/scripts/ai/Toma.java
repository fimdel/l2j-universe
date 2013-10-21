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
public class Toma extends DefaultAI
{
	/**
	 * Field _points.
	 */
	private final Location[] _points =
	{
		new Location(151680, -174891, -1807, 41400),
		new Location(154153, -220105, -3402),
		new Location(178834, -184336, -352)
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
	 * Constructor for Toma.
	 * @param actor NpcInstance
	 */
	public Toma(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if ((System.currentTimeMillis() - _lastTeleport) < TELEPORT_PERIOD)
		{
			return false;
		}
		final NpcInstance _thisActor = getActor();
		final Location loc = _points[Rnd.get(_points.length)];
		if (_thisActor.getLoc().equals(loc))
		{
			return false;
		}
		_thisActor.broadcastPacketToOthers(new MagicSkillUse(_thisActor, _thisActor, 4671, 1, 1000, 0));
		ThreadPoolManager.getInstance().schedule(new Teleport(loc), 1000);
		_lastTeleport = System.currentTimeMillis();
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
