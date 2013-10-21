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
package lineage2.commons.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class RunnableImpl implements Runnable
{
	/**
	 * Field _log.
	 */
	public static final Logger _log = LoggerFactory.getLogger(RunnableImpl.class);
	
	/**
	 * Method runImpl.
	 * @throws Exception
	 */
	public abstract void runImpl() throws Exception;
	
	/**
	 * Method run.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run()
	{
		try
		{
			runImpl();
		}
		catch (Exception e)
		{
			_log.error("Exception: RunnableImpl.run(): " + e, e);
		}
	}
}
