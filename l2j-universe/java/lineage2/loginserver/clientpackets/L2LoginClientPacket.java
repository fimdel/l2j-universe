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
package lineage2.loginserver.clientpackets;

import lineage2.commons.net.nio.impl.ReceivablePacket;
import lineage2.loginserver.L2LoginClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class L2LoginClientPacket extends ReceivablePacket<L2LoginClient>
{
	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(L2LoginClientPacket.class);
	
	/**
	 * Method read.
	 * @return boolean
	 */
	@Override
	protected final boolean read()
	{
		try
		{
			readImpl();
			return true;
		}
		catch (Exception e)
		{
			_log.error("", e);
			return false;
		}
	}
	
	/**
	 * Method run.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		try
		{
			runImpl();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method readImpl.
	 */
	protected abstract void readImpl();
	
	/**
	 * Method runImpl.
	 * @throws Exception
	 */
	protected abstract void runImpl() throws Exception;
}
