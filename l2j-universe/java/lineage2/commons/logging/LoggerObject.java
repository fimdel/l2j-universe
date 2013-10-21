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
package lineage2.commons.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class LoggerObject
{
	/**
	 * Field _log.
	 */
	protected final Logger _log = LoggerFactory.getLogger(getClass());
	
	/**
	 * Method error.
	 * @param st String
	 * @param e Exception
	 */
	public void error(String st, Exception e)
	{
		_log.error(getClass().getSimpleName() + ": " + st, e);
	}
	
	/**
	 * Method error.
	 * @param st String
	 */
	public void error(String st)
	{
		_log.error(getClass().getSimpleName() + ": " + st);
	}
	
	/**
	 * Method warn.
	 * @param st String
	 * @param e Exception
	 */
	public void warn(String st, Exception e)
	{
		_log.warn(getClass().getSimpleName() + ": " + st, e);
	}
	
	/**
	 * Method warn.
	 * @param st String
	 */
	public void warn(String st)
	{
		_log.warn(getClass().getSimpleName() + ": " + st);
	}
	
	/**
	 * Method info.
	 * @param st String
	 * @param e Exception
	 */
	public void info(String st, Exception e)
	{
		_log.info(getClass().getSimpleName() + ": " + st, e);
	}
	
	/**
	 * Method info.
	 * @param st String
	 */
	public void info(String st)
	{
		_log.info(getClass().getSimpleName() + ": " + st);
	}
}
