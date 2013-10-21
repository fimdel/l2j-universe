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
package lineage2.gameserver;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lineage2.commons.threading.LoggingRejectedExecutionHandler;
import lineage2.commons.threading.PriorityThreadFactory;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.threading.RunnableStatsWrapper;

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
	public static ThreadPoolManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _scheduledExecutor.
	 */
	final ScheduledThreadPoolExecutor _scheduledExecutor;
	/**
	 * Field _executor.
	 */
	final ThreadPoolExecutor _executor;
	/**
	 * Field _shutdown.
	 */
	private boolean _shutdown;
	
	/**
	 * Constructor for ThreadPoolManager.
	 */
	private ThreadPoolManager()
	{
		_scheduledExecutor = new ScheduledThreadPoolExecutor(Config.SCHEDULED_THREAD_POOL_SIZE, new PriorityThreadFactory("ScheduledThreadPool", Thread.NORM_PRIORITY), new LoggingRejectedExecutionHandler());
		_executor = new ThreadPoolExecutor(Config.EXECUTOR_THREAD_POOL_SIZE, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("ThreadPoolExecutor", Thread.NORM_PRIORITY), new LoggingRejectedExecutionHandler());
		scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				_scheduledExecutor.purge();
				_executor.purge();
			}
		}, 300000L, 300000L);
	}
	
	/**
	 * Method validate.
	 * @param delay long
	 * @return long
	 */
	private long validate(long delay)
	{
		return Math.max(0, Math.min(MAX_DELAY, delay));
	}
	
	/**
	 * Method isShutdown.
	 * @return boolean
	 */
	public boolean isShutdown()
	{
		return _shutdown;
	}
	
	/**
	 * Method wrap.
	 * @param r Runnable
	 * @return Runnable
	 */
	public Runnable wrap(Runnable r)
	{
		return Config.ENABLE_RUNNABLE_STATS ? RunnableStatsWrapper.wrap(r) : r;
	}
	
	/**
	 * Method schedule.
	 * @param r Runnable
	 * @param delay long
	 * @return ScheduledFuture<?>
	 */
	public ScheduledFuture<?> schedule(Runnable r, long delay)
	{
		return _scheduledExecutor.schedule(wrap(r), validate(delay), TimeUnit.MILLISECONDS);
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
		return _scheduledExecutor.scheduleAtFixedRate(wrap(r), validate(initial), validate(delay), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Method scheduleAtFixedDelay.
	 * @param r Runnable
	 * @param initial long
	 * @param delay long
	 * @return ScheduledFuture<?>
	 */
	public ScheduledFuture<?> scheduleAtFixedDelay(Runnable r, long initial, long delay)
	{
		return _scheduledExecutor.scheduleWithFixedDelay(wrap(r), validate(initial), validate(delay), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Method execute.
	 * @param r Runnable
	 */
	public void execute(Runnable r)
	{
		_executor.execute(wrap(r));
	}
	
	/**
	 * Method shutdown.
	 * @throws InterruptedException
	 */
	public void shutdown() throws InterruptedException
	{
		_shutdown = true;
		try
		{
			_scheduledExecutor.shutdown();
			_scheduledExecutor.awaitTermination(10, TimeUnit.SECONDS);
		}
		finally
		{
			_executor.shutdown();
			_executor.awaitTermination(1, TimeUnit.MINUTES);
		}
	}
	
	/**
	 * Method getStats.
	 * @return CharSequence
	 */
	public CharSequence getStats()
	{
		StringBuilder list = new StringBuilder();
		list.append("ScheduledThreadPool\n");
		list.append("=================================================\n");
		list.append("\tgetActiveCount: ...... ").append(_scheduledExecutor.getActiveCount()).append('\n');
		list.append("\tgetCorePoolSize: ..... ").append(_scheduledExecutor.getCorePoolSize()).append('\n');
		list.append("\tgetPoolSize: ......... ").append(_scheduledExecutor.getPoolSize()).append('\n');
		list.append("\tgetLargestPoolSize: .. ").append(_scheduledExecutor.getLargestPoolSize()).append('\n');
		list.append("\tgetMaximumPoolSize: .. ").append(_scheduledExecutor.getMaximumPoolSize()).append('\n');
		list.append("\tgetCompletedTaskCount: ").append(_scheduledExecutor.getCompletedTaskCount()).append('\n');
		list.append("\tgetQueuedTaskCount: .. ").append(_scheduledExecutor.getQueue().size()).append('\n');
		list.append("\tgetTaskCount: ........ ").append(_scheduledExecutor.getTaskCount()).append('\n');
		list.append("ThreadPoolExecutor\n");
		list.append("=================================================\n");
		list.append("\tgetActiveCount: ...... ").append(_executor.getActiveCount()).append('\n');
		list.append("\tgetCorePoolSize: ..... ").append(_executor.getCorePoolSize()).append('\n');
		list.append("\tgetPoolSize: ......... ").append(_executor.getPoolSize()).append('\n');
		list.append("\tgetLargestPoolSize: .. ").append(_executor.getLargestPoolSize()).append('\n');
		list.append("\tgetMaximumPoolSize: .. ").append(_executor.getMaximumPoolSize()).append('\n');
		list.append("\tgetCompletedTaskCount: ").append(_executor.getCompletedTaskCount()).append('\n');
		list.append("\tgetQueuedTaskCount: .. ").append(_executor.getQueue().size()).append('\n');
		list.append("\tgetTaskCount: ........ ").append(_executor.getTaskCount()).append('\n');
		return list;
	}
}
