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
package lineage2.gameserver.handler.admincommands.impl;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.RaidBossSpawnManager;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.tables.CustomSpawnTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminSpawn implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_show_spawns.
		 */
		admin_show_spawns,
		/**
		 * Field admin_spawn.
		 */
		admin_spawn,
		/**
		 * Field admin_spawn_monster.
		 */
		admin_spawn_monster,
		/**
		 * Field admin_spawn_index.
		 */
		admin_spawn_index,
		/**
		 * Field admin_spawn1.
		 */
		admin_spawn1,
		/**
		 * Field admin_setheading.
		 */
		admin_setheading,
		/**
		 * Field admin_setai.
		 */
		admin_setai,
		/**
		 * Field admin_setaiparam.
		 */
		admin_setaiparam,
		/**
		 * Field admin_dumpparams.
		 */
		admin_dumpparams,
		/**
		 * Field admin_generate_loc.
		 */
		admin_generate_loc,
		/**
		 * Field admin_dumpspawn.
		 */
		admin_dumpspawn
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().CanEditNPC)
		{
			return false;
		}
		StringTokenizer st;
		NpcInstance target;
		Spawner spawn;
		NpcInstance npc;
		switch (command)
		{
			case admin_show_spawns:
				activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/spawns.htm"));
				break;
			case admin_spawn_index:
				try
				{
					String val = fullString.substring(18);
					activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/spawns/" + val + ".htm"));
				}
				catch (StringIndexOutOfBoundsException e)
				{
				}
				break;
			case admin_spawn1:
				st = new StringTokenizer(fullString, " ");
				try
				{
					st.nextToken();
					String id = st.nextToken();
					int mobCount = 1;
					if (st.hasMoreTokens())
					{
						mobCount = Integer.parseInt(st.nextToken());
					}
					spawnMonster(activeChar, id, 0, mobCount);
				}
				catch (Exception e)
				{
				}
				break;
			case admin_spawn:
			case admin_spawn_monster:
				st = new StringTokenizer(fullString, " ");
				try
				{
					st.nextToken();
					String id = st.nextToken();
					int respawnTime = 30;
					int mobCount = 1;
					if (st.hasMoreTokens())
					{
						mobCount = Integer.parseInt(st.nextToken());
					}
					if (st.hasMoreTokens())
					{
						respawnTime = Integer.parseInt(st.nextToken());
					}
					spawnMonster(activeChar, id, respawnTime, mobCount);
				}
				catch (Exception e)
				{
				}
				break;
			case admin_setai:
				if ((activeChar.getTarget() == null) || !activeChar.getTarget().isNpc())
				{
					activeChar.sendMessage("Please select target NPC or mob.");
					return false;
				}
				st = new StringTokenizer(fullString, " ");
				st.nextToken();
				if (!st.hasMoreTokens())
				{
					activeChar.sendMessage("Please specify AI name.");
					return false;
				}
				String aiName = st.nextToken();
				target = (NpcInstance) activeChar.getTarget();
				Constructor<?> aiConstructor = null;
				try
				{
					if (!aiName.equalsIgnoreCase("npc"))
					{
						aiConstructor = Class.forName("lineage2.gameserver.ai." + aiName).getConstructors()[0];
					}
				}
				catch (Exception e)
				{
					try
					{
						aiConstructor = Scripts.getInstance().getClasses().get("ai." + aiName).getConstructors()[0];
					}
					catch (Exception e1)
					{
						activeChar.sendMessage("This type AI not found.");
						return false;
					}
				}
				if (aiConstructor != null)
				{
					try
					{
						target.setAI((CharacterAI) aiConstructor.newInstance(new Object[]
						{
							target
						}));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					target.getAI().startAITask();
				}
				break;
			case admin_setaiparam:
				if ((activeChar.getTarget() == null) || !activeChar.getTarget().isNpc())
				{
					activeChar.sendMessage("Please select target NPC or mob.");
					return false;
				}
				st = new StringTokenizer(fullString, " ");
				st.nextToken();
				if (!st.hasMoreTokens())
				{
					activeChar.sendMessage("Please specify AI parameter name.");
					activeChar.sendMessage("USAGE: //setaiparam <param> <value>");
					return false;
				}
				String paramName = st.nextToken();
				if (!st.hasMoreTokens())
				{
					activeChar.sendMessage("Please specify AI parameter value.");
					activeChar.sendMessage("USAGE: //setaiparam <param> <value>");
					return false;
				}
				String paramValue = st.nextToken();
				target = (NpcInstance) activeChar.getTarget();
				target.setParameter(paramName, paramValue);
				target.decayMe();
				target.spawnMe();
				activeChar.sendMessage("AI parameter " + paramName + " succesfully setted to " + paramValue);
				break;
			case admin_dumpparams:
				if ((activeChar.getTarget() == null) || !activeChar.getTarget().isNpc())
				{
					activeChar.sendMessage("Please select target NPC or mob.");
					return false;
				}
				target = (NpcInstance) activeChar.getTarget();
				MultiValueSet<String> set = target.getParameters();
				if (!set.isEmpty())
				{
					System.out.println("Dump of Parameters:\r\n" + set.toString());
				}
				else
				{
					System.out.println("Parameters is empty.");
				}
				break;
			case admin_setheading:
				GameObject obj = activeChar.getTarget();
				if (!obj.isNpc())
				{
					activeChar.sendMessage("Target is incorrect!");
					return false;
				}
				npc = (NpcInstance) obj;
				npc.setHeading(activeChar.getHeading());
				npc.decayMe();
				npc.spawnMe();
				activeChar.sendMessage("New heading : " + activeChar.getHeading());
				spawn = npc.getSpawn();
				if (spawn == null)
				{
					activeChar.sendMessage("Spawn for this npc == null!");
					return false;
				}
				break;
			case admin_generate_loc:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("Incorrect argument count!");
					return false;
				}
				int id = Integer.parseInt(wordList[1]);
				int id2 = 0;
				if (wordList.length > 2)
				{
					id2 = Integer.parseInt(wordList[2]);
				}
				int min_x = Integer.MIN_VALUE;
				int min_y = Integer.MIN_VALUE;
				int min_z = Integer.MIN_VALUE;
				int max_x = Integer.MAX_VALUE;
				int max_y = Integer.MAX_VALUE;
				int max_z = Integer.MAX_VALUE;
				String name = "";
				for (NpcInstance _npc : World.getAroundNpc(activeChar))
				{
					if ((_npc.getNpcId() == id) || (_npc.getNpcId() == id2))
					{
						name = _npc.getName();
						min_x = Math.min(min_x, _npc.getX());
						min_y = Math.min(min_y, _npc.getY());
						min_z = Math.min(min_z, _npc.getZ());
						max_x = Math.max(max_x, _npc.getX());
						max_y = Math.max(max_y, _npc.getY());
						max_z = Math.max(max_z, _npc.getZ());
					}
				}
				min_x -= 500;
				min_y -= 500;
				max_x += 500;
				max_y += 500;
				System.out.println("(0,'" + name + "'," + min_x + "," + min_y + "," + min_z + "," + max_z + ",0),");
				System.out.println("(0,'" + name + "'," + min_x + "," + max_y + "," + min_z + "," + max_z + ",0),");
				System.out.println("(0,'" + name + "'," + max_x + "," + max_y + "," + min_z + "," + max_z + ",0),");
				System.out.println("(0,'" + name + "'," + max_x + "," + min_y + "," + min_z + "," + max_z + ",0),");
				System.out.println("delete from spawnlist where npc_templateid in (" + id + ", " + id2 + ")" + " and locx <= " + min_x + " and locy <= " + min_y + " and locz <= " + min_z + " and locx >= " + max_x + " and locy >= " + max_y + " and locz >= " + max_z + ";");
				break;
			case admin_dumpspawn:
				st = new StringTokenizer(fullString, " ");
				try
				{
					st.nextToken();
					String id3 = st.nextToken();
					int respawnTime = 30;
					int mobCount = 1;
					spawnMonster(activeChar, id3, respawnTime, mobCount);
					try
					{
						new File("dumps").mkdir();
						File f = new File("dumps/spawndump.txt");
						if (!f.exists())
						{
							f.createNewFile();
						}
						FileWriter writer = new FileWriter(f, true);
						writer.write("<spawn count=\"1\" respawn=\"60\" respawn_random=\"0\" period_of_day=\"none\">\n\t" + "<point x=\"" + activeChar.getLoc().x + "\" y=\"" + activeChar.getLoc().y + "\" z=\"" + activeChar.getLoc().z + "\" h=\"" + activeChar.getLoc().h + "\" />\n\t" + "<npc id=\"" + Integer.parseInt(id3) + "\" /><!--" + NpcHolder.getInstance().getTemplate(Integer.parseInt(id3)).getName() + "-->\n" + "</spawn>\n");
						writer.close();
					}
					catch (Exception e)
					{
					}
				}
				catch (Exception e)
				{
				}
				break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[]
	 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
	
	/**
	 * Method spawnMonster.
	 * @param activeChar Player
	 * @param monsterId String
	 * @param respawnTime int
	 * @param mobCount int
	 */
	private void spawnMonster(Player activeChar, String monsterId, int respawnTime, int mobCount)
	{
		GameObject target = activeChar.getTarget();
		if (target == null)
		{
			target = activeChar;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher regexp = pattern.matcher(monsterId);
		NpcTemplate template;
		if (regexp.matches())
		{
			int monsterTemplate = Integer.parseInt(monsterId);
			template = NpcHolder.getInstance().getTemplate(monsterTemplate);
		}
		else
		{
			monsterId = monsterId.replace('_', ' ');
			template = NpcHolder.getInstance().getTemplateByName(monsterId);
		}
		if (template == null)
		{
			activeChar.sendMessage("Incorrect monster template.");
			return;
		}
		try
		{
			SimpleSpawner spawn = new SimpleSpawner(template);
			spawn.setLoc(target.getLoc());
			spawn.setAmount(mobCount);
			spawn.setHeading(activeChar.getHeading());
			spawn.setRespawnDelay(respawnTime);
			spawn.setReflection(activeChar.getReflection());
			if (RaidBossSpawnManager.getInstance().isDefined(template.getNpcId()))
			{
				activeChar.sendMessage("Raid Boss " + template.name + " already spawned.");
			}
			else
			{
				if (Config.SAVE_GM_SPAWN_CUSTOM)
				{
					CustomSpawnTable.getInstance().addNewSpawn(spawn);
				}
				spawn.init();
				if (respawnTime == 0)
				{
					spawn.stopRespawn();
				}
				activeChar.sendMessage("Created " + template.name + " on " + target.getObjectId() + ".");
			}
		}
		catch (Exception e)
		{
			activeChar.sendMessage("Target is not ingame.");
		}
	}
}
