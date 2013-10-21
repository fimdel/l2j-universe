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
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.network.serverpackets.SystemMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadEndTask extends RunnableImpl
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(OlympiadEndTask.class);
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		if (Olympiad._inCompPeriod)
		{
			ThreadPoolManager.getInstance().schedule(new OlympiadEndTask(), 60000);
			return;
		}
		Announcements.getInstance().announceToAll(new SystemMessage(SystemMessage.OLYMPIAD_PERIOD_S1_HAS_ENDED).addNumber(Olympiad._currentCycle));
		Announcements.getInstance().announceToAll("Olympiad Validation Period has began");
		Olympiad._isOlympiadEnd = true;
		if (Olympiad._scheduledManagerTask != null)
		{
			Olympiad._scheduledManagerTask.cancel(false);
		}
		if (Olympiad._scheduledWeeklyTask != null)
		{
			Olympiad._scheduledWeeklyTask.cancel(false);
		}
		Olympiad._validationEnd = Olympiad._olympiadEnd + Config.ALT_OLY_VPERIOD;
		OlympiadDatabase.saveNobleData();
		Olympiad._period = 1;
		Hero.getInstance().clearHeroes();
		try
		{
			OlympiadDatabase.save();
		}
		catch (Exception e)
		{
			_log.error("Olympiad System: Failed to save Olympiad configuration!", e);
		}
		_log.info("Olympiad System: Starting Validation period. Time to end validation:" + (Olympiad.getMillisToValidationEnd() / (60 * 1000)));
		if (Olympiad._scheduledValdationTask != null)
		{
			Olympiad._scheduledValdationTask.cancel(false);
		}
		Olympiad._scheduledValdationTask = ThreadPoolManager.getInstance().schedule(new ValidationTask(), Olympiad.getMillisToValidationEnd());
	}
}
