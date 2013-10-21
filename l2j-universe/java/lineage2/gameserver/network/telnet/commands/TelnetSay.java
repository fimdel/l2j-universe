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
package lineage2.gameserver.network.telnet.commands;

import java.util.LinkedHashSet;
import java.util.Set;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.telnet.TelnetCommand;
import lineage2.gameserver.network.telnet.TelnetCommandHolder;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TelnetSay implements TelnetCommandHolder
{
	/**
	 * Field _commands.
	 */
	private final Set<TelnetCommand> _commands = new LinkedHashSet<>();
	
	/**
	 * Constructor for TelnetSay.
	 */
	public TelnetSay()
	{
		_commands.add(new TelnetCommand("announce", "ann")
		{
			@Override
			public String getUsage()
			{
				return "announce <text>";
			}
			
			@Override
			public String handle(String[] args)
			{
				if (args.length == 0)
				{
					return null;
				}
				Announcements.getInstance().announceToAll(args[0]);
				return "Announcement sent.\n";
			}
		});
		_commands.add(new TelnetCommand("message", "msg")
		{
			@Override
			public String getUsage()
			{
				return "message <player> <text>";
			}
			
			@Override
			public String handle(String[] args)
			{
				if (args.length < 2)
				{
					return null;
				}
				Player player = World.getPlayer(args[0]);
				if (player == null)
				{
					return "Player not found.\n";
				}
				Say2 cs = new Say2(0, ChatType.TELL, "[Admin]", args[1]);
				player.sendPacket(cs);
				return "Message sent.\n";
			}
		});
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
}
