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
package lineage2.gameserver.model.entity.olympiad;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.instancemanager.OlympiadHistoryManager;
import lineage2.gameserver.model.entity.Hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ValidationTask extends RunnableImpl
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ValidationTask.class);
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		OlympiadHistoryManager.getInstance().switchData();
		
		OlympiadDatabase.sortHerosToBe();
		OlympiadDatabase.saveNobleData();
		if (Hero.getInstance().computeNewHeroes(Olympiad._heroesToBe))
		{
			_log.warn("Olympiad: Error while computing new heroes!");
		}

		Olympiad._period = 0;
		Olympiad._currentCycle++;
		
		OlympiadDatabase.cleanupNobles();
		OlympiadDatabase.loadNoblesRank();
		OlympiadDatabase.setNewOlympiadEnd();
		
		Olympiad.init();
		OlympiadDatabase.save();
	}
}
