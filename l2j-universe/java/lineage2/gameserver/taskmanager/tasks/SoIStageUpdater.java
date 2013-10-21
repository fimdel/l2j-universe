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

import java.util.Calendar;

import lineage2.gameserver.instancemanager.SoIManager;
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
public class SoIStageUpdater extends Task
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SoIStageUpdater.class);
	/**
	 * Field NAME. (value is ""soi_update"")
	 */
	private static final String NAME = "soi_update";
	
	/**
	 * Method initializate.
	 */
	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask(getName(), TaskTypes.TYPE_GLOBAL_TASK, "1", "12:00:00", "");
	}
	
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
		if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
		{
			SoIManager.setCurrentStage(1);
			_log.info("Seed of Infinity update Task: Seed updated successfuly.");
		}
	}
}
