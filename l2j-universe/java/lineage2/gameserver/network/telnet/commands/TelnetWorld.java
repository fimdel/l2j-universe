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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.telnet.TelnetCommand;
import lineage2.gameserver.network.telnet.TelnetCommandHolder;
import lineage2.gameserver.tables.GmListTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TelnetWorld implements TelnetCommandHolder
{
	/**
	 * Field _commands.
	 */
	private final Set<TelnetCommand> _commands = new LinkedHashSet<>();
	
	/**
	 * Constructor for TelnetWorld.
	 */
	public TelnetWorld()
	{
		_commands.add(new TelnetCommand("find")
		{
			@Override
			public String getUsage()
			{
				return "find <name>";
			}
			
			@Override
			public String handle(String[] args)
			{
				if (args.length == 0)
				{
					return null;
				}
				Iterable<Player> players = GameObjectsStorage.getAllPlayersForIterate();
				Iterator<Player> itr = players.iterator();
				StringBuilder sb = new StringBuilder();
				int count = 0;
				Player player;
				Pattern pattern = Pattern.compile(args[0] + "\\S+", Pattern.CASE_INSENSITIVE);
				while (itr.hasNext())
				{
					player = itr.next();
					if (pattern.matcher(player.getName()).matches())
					{
						count++;
						sb.append(player).append('\n');
					}
				}
				if (count == 0)
				{
					sb.append("Player not found.").append('\n');
				}
				else
				{
					sb.append("=================================================\n");
					sb.append("Found: ").append(count).append(" players.").append('\n');
				}
				return sb.toString();
			}
		});
		_commands.add(new TelnetCommand("whois", "who")
		{
			@Override
			public String getUsage()
			{
				return "whois <name>";
			}
			
			@Override
			public String handle(String[] args)
			{
				if (args.length == 0)
				{
					return null;
				}
				Player player = GameObjectsStorage.getPlayer(args[0]);
				if (player == null)
				{
					return "Player not found.\n";
				}
				StringBuilder sb = new StringBuilder();
				sb.append("Name: .................... ").append(player.getName()).append('\n');
				sb.append("ID: ...................... ").append(player.getObjectId()).append('\n');
				sb.append("Account Name: ............ ").append(player.getAccountName()).append('\n');
				sb.append("IP: ...................... ").append(player.getIP()).append('\n');
				sb.append("Level: ................... ").append(player.getLevel()).append('\n');
				sb.append("Location: ................ ").append(player.getLoc()).append('\n');
				if (player.getClan() != null)
				{
					sb.append("Clan: .................... ").append(player.getClan().getName()).append('\n');
					if (player.getAlliance() != null)
					{
						sb.append("Ally: .................... ").append(player.getAlliance().getAllyName()).append('\n');
					}
				}
				sb.append("Offline: ................. ").append(player.isInOfflineMode()).append('\n');
				sb.append(player.toString()).append('\n');
				return sb.toString();
			}
		});
		_commands.add(new TelnetCommand("gmlist", "gms")
		{
			@Override
			public String getUsage()
			{
				return "gmlist";
			}
			
			@Override
			public String handle(String[] args)
			{
				List<Player> gms = GmListTable.getAllGMs();
				int count = gms.size();
				if (count == 0)
				{
					return "GMs not found.\n";
				}
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < count; i++)
				{
					sb.append(gms.get(i)).append('\n');
				}
				sb.append("Found: ").append(count).append(" GMs.").append('\n');
				return sb.toString();
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
