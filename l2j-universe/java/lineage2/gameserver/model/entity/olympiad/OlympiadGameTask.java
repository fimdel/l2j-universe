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
package lineage2.gameserver.model.entity.olympiad;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.utils.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadGameTask extends RunnableImpl
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(OlympiadGameTask.class);
	/**
	 * Field _game.
	 */
	private final OlympiadGame _game;
	/**
	 * Field _status.
	 */
	private final BattleStatus _status;
	/**
	 * Field _count.
	 */
	private int _count;
	/**
	 * Field _time.
	 */
	private final long _time;
	/**
	 * Field _terminated.
	 */
	private boolean _terminated = false;
	
	/**
	 * Method isTerminated.
	 * @return boolean
	 */
	public boolean isTerminated()
	{
		return _terminated;
	}
	
	/**
	 * Method getStatus.
	 * @return BattleStatus
	 */
	public BattleStatus getStatus()
	{
		return _status;
	}
	
	/**
	 * Method getCount.
	 * @return int
	 */
	public int getCount()
	{
		return _count;
	}
	
	/**
	 * Method getGame.
	 * @return OlympiadGame
	 */
	public OlympiadGame getGame()
	{
		return _game;
	}
	
	/**
	 * Method getTime.
	 * @return long
	 */
	public long getTime()
	{
		return _count;
	}
	
	/**
	 * Method shedule.
	 * @return ScheduledFuture<?>
	 */
	public ScheduledFuture<?> shedule()
	{
		return ThreadPoolManager.getInstance().schedule(this, _time);
	}
	
	/**
	 * Constructor for OlympiadGameTask.
	 * @param game OlympiadGame
	 * @param status BattleStatus
	 * @param count int
	 * @param time long
	 */
	public OlympiadGameTask(OlympiadGame game, BattleStatus status, int count, long time)
	{
		_game = game;
		_status = status;
		_count = count;
		_time = time;
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		if ((_game == null) || _terminated)
		{
			return;
		}
		OlympiadGameTask task = null;
		int gameId = _game.getId();
		try
		{
			if (!Olympiad.inCompPeriod())
			{
				return;
			}
			if (!_game.checkPlayersOnline() && (_status != BattleStatus.ValidateWinner) && (_status != BattleStatus.Ending))
			{
				Log.add("Player is offline for game " + gameId + ", status: " + _status, "olympiad");
				_game.endGame(1000, true);
				return;
			}
			switch (_status)
			{
				case Begining:
				{
					_game.broadcastPacket(new SystemMessage(SystemMessage.YOU_WILL_ENTER_THE_OLYMPIAD_STADIUM_IN_S1_SECOND_S).addNumber(120), true, false);
					task = new OlympiadGameTask(_game, BattleStatus.Begin_Countdown, 60, 60000);
					break;
				}
				case Begin_Countdown:
				{
					_game.broadcastPacket(new SystemMessage(SystemMessage.YOU_WILL_ENTER_THE_OLYMPIAD_STADIUM_IN_S1_SECOND_S).addNumber(_count), true, false);
					if (_count == 60)
					{
						task = new OlympiadGameTask(_game, BattleStatus.Begin_Countdown, 30, 30000);
					}
					else if (_count == 30)
					{
						task = new OlympiadGameTask(_game, BattleStatus.Begin_Countdown, 15, 15000);
					}
					else if (_count == 15)
					{
						task = new OlympiadGameTask(_game, BattleStatus.Begin_Countdown, 5, 10000);
					}
					else if ((_count < 6) && (_count > 1))
					{
						task = new OlympiadGameTask(_game, BattleStatus.Begin_Countdown, _count - 1, 1000);
					}
					else if (_count == 1)
					{
						task = new OlympiadGameTask(_game, BattleStatus.PortPlayers, 0, 1000);
					}
					break;
				}
				case PortPlayers:
				{
					_game.portPlayersToArena();
					_game.managerShout();
					task = new OlympiadGameTask(_game, BattleStatus.Started, 60, 1000);
					break;
				}
				case Started:
				{
					if (_count == 60)
					{
						_game.setState(1);
						_game.preparePlayers();
						_game.addBuffers();
					}
					_game.broadcastPacket(new SystemMessage(SystemMessage.THE_GAME_WILL_START_IN_S1_SECOND_S).addNumber(_count), true, true);
					_count -= 10;
					if (_count > 0)
					{
						task = new OlympiadGameTask(_game, BattleStatus.Started, _count, 10000);
						break;
					}
					_game.openDoors();
					task = new OlympiadGameTask(_game, BattleStatus.CountDown, 5, 5000);
					break;
				}
				case CountDown:
				{
					_game.broadcastPacket(new SystemMessage(SystemMessage.THE_GAME_WILL_START_IN_S1_SECOND_S).addNumber(_count), true, true);
					_count--;
					if (_count <= 0)
					{
						task = new OlympiadGameTask(_game, BattleStatus.StartComp, 36, 1000);
					}
					else
					{
						task = new OlympiadGameTask(_game, BattleStatus.CountDown, _count, 1000);
					}
					break;
				}
				case StartComp:
				{
					_game.deleteBuffers();
					if (_count == 36)
					{
						_game.setState(2);
						_game.broadcastPacket(Msg.STARTS_THE_GAME, true, true);
						_game.broadcastInfo(null, null, false);
					}
					_count--;
					if (_count == 0)
					{
						task = new OlympiadGameTask(_game, BattleStatus.ValidateWinner, 0, 10000);
					}
					else
					{
						task = new OlympiadGameTask(_game, BattleStatus.StartComp, _count, 10000);
					}
					break;
				}
				case ValidateWinner:
				{
					try
					{
						_game.validateWinner(_count > 0);
					}
					catch (Exception e)
					{
						_log.error("", e);
					}
					task = new OlympiadGameTask(_game, BattleStatus.Ending, 0, 20000);
					break;
				}
				case Ending:
				{
					_game.collapse();
					_terminated = true;
					if (Olympiad._manager != null)
					{
						Olympiad._manager.freeOlympiadInstance(_game.getId());
					}
					return;
				}
			}
			if (task == null)
			{
				Log.add("task == null for game " + gameId, "olympiad");
				Thread.dumpStack();
				_game.endGame(1000, true);
				return;
			}
			_game.sheduleTask(task);
		}
		catch (Exception e)
		{
			_log.error("", e);
			_game.endGame(1000, true);
		}
	}
}
