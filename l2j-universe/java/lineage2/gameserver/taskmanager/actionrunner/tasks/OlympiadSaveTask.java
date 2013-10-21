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
package lineage2.gameserver.taskmanager.actionrunner.tasks;

import lineage2.gameserver.model.entity.olympiad.OlympiadDatabase;

import org.apache.log4j.Logger;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadSaveTask extends AutomaticTask
{
	/**
	 * Field INTERVAL. (value is 7200000)
	 */
	private static final long INTERVAL = 7200000L;
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(OlympiadSaveTask.class);
	
	/**
	 * Constructor for OlympiadSaveTask.
	 */
	public OlympiadSaveTask()
	{
		super();
	}
	
	/**
	 * Method doTask.
	 */
	@Override
	public void doTask()
	{
		System.currentTimeMillis();
		OlympiadDatabase.save();
	}
	
	/**
	 * Method reCalcTime.
	 * @param start boolean
	 * @return long
	 */
	@Override
	public long reCalcTime(boolean start)
	{
		return System.currentTimeMillis() + INTERVAL;
	}
}
