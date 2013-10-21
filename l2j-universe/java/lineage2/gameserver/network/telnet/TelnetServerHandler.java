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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lineage2.gameserver.Config;
import lineage2.gameserver.network.telnet.commands.TelnetBan;
import lineage2.gameserver.network.telnet.commands.TelnetConfig;
import lineage2.gameserver.network.telnet.commands.TelnetDebug;
import lineage2.gameserver.network.telnet.commands.TelnetGive;
import lineage2.gameserver.network.telnet.commands.TelnetPerfomance;
import lineage2.gameserver.network.telnet.commands.TelnetSay;
import lineage2.gameserver.network.telnet.commands.TelnetServer;
import lineage2.gameserver.network.telnet.commands.TelnetStatus;
import lineage2.gameserver.network.telnet.commands.TelnetWorld;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TelnetServerHandler extends SimpleChannelUpstreamHandler implements TelnetCommandHolder
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TelnetServerHandler.class);
	/**
	 * Field COMMAND_ARGS_PATTERN.
	 */
	private static final Pattern COMMAND_ARGS_PATTERN = Pattern.compile("\"([^\"]*)\"|([^\\s]+)");
	/**
	 * Field _commands.
	 */
	final Set<TelnetCommand> _commands = new LinkedHashSet<>();
	
	/**
	 * Constructor for TelnetServerHandler.
	 */
	public TelnetServerHandler()
	{
		_commands.add(new TelnetCommand("help", "h")
		{
			@Override
			public String getUsage()
			{
				return "help [command]";
			}
			
			@Override
			public String handle(String[] args)
			{
				if (args.length == 0)
				{
					StringBuilder sb = new StringBuilder();
					sb.append("Available commands:\n");
					for (TelnetCommand cmd : _commands)
					{
						sb.append(cmd.getCommand()).append('\n');
					}
					return sb.toString();
				}
				TelnetCommand cmd = TelnetServerHandler.this.getCommand(args[0]);
				if (cmd == null)
				{
					return "Unknown command.\n";
				}
				return "usage:\n" + cmd.getUsage() + "\n";
			}
		});
		addHandler(new TelnetBan());
		addHandler(new TelnetConfig());
		addHandler(new TelnetDebug());
		addHandler(new TelnetGive());
		addHandler(new TelnetPerfomance());
		addHandler(new TelnetSay());
		addHandler(new TelnetServer());
		addHandler(new TelnetStatus());
		addHandler(new TelnetWorld());
	}
	
	/**
	 * Method addHandler.
	 * @param handler TelnetCommandHolder
	 */
	public void addHandler(TelnetCommandHolder handler)
	{
		for (TelnetCommand cmd : handler.getCommands())
		{
			_commands.add(cmd);
		}
	}
	
	/**
	 * Method getCommands.
	 * @return Set<TelnetCommand> * @see lineage2.gameserver.network.telnet.TelnetCommandHolder#getCommands()
	 */
	@Override
	public Set<TelnetCommand> getCommands()
	{
		return _commands;
	}
	
	/**
	 * Method getCommand.
	 * @param command String
	 * @return TelnetCommand
	 */
	TelnetCommand getCommand(String command)
	{
		for (TelnetCommand cmd : _commands)
		{
			if (cmd.equals(command))
			{
				return cmd;
			}
		}
		return null;
	}
	
	/**
	 * Method tryHandleCommand.
	 * @param command String
	 * @param args String[]
	 * @return String
	 */
	private String tryHandleCommand(String command, String[] args)
	{
		TelnetCommand cmd = getCommand(command);
		if (cmd == null)
		{
			return "Unknown command.\n";
		}
		String response = cmd.handle(args);
		if (response == null)
		{
			response = "usage:\n" + cmd.getUsage() + "\n";
		}
		return response;
	}
	
	/**
	 * Method channelConnected.
	 * @param ctx ChannelHandlerContext
	 * @param e ChannelStateEvent
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		e.getChannel().write("Welcome to Lineage2 GameServer telnet console.\n");
		e.getChannel().write("It is " + new Date() + " now.\n");
		if (!Config.TELNET_PASSWORD.isEmpty())
		{
			e.getChannel().write("Password:");
			ctx.setAttachment(Boolean.FALSE);
		}
		else
		{
			e.getChannel().write("Type 'help' to see all available commands.\n");
			ctx.setAttachment(Boolean.TRUE);
		}
	}
	
	/**
	 * Method messageReceived.
	 * @param ctx ChannelHandlerContext
	 * @param e MessageEvent
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	{
		String request = e.getMessage().toString();
		String response = null;
		boolean close = false;
		if (Boolean.FALSE.equals(ctx.getAttachment()))
		{
			if (Config.TELNET_PASSWORD.equals(request))
			{
				ctx.setAttachment(Boolean.TRUE);
				request = "";
			}
			else
			{
				response = "Wrong password!\n";
			}
		}
		if (Boolean.TRUE.equals(ctx.getAttachment()))
		{
			if (request.isEmpty())
			{
				response = "Type 'help' to see all available commands.\n";
			}
			else if (request.toLowerCase().equals("exit"))
			{
				response = "Have a good day!\n";
				close = true;
			}
			else
			{
				Matcher m = COMMAND_ARGS_PATTERN.matcher(request);
				m.find();
				String command = m.group();
				List<String> args = new ArrayList<>();
				String arg;
				while (m.find())
				{
					arg = m.group(1);
					if (arg == null)
					{
						arg = m.group(0);
					}
					args.add(arg);
				}
				response = tryHandleCommand(command, args.toArray(new String[args.size()]));
			}
		}
		ChannelFuture future = e.getChannel().write(response);
		if (close)
		{
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	/**
	 * Method exceptionCaught.
	 * @param ctx ChannelHandlerContext
	 * @param e ExceptionEvent
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		if (e.getCause() instanceof IOException)
		{
			e.getChannel().close();
		}
		else
		{
			_log.error("", e.getCause());
		}
	}
}
