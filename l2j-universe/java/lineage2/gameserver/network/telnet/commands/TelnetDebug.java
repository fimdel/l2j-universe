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

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.telnet.TelnetCommand;
import lineage2.gameserver.network.telnet.TelnetCommandHolder;

import org.apache.commons.io.FileUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TelnetDebug implements TelnetCommandHolder
{
	/**
	 * Field _commands.
	 */
	private final Set<TelnetCommand> _commands = new LinkedHashSet<>();
	
	/**
	 * Constructor for TelnetDebug.
	 */
	public TelnetDebug()
	{
		_commands.add(new TelnetCommand("dumpnpc", "dnpc")
		{
			@Override
			public String getUsage()
			{
				return "dumpnpc";
			}
			
			@Override
			public String handle(String[] args)
			{
				StringBuilder sb = new StringBuilder();
				int total = 0;
				int maxId = 0, maxCount = 0;
				TIntObjectHashMap<List<NpcInstance>> npcStats = new TIntObjectHashMap<>();
				for (GameObject obj : GameObjectsStorage.getAllObjects())
				{
					if (obj.isCreature())
					{
						if (obj.isNpc())
						{
							List<NpcInstance> list;
							NpcInstance npc = (NpcInstance) obj;
							int id = npc.getNpcId();
							if ((list = npcStats.get(id)) == null)
							{
								npcStats.put(id, list = new ArrayList<>());
							}
							list.add(npc);
							if (list.size() > maxCount)
							{
								maxId = id;
								maxCount = list.size();
							}
							total++;
						}
					}
				}
				sb.append("Total NPCs: ").append(total).append('\n');
				sb.append("Maximum NPC ID: ").append(maxId).append(" count : ").append(maxCount).append('\n');
				TIntObjectIterator<List<NpcInstance>> itr = npcStats.iterator();
				while (itr.hasNext())
				{
					itr.advance();
					int id = itr.key();
					List<NpcInstance> list = itr.value();
					sb.append("=== ID: ").append(id).append(' ').append(" Count: ").append(list.size()).append(" ===").append('\n');
					for (NpcInstance npc : list)
					{
						try
						{
							sb.append("AI: ");
							if (npc.hasAI())
							{
								sb.append(npc.getAI().getClass().getName());
							}
							else
							{
								sb.append("none");
							}
							sb.append(", ");
							if (npc.getReflectionId() > 0)
							{
								sb.append("ref: ").append(npc.getReflectionId());
								sb.append(" - ").append(npc.getReflection().getName());
							}
							sb.append("loc: ").append(npc.getLoc());
							sb.append(", ");
							sb.append("spawned: ");
							sb.append(npc.isVisible());
							sb.append('\n');
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				try
				{
					new File("stats").mkdir();
					FileUtils.writeStringToFile(new File("stats/NpcStats-" + new SimpleDateFormat("MMddHHmmss").format(System.currentTimeMillis()) + ".txt"), sb.toString());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				return "NPC stats saved.\n";
			}
		});
		_commands.add(new TelnetCommand("asrestart")
		{
			@Override
			public String getUsage()
			{
				return "asrestart";
			}
			
			@Override
			public String handle(String[] args)
			{
				LoginServerCommunication.getInstance().restart();
				return "Restarted.\n";
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
