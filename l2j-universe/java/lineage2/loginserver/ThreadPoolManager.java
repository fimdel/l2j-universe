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
package lineage2.loginserver;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lineage2.commons.threading.RunnableImpl;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ThreadPoolManager
{
	/**
	 * Field MAX_DELAY.
	 */
	private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;
	/**
	 * Field _instance.
	 */
	private static final ThreadPoolManager _instance = new ThreadPoolManager();
	
	/**
	 * Method getInstance.
	 * @return ThreadPoolManager
	 */
	public static final ThreadPoolManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field scheduledExecutor.
	 */
	final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);
	/**
	 * Field executor.
	 */
	final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	/**
	 * Constructor for ThreadPoolManager.
	 */
	private ThreadPoolManager()
	{
		scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				executor.purge();
				scheduledExecutor.purge();
			}
		}, 600000L, 600000L);
	}
	
	/**
	 * Method validate.
	 * @param delay long
	 * @return long
	 */
	private final long validate(long delay)
	{
		return Math.max(0, Math.min(MAX_DELAY, delay));
	}
	
	/**
	 * Method execute.
	 * @param r Runnable
	 */
	public void execute(Runnable r)
	{
		executor.execute(r);
	}
	
	/**
	 * Method schedule.
	 * @param r Runnable
	 * @param delay long
	 * @return ScheduledFuture<?>
	 */
	public ScheduledFuture<?> schedule(Runnable r, long delay)
	{
		return scheduledExecutor.schedule(r, validate(delay), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Method scheduleAtFixedRate.
	 * @param r Runnable
	 * @param initial long
	 * @param delay long
	 * @return ScheduledFuture<?>
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initial, long delay)
	{
		return scheduledExecutor.scheduleAtFixedRate(r, validate(initial), validate(delay), TimeUnit.MILLISECONDS);
	}
}
