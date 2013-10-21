package lineage2.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.taskmanager.Task;
import lineage2.gameserver.taskmanager.TaskManager;
import lineage2.gameserver.taskmanager.TaskTypes;

/**
 * @author Дмитрий
 * @date 11.10.12 23:37
 */
public class WorldStatisticUpdate extends Task
{
	private static final Logger _log = LoggerFactory.getLogger(WorldStatisticUpdate.class);
	private static final String NAME = "world_statistic_update";
	private static final int DAY_OF_MONTH = 1;

	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public void onTimeElapsed(TaskManager.ExecutedTask task)
	{
		if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == DAY_OF_MONTH)
		{
			_log.info("World statistic task: launched.");
			WorldStatisticsManager.getInstance().resetMonthlyStatistic();
			_log.info("World statistic task: completed.");
		}
	}
}
