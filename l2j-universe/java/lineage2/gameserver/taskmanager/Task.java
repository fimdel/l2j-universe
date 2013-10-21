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

import java.util.concurrent.ScheduledFuture;

import lineage2.gameserver.taskmanager.TaskManager.ExecutedTask;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Task
{
	/**
	 * Method initializate.
	 */
	public abstract void initializate();
	
	/**
	 * Method launchSpecial.
	 * @param instance ExecutedTask
	 * @return ScheduledFuture<?>
	 */
	public ScheduledFuture<?> launchSpecial(ExecutedTask instance)
	{
		return null;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public abstract String getName();
	
	/**
	 * Method onTimeElapsed.
	 * @param task ExecutedTask
	 */
	public abstract void onTimeElapsed(ExecutedTask task);
	
	/**
	 * Method onDestroy.
	 */
	public void onDestroy()
	{
	}
}
