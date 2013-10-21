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
package lineage2.gameserver.taskmanager.tasks;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.taskmanager.Task;
import lineage2.gameserver.taskmanager.TaskManager;
import lineage2.gameserver.taskmanager.TaskManager.ExecutedTask;
import lineage2.gameserver.taskmanager.TaskTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TaskRecom extends Task
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TaskRecom.class);
	/**
	 * Field NAME. (value is ""sp_recommendations"")
	 */
	private static final String NAME = "sp_recommendations";
	
	/**
	 * Method getName.
	 * @return String
	 */
	@Override
	public String getName()
	{
		return NAME;
	}
	
	/**
	 * Method onTimeElapsed.
	 * @param task ExecutedTask
	 */
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		_log.info("Recommendation Global Task: launched.");
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.restartRecom();
		}
		_log.info("Recommendation Global Task: completed.");
	}
	
	/**
	 * Method initializate.
	 */
	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}
}
