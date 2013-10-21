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
package lineage2.gameserver.taskmanager.actionrunner;

import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class ActionWrapper extends RunnableImpl
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ActionWrapper.class);
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _scheduledFuture.
	 */
	private Future<?> _scheduledFuture;
	
	/**
	 * Constructor for ActionWrapper.
	 * @param name String
	 */
	public ActionWrapper(String name)
	{
		_name = name;
	}
	
	/**
	 * Method schedule.
	 * @param time long
	 */
	public void schedule(long time)
	{
		_scheduledFuture = ThreadPoolManager.getInstance().schedule(this, time);
	}
	
	/**
	 * Method cancel.
	 */
	public void cancel()
	{
		if (_scheduledFuture != null)
		{
			_scheduledFuture.cancel(true);
			_scheduledFuture = null;
		}
	}
	
	/**
	 * Method runImpl0.
	 * @throws Exception
	 */
	public abstract void runImpl0() throws Exception;
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		try
		{
			runImpl0();
		}
		catch (Exception e)
		{
			_log.info("ActionWrapper: Exception: " + e + "; name: " + _name, e);
		}
		finally
		{
			ActionRunner.getInstance().remove(_name, this);
			_scheduledFuture = null;
		}
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
}
