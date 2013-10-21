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
package lineage2.gameserver.taskmanager;

import static lineage2.gameserver.taskmanager.TaskTypes.TYPE_FIXED_SHEDULED;
import static lineage2.gameserver.taskmanager.TaskTypes.TYPE_GLOBAL_TASK;
import static lineage2.gameserver.taskmanager.TaskTypes.TYPE_NONE;
import static lineage2.gameserver.taskmanager.TaskTypes.TYPE_SHEDULED;
import static lineage2.gameserver.taskmanager.TaskTypes.TYPE_SPECIAL;
import static lineage2.gameserver.taskmanager.TaskTypes.TYPE_STARTUP;
import static lineage2.gameserver.taskmanager.TaskTypes.TYPE_TIME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.taskmanager.tasks.SoIStageUpdater;
import lineage2.gameserver.taskmanager.tasks.TaskRecom;
import lineage2.gameserver.taskmanager.tasks.TaskVitalitySystem;
import lineage2.gameserver.taskmanager.tasks.WorldStatisticUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class TaskManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TaskManager.class);
	/**
	 * Field _instance.
	 */
	private static TaskManager _instance;
	/**
	 * Field SQL_STATEMENTS.
	 */
	static final String[] SQL_STATEMENTS =
	{
		"SELECT id,task,type,last_activation,param1,param2,param3 FROM global_tasks",
		"UPDATE global_tasks SET last_activation=? WHERE id=?",
		"SELECT id FROM global_tasks WHERE task=?",
		"INSERT INTO global_tasks (task,type,last_activation,param1,param2,param3) VALUES(?,?,?,?,?,?)"
	};
	/**
	 * Field _tasks.
	 */
	private final Map<Integer, Task> _tasks = new ConcurrentHashMap<>();
	/**
	 * Field _currentTasks.
	 */
	final List<ExecutedTask> _currentTasks = new ArrayList<>();
	
	/**
	 * @author Mobius
	 */
	public class ExecutedTask extends RunnableImpl
	{
		/**
		 * Field _id.
		 */
		int _id;
		/**
		 * Field _lastActivation.
		 */
		long _lastActivation;
		/**
		 * Field _task.
		 */
		Task _task;
		/**
		 * Field _type.
		 */
		TaskTypes _type;
		/**
		 * Field _params.
		 */
		String[] _params;
		/**
		 * Field _scheduled.
		 */
		ScheduledFuture<?> _scheduled;
		
		/**
		 * Constructor for ExecutedTask.
		 * @param task Task
		 * @param type TaskTypes
		 * @param rset ResultSet
		 * @throws SQLException
		 */
		public ExecutedTask(Task task, TaskTypes type, ResultSet rset) throws SQLException
		{
			_task = task;
			_type = type;
			_id = rset.getInt("id");
			_lastActivation = rset.getLong("last_activation") * 1000L;
			_params = new String[]
			{
				rset.getString("param1"),
				rset.getString("param2"),
				rset.getString("param3")
			};
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_task.onTimeElapsed(this);
			_lastActivation = System.currentTimeMillis();
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement(SQL_STATEMENTS[1]);
				statement.setLong(1, _lastActivation / 1000);
				statement.setInt(2, _id);
				statement.executeUpdate();
			}
			catch (SQLException e)
			{
				_log.warn("cannot updated the Global Task " + _id + ": " + e.getMessage());
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
			if ((_type == TYPE_SHEDULED) || (_type == TYPE_TIME))
			{
				stopTask();
			}
		}
		
		/**
		 * Method equals.
		 * @param object Object
		 * @return boolean
		 */
		@Override
		public boolean equals(Object object)
		{
			return _id == ((ExecutedTask) object)._id;
		}
		
		/**
		 * Method getTask.
		 * @return Task
		 */
		public Task getTask()
		{
			return _task;
		}
		
		/**
		 * Method getType.
		 * @return TaskTypes
		 */
		public TaskTypes getType()
		{
			return _type;
		}
		
		/**
		 * Method getId.
		 * @return int
		 */
		public int getId()
		{
			return _id;
		}
		
		/**
		 * Method getParams.
		 * @return String[]
		 */
		public String[] getParams()
		{
			return _params;
		}
		
		/**
		 * Method getLastActivation.
		 * @return long
		 */
		public long getLastActivation()
		{
			return _lastActivation;
		}
		
		/**
		 * Method stopTask.
		 */
		public void stopTask()
		{
			_task.onDestroy();
			if (_scheduled != null)
			{
				_scheduled.cancel(false);
			}
			_currentTasks.remove(this);
		}
	}
	
	/**
	 * Method getInstance.
	 * @return TaskManager
	 */
	public static TaskManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new TaskManager();
		}
		return _instance;
	}
	
	/**
	 * Constructor for TaskManager.
	 */
	public TaskManager()
	{
		initializate();
		startAllTasks();
	}
	
	/**
	 * Method initializate.
	 */
	private void initializate()
	{
		registerTask(new TaskRecom());
		registerTask(new SoIStageUpdater());
		registerTask(new TaskVitalitySystem());
		registerTask(new WorldStatisticUpdate());
	}
	
	/**
	 * Method registerTask.
	 * @param task Task
	 */
	public void registerTask(Task task)
	{
		int key = task.getName().hashCode();
		if (!_tasks.containsKey(key))
		{
			_tasks.put(key, task);
			task.initializate();
		}
	}
	
	/**
	 * Method startAllTasks.
	 */
	private void startAllTasks()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SQL_STATEMENTS[0]);
			rset = statement.executeQuery();
			while (rset.next())
			{
				Task task = _tasks.get(rset.getString("task").trim().toLowerCase().hashCode());
				if (task == null)
				{
					continue;
				}
				TaskTypes type = TaskTypes.valueOf(rset.getString("type"));
				if (type != TYPE_NONE)
				{
					ExecutedTask current = new ExecutedTask(task, type, rset);
					if (launchTask(current))
					{
						_currentTasks.add(current);
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.error("error while loading Global Task table " + e);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method launchTask.
	 * @param task ExecutedTask
	 * @return boolean
	 */
	private boolean launchTask(ExecutedTask task)
	{
		final ThreadPoolManager scheduler = ThreadPoolManager.getInstance();
		final TaskTypes type = task.getType();
		if (type == TYPE_STARTUP)
		{
			task.run();
			return false;
		}
		else if (type == TYPE_SHEDULED)
		{
			long delay = Long.valueOf(task.getParams()[0]);
			task._scheduled = scheduler.schedule(task, delay);
			return true;
		}
		else if (type == TYPE_FIXED_SHEDULED)
		{
			long delay = Long.valueOf(task.getParams()[0]);
			long interval = Long.valueOf(task.getParams()[1]);
			task._scheduled = scheduler.scheduleAtFixedRate(task, delay, interval);
			return true;
		}
		else if (type == TYPE_TIME)
		{
			try
			{
				Date desired = DateFormat.getInstance().parse(task.getParams()[0]);
				long diff = desired.getTime() - System.currentTimeMillis();
				if (diff >= 0)
				{
					task._scheduled = scheduler.schedule(task, diff);
					return true;
				}
				_log.info("Task " + task.getId() + " is obsoleted.");
			}
			catch (Exception e)
			{
			}
		}
		else if (type == TYPE_SPECIAL)
		{
			ScheduledFuture<?> result = task.getTask().launchSpecial(task);
			if (result != null)
			{
				task._scheduled = result;
				return true;
			}
		}
		else if (type == TYPE_GLOBAL_TASK)
		{
			long interval = Long.valueOf(task.getParams()[0]) * 86400000L;
			String[] hour = task.getParams()[1].split(":");
			if (hour.length != 3)
			{
				_log.warn("Task " + task.getId() + " has incorrect parameters");
				return false;
			}
			Calendar check = Calendar.getInstance();
			check.setTimeInMillis(task.getLastActivation() + interval);
			Calendar min = Calendar.getInstance();
			try
			{
				min.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour[0]));
				min.set(Calendar.MINUTE, Integer.valueOf(hour[1]));
				min.set(Calendar.SECOND, Integer.valueOf(hour[2]));
			}
			catch (Exception e)
			{
				_log.warn("Bad parameter on task " + task.getId() + ": " + e.getMessage());
				return false;
			}
			long delay = min.getTimeInMillis() - System.currentTimeMillis();
			if (check.after(min) || (delay < 0))
			{
				delay += interval;
			}
			task._scheduled = scheduler.scheduleAtFixedRate(task, delay, interval);
			return true;
		}
		return false;
	}
	
	/**
	 * Method addUniqueTask.
	 * @param task String
	 * @param type TaskTypes
	 * @param param1 String
	 * @param param2 String
	 * @param param3 String
	 * @return boolean
	 */
	public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3)
	{
		return addUniqueTask(task, type, param1, param2, param3, 0);
	}
	
	/**
	 * Method addUniqueTask.
	 * @param task String
	 * @param type TaskTypes
	 * @param param1 String
	 * @param param2 String
	 * @param param3 String
	 * @param lastActivation long
	 * @return boolean
	 */
	public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SQL_STATEMENTS[2]);
			statement.setString(1, task);
			rset = statement.executeQuery();
			boolean exists = rset.next();
			DbUtils.close(statement, rset);
			if (!exists)
			{
				statement = con.prepareStatement(SQL_STATEMENTS[3]);
				statement.setString(1, task);
				statement.setString(2, type.toString());
				statement.setLong(3, lastActivation / 1000);
				statement.setString(4, param1);
				statement.setString(5, param2);
				statement.setString(6, param3);
				statement.execute();
			}
			return true;
		}
		catch (SQLException e)
		{
			_log.warn("cannot add the unique task: " + e.getMessage());
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return false;
	}
	
	/**
	 * Method addTask.
	 * @param task String
	 * @param type TaskTypes
	 * @param param1 String
	 * @param param2 String
	 * @param param3 String
	 * @return boolean
	 */
	public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3)
	{
		return addTask(task, type, param1, param2, param3, 0);
	}
	
	/**
	 * Method addTask.
	 * @param task String
	 * @param type TaskTypes
	 * @param param1 String
	 * @param param2 String
	 * @param param3 String
	 * @param lastActivation long
	 * @return boolean
	 */
	public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SQL_STATEMENTS[3]);
			statement.setString(1, task);
			statement.setString(2, type.toString());
			statement.setLong(3, lastActivation / 1000);
			statement.setString(4, param1);
			statement.setString(5, param2);
			statement.setString(6, param3);
			statement.execute();
			return true;
		}
		catch (SQLException e)
		{
			_log.warn("cannot add the task:	" + e.getMessage());
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return false;
	}
}
