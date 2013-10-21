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

import lineage2.gameserver.Config;
import lineage2.gameserver.network.telnet.TelnetCommand;
import lineage2.gameserver.network.telnet.TelnetCommandHolder;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TelnetConfig implements TelnetCommandHolder
{
	/**
	 * Field _commands.
	 */
	private final Set<TelnetCommand> _commands = new LinkedHashSet<>();
	
	/**
	 * Constructor for TelnetConfig.
	 */
	public TelnetConfig()
	{
		_commands.add(new TelnetCommand("config", "cfg")
		{
			@Override
			public String getUsage()
			{
				return "config parameter[=value]";
			}
			
			@Override
			public String handle(String[] args)
			{
				if ((args.length == 0) || args[0].isEmpty())
				{
					return null;
				}
				String[] val = args[0].split("=");
				if (val.length == 1)
				{
					String value = Config.getField(args[0]);
					return value == null ? "Not found.\n" : value + "\n";
				}
				if (Config.setField(val[0], val[1]))
				{
					return "Done.\n";
				}
				return "Error!\n";
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
