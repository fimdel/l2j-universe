package l2p.gameserver.taskmanager.tasks;

import l2p.gameserver.instancemanager.WorldStatisticsManager;
import l2p.gameserver.taskmanager.Task;
import l2p.gameserver.taskmanager.TaskManager;
import l2p.gameserver.taskmanager.TaskTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 02.11.12
 * Time: 10:52
 */
public class WorldStatisticUpdate extends Task {
    private static final Logger _log = LoggerFactory.getLogger(WorldStatisticUpdate.class);
    private static final String NAME = "world_statistic_update";
    // День месяца, в который происходит обновление
    private static final int DAY_OF_MONTH = 1;

    @Override
    public void initializate() {
        TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onTimeElapsed(TaskManager.ExecutedTask task) {
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == DAY_OF_MONTH) {
            _log.info("World statistic task: launched.");
            WorldStatisticsManager.getInstance().resetMonthlyStatistic();
            _log.info("World statistic task: completed.");
        }
    }
}
