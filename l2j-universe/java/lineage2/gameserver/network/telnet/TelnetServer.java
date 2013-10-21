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
package lineage2.gameserver.network.telnet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import lineage2.gameserver.Config;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TelnetServer
{
	/**
	 * Constructor for TelnetServer.
	 */
	public TelnetServer()
	{
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newFixedThreadPool(1), Executors.newFixedThreadPool(1), 1));
		TelnetServerHandler handler = new TelnetServerHandler();
		bootstrap.setPipelineFactory(new TelnetPipelineFactory(handler));
		bootstrap.bind(new InetSocketAddress(Config.TELNET_HOSTNAME.equals("*") ? null : Config.TELNET_HOSTNAME, Config.TELNET_PORT));
	}
}
