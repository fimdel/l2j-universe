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
package lineage2.gameserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GarbageCollector
{
	/**
	 * Field _log.
	 */
	static final Logger _log = LoggerFactory.getLogger(GarbageCollector.class);
	static
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new GarbageCollectorTask(), Config.GARBAGE_COLLECTOR_INTERVAL, Config.GARBAGE_COLLECTOR_INTERVAL);
	}
	
	/**
	 * @author Mobius
	 */
	static class GarbageCollectorTask implements Runnable
	{
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			_log.info("GarbageCollector: start");
			System.gc();
			System.runFinalization();
			_log.info("GarbageCollector: finish");
		}
	}
}
