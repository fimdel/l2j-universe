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
package lineage2.gameserver.model.entity.events.impl;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.GlobalEvent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class UndergroundColiseumEvent extends GlobalEvent
{
	/**
	 * Field DATE_PATTERN.
	 */
	private static final SchedulingPattern DATE_PATTERN = new SchedulingPattern("0 21 * * mon,sat,sun");
	/**
	 * Field _startCalendar.
	 */
	private final Calendar _startCalendar = Calendar.getInstance();
	/**
	 * Field _registeredPlayers.
	 */
	private final List<Player> _registeredPlayers = new CopyOnWriteArrayList<>();
	/**
	 * Field _minLevel.
	 */
	private final int _minLevel;
	/**
	 * Field _maxLevel.
	 */
	private final int _maxLevel;
	/**
	 * Field _battleEvent.
	 */
	@SuppressWarnings("unused")
	private UndergroundColiseumBattleEvent _battleEvent;
	
	/**
	 * Constructor for UndergroundColiseumEvent.
	 * @param set MultiValueSet<String>
	 */
	public UndergroundColiseumEvent(MultiValueSet<String> set)
	{
		super(set);
		_minLevel = set.getInteger("min_level");
		_maxLevel = set.getInteger("max_level");
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		super.startEvent();
	}
	
	/**
	 * Method stopEvent.
	 */
	@Override
	public void stopEvent()
	{
		super.stopEvent();
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();
		_startCalendar.setTimeInMillis(DATE_PATTERN.next(System.currentTimeMillis()));
		registerActions();
	}
	
	/**
	 * Method startTimeMillis.
	 * @return long
	 */
	@Override
	protected long startTimeMillis()
	{
		return _startCalendar.getTimeInMillis();
	}
	
	/**
	 * Method getRegisteredPlayers.
	 * @return List<Player>
	 */
	public List<Player> getRegisteredPlayers()
	{
		return _registeredPlayers;
	}
	
	/**
	 * Method getMinLevel.
	 * @return int
	 */
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public int getMaxLevel()
	{
		return _maxLevel;
	}
}
