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

import lineage2.gameserver.data.StringHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.TimeUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadHistory
{
	/**
	 * Field _objectId1.
	 */
	private final int _objectId1;
	/**
	 * Field _objectId2.
	 */
	private final int _objectId2;
	/**
	 * Field _classId1.
	 */
	private final int _classId1;
	/**
	 * Field _classId2.
	 */
	private final int _classId2;
	/**
	 * Field _name1.
	 */
	private final String _name1;
	/**
	 * Field _name2.
	 */
	private final String _name2;
	/**
	 * Field _gameStartTime.
	 */
	private final long _gameStartTime;
	/**
	 * Field _gameTime.
	 */
	private final int _gameTime;
	/**
	 * Field _gameStatus.
	 */
	private final int _gameStatus;
	/**
	 * Field _gameType.
	 */
	private final int _gameType;
	
	/**
	 * Constructor for OlympiadHistory.
	 * @param objectId1 int
	 * @param objectId2 int
	 * @param classId1 int
	 * @param classId2 int
	 * @param name1 String
	 * @param name2 String
	 * @param gameStartTime long
	 * @param gameTime int
	 * @param gameStatus int
	 * @param gameType int
	 */
	public OlympiadHistory(int objectId1, int objectId2, int classId1, int classId2, String name1, String name2, long gameStartTime, int gameTime, int gameStatus, int gameType)
	{
		_objectId1 = objectId1;
		_objectId2 = objectId2;
		_classId1 = classId1;
		_classId2 = classId2;
		_name1 = name1;
		_name2 = name2;
		_gameStartTime = gameStartTime;
		_gameTime = gameTime;
		_gameStatus = gameStatus;
		_gameType = gameType;
	}
	
	/**
	 * Method getGameTime.
	 * @return int
	 */
	public int getGameTime()
	{
		return _gameTime;
	}
	
	/**
	 * Method getGameStatus.
	 * @return int
	 */
	public int getGameStatus()
	{
		return _gameStatus;
	}
	
	/**
	 * Method getGameType.
	 * @return int
	 */
	public int getGameType()
	{
		return _gameType;
	}
	
	/**
	 * Method getGameStartTime.
	 * @return long
	 */
	public long getGameStartTime()
	{
		return _gameStartTime;
	}
	
	/**
	 * Method toString.
	 * @param player Player
	 * @param target int
	 * @param wins int
	 * @param loss int
	 * @param tie int
	 * @return String
	 */
	public String toString(Player player, int target, int wins, int loss, int tie)
	{
		int team = _objectId1 == target ? 1 : 2;
		String main = null;
		if (_gameStatus == 0)
		{
			main = StringHolder.getInstance().getNotNull(player, "hero.history.tie");
		}
		else if (team == _gameStatus)
		{
			main = StringHolder.getInstance().getNotNull(player, "hero.history.win");
		}
		else
		{
			main = StringHolder.getInstance().getNotNull(player, "hero.history.loss");
		}
		main = main.replace("%classId%", String.valueOf(team == 1 ? _classId2 : _classId1));
		main = main.replace("%name%", team == 1 ? _name2 : _name1);
		main = main.replace("%date%", TimeUtils.toSimpleFormat(_gameStartTime));
		int m = _gameTime / 60;
		int s = _gameTime % 60;
		main = main.replace("%time%", (m <= 9 ? "0" : "") + m + ":" + (s <= 9 ? "0" : "") + s);
		main = main.replace("%victory_count%", String.valueOf(wins));
		main = main.replace("%tie_count%", String.valueOf(tie));
		main = main.replace("%loss_count%", String.valueOf(loss));
		return main;
	}
	
	/**
	 * Method getObjectId1.
	 * @return int
	 */
	public int getObjectId1()
	{
		return _objectId1;
	}
	
	/**
	 * Method getObjectId2.
	 * @return int
	 */
	public int getObjectId2()
	{
		return _objectId2;
	}
	
	/**
	 * Method getClassId1.
	 * @return int
	 */
	public int getClassId1()
	{
		return _classId1;
	}
	
	/**
	 * Method getClassId2.
	 * @return int
	 */
	public int getClassId2()
	{
		return _classId2;
	}
	
	/**
	 * Method getName1.
	 * @return String
	 */
	public String getName1()
	{
		return _name1;
	}
	
	/**
	 * Method getName2.
	 * @return String
	 */
	public String getName2()
	{
		return _name2;
	}
}
