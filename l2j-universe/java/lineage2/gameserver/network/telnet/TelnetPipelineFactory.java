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

import static org.jboss.netty.channel.Channels.pipeline;

import java.nio.charset.Charset;

import lineage2.gameserver.Config;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TelnetPipelineFactory implements ChannelPipelineFactory
{
	/**
	 * Field handler.
	 */
	private final ChannelHandler handler;
	
	/**
	 * Constructor for TelnetPipelineFactory.
	 * @param handler ChannelHandler
	 */
	public TelnetPipelineFactory(ChannelHandler handler)
	{
		this.handler = handler;
	}
	
	/**
	 * Method getPipeline.
	 * @return ChannelPipeline * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline()
	{
		ChannelPipeline pipeline = pipeline();
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder(Charset.forName(Config.TELNET_DEFAULT_ENCODING)));
		pipeline.addLast("encoder", new StringEncoder(Charset.forName(Config.TELNET_DEFAULT_ENCODING)));
		pipeline.addLast("handler", handler);
		return pipeline;
	}
}
