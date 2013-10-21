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
package lineage2.loginserver;

import java.nio.channels.SocketChannel;

import lineage2.commons.net.nio.impl.IAcceptFilter;
import lineage2.commons.net.nio.impl.IClientFactory;
import lineage2.commons.net.nio.impl.IMMOExecutor;
import lineage2.commons.net.nio.impl.MMOConnection;
import lineage2.commons.threading.RunnableImpl;
import lineage2.loginserver.serverpackets.Init;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SelectorHelper implements IMMOExecutor<L2LoginClient>, IClientFactory<L2LoginClient>, IAcceptFilter
{
	/**
	 * Method execute.
	 * @param r Runnable
	 * @see lineage2.commons.net.nio.impl.IMMOExecutor#execute(Runnable)
	 */
	@Override
	public void execute(Runnable r)
	{
		ThreadPoolManager.getInstance().execute(r);
	}
	
	/**
	 * Method create.
	 * @param con MMOConnection<L2LoginClient>
	 * @return L2LoginClient * @see lineage2.commons.net.nio.impl.IClientFactory#create(MMOConnection<L2LoginClient>)
	 */
	@Override
	public L2LoginClient create(MMOConnection<L2LoginClient> con)
	{
		final L2LoginClient client = new L2LoginClient(con);
		client.sendPacket(new Init(client));
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				client.closeNow(false);
			}
		}, Config.LOGIN_TIMEOUT);
		return client;
	}
	
	/**
	 * Method accept.
	 * @param sc SocketChannel
	 * @return boolean * @see lineage2.commons.net.nio.impl.IAcceptFilter#accept(SocketChannel)
	 */
	@Override
	public boolean accept(SocketChannel sc)
	{
		return !IpBanManager.getInstance().isIpBanned(sc.socket().getInetAddress().getHostAddress());
	}
}
