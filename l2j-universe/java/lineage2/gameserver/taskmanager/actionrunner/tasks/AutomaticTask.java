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
package lineage2.gameserver.taskmanager.actionrunner.tasks;

import lineage2.gameserver.taskmanager.actionrunner.ActionRunner;
import lineage2.gameserver.taskmanager.actionrunner.ActionWrapper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class AutomaticTask extends ActionWrapper
{
	/**
	 * Field TASKS. (value is ""automatic_tasks"")
	 */
	public static final String TASKS = "automatic_tasks";
	
	/**
	 * Constructor for AutomaticTask.
	 */
	public AutomaticTask()
	{
		super(TASKS);
	}
	
	/**
	 * Method doTask.
	 * @throws Exception
	 */
	public abstract void doTask() throws Exception;
	
	/**
	 * Method reCalcTime.
	 * @param start boolean
	 * @return long
	 */
	public abstract long reCalcTime(boolean start);
	
	/**
	 * Method runImpl0.
	 * @throws Exception
	 */
	@Override
	public void runImpl0() throws Exception
	{
		try
		{
			doTask();
		}
		finally
		{
			ActionRunner.getInstance().register(reCalcTime(false), this);
		}
	}
}
