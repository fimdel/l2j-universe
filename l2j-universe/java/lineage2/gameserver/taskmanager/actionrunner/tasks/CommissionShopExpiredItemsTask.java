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

import lineage2.gameserver.instancemanager.commission.CommissionShopManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommissionShopExpiredItemsTask extends AutomaticTask
{
	/**
	 * Method doTask.
	 */
	@Override
	public void doTask()
	{
		CommissionShopManager.getInstance().returnExpiredItems();
	}
	
	/**
	 * Method reCalcTime.
	 * @param start boolean
	 * @return long
	 */
	@Override
	public long reCalcTime(boolean start)
	{
		return System.currentTimeMillis() + 600000L;
	}
}
