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
package lineage2.gameserver.model.entity.events.objects;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.KrateisCubeEvent;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KrateisCubePlayerObject implements Serializable, Comparable<KrateisCubePlayerObject>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @author Mobius
	 */
	private class RessurectTask extends RunnableImpl
	{
		/**
		 * Field _seconds.
		 */
		private int _seconds = 10;
		
		/**
		 * Constructor for RessurectTask.
		 */
		public RessurectTask()
		{
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_seconds -= 1;
			if (_seconds == 0)
			{
				KrateisCubeEvent cubeEvent = _player.getEvent(KrateisCubeEvent.class);
				List<Location> waitLocs = cubeEvent.getObjects(KrateisCubeEvent.WAIT_LOCS);
				_ressurectTask = null;
				_player.teleToLocation(Rnd.get(waitLocs));
				_player.doRevive();
			}
			else
			{
				_player.sendPacket(new SystemMessage2(SystemMsg.RESURRECTION_WILL_TAKE_PLACE_IN_THE_WAITING_ROOM_AFTER_S1_SECONDS).addInteger(_seconds));
				_ressurectTask = ThreadPoolManager.getInstance().schedule(this, 1000L);
			}
		}
	}
	
	/**
	 * Field _player.
	 */
	final Player _player;
	/**
	 * Field _registrationTime.
	 */
	private final long _registrationTime;
	/**
	 * Field _showRank.
	 */
	private boolean _showRank;
	/**
	 * Field _points.
	 */
	private int _points;
	/**
	 * Field _ressurectTask.
	 */
	Future<?> _ressurectTask;
	
	/**
	 * Constructor for KrateisCubePlayerObject.
	 * @param player Player
	 */
	public KrateisCubePlayerObject(Player player)
	{
		_player = player;
		_registrationTime = System.currentTimeMillis();
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _player.getName();
	}
	
	/**
	 * Method isShowRank.
	 * @return boolean
	 */
	public boolean isShowRank()
	{
		return _showRank;
	}
	
	/**
	 * Method getPoints.
	 * @return int
	 */
	public int getPoints()
	{
		return _points;
	}
	
	/**
	 * Method setPoints.
	 * @param points int
	 */
	public void setPoints(int points)
	{
		_points = points;
	}
	
	/**
	 * Method setShowRank.
	 * @param showRank boolean
	 */
	public void setShowRank(boolean showRank)
	{
		_showRank = showRank;
	}
	
	/**
	 * Method getRegistrationTime.
	 * @return long
	 */
	public long getRegistrationTime()
	{
		return _registrationTime;
	}
	
	/**
	 * Method getObjectId.
	 * @return int
	 */
	public int getObjectId()
	{
		return _player.getObjectId();
	}
	
	/**
	 * Method getPlayer.
	 * @return Player
	 */
	public Player getPlayer()
	{
		return _player;
	}
	
	/**
	 * Method startRessurectTask.
	 */
	public void startRessurectTask()
	{
		if (_ressurectTask != null)
		{
			return;
		}
		_ressurectTask = ThreadPoolManager.getInstance().schedule(new RessurectTask(), 1000L);
	}
	
	/**
	 * Method stopRessurectTask.
	 */
	public void stopRessurectTask()
	{
		if (_ressurectTask != null)
		{
			_ressurectTask.cancel(false);
			_ressurectTask = null;
		}
	}
	
	/**
	 * Method compareTo.
	 * @param o KrateisCubePlayerObject
	 * @return int
	 */
	@Override
	public int compareTo(KrateisCubePlayerObject o)
	{
		if (getPoints() == o.getPoints())
		{
			return (int) ((getRegistrationTime() - o.getRegistrationTime()) / 1000L);
		}
		return getPoints() - o.getPoints();
	}
}
