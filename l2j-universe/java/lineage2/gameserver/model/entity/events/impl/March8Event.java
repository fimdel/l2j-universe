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

import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.model.entity.events.GlobalEvent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class March8Event extends GlobalEvent
{
	/**
	 * Field _calendar.
	 */
	private final Calendar _calendar = Calendar.getInstance();
	/**
	 * Field LENGTH.
	 */
	private static final long LENGTH = 7 * 24 * 60 * 60 * 1000L;
	
	/**
	 * Constructor for March8Event.
	 * @param set MultiValueSet<String>
	 */
	public March8Event(MultiValueSet<String> set)
	{
		super(set);
	}
	
	/**
	 * Method initEvent.
	 */
	@SuppressWarnings("unused")
	@Override
	public void initEvent()
	{
		if (false)
		{
			super.initEvent();
		}
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		super.startEvent();
		Announcements.getInstance().announceToAll("Test startEvent");
	}
	
	/**
	 * Method stopEvent.
	 */
	@Override
	public void stopEvent()
	{
		super.stopEvent();
		Announcements.getInstance().announceToAll("Test stopEvent");
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();
		if (onInit)
		{
			_calendar.set(Calendar.MONTH, Calendar.MARCH);
			_calendar.set(Calendar.DAY_OF_MONTH, 8);
			_calendar.set(Calendar.HOUR_OF_DAY, 0);
			_calendar.set(Calendar.MINUTE, 0);
			_calendar.set(Calendar.SECOND, 0);
			if ((_calendar.getTimeInMillis() + LENGTH) < System.currentTimeMillis())
			{
				_calendar.add(Calendar.YEAR, 1);
			}
		}
		else
		{
			_calendar.add(Calendar.YEAR, 1);
		}
		registerActions();
	}
	
	/**
	 * Method startTimeMillis.
	 * @return long
	 */
	@Override
	protected long startTimeMillis()
	{
		return _calendar.getTimeInMillis();
	}
}
