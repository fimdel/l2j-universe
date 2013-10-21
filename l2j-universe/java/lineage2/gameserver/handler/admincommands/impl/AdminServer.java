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

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.WorldRegion;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.RaidBossInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminServer implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_server.
		 */
		admin_server,
		/**
		 * Field admin_check_actor.
		 */
		admin_check_actor,
		/**
		 * Field admin_setvar.
		 */
		admin_setvar,
		/**
		 * Field admin_set_ai_interval.
		 */
		admin_set_ai_interval,
		/**
		 * Field admin_spawn2.
		 */
		admin_spawn2
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().Menu)
		{
			return false;
		}
		switch (command)
		{
			case admin_server:
				try
				{
					String val = fullString.substring(13);
					showHelpPage(activeChar, val);
				}
				catch (StringIndexOutOfBoundsException e)
				{
				}
				break;
			case admin_check_actor:
				GameObject obj = activeChar.getTarget();
				if (obj == null)
				{
					activeChar.sendMessage("target == null");
					return false;
				}
				if (!obj.isCreature())
				{
					activeChar.sendMessage("target is not a character");
					return false;
				}
				Creature target = (Creature) obj;
				CharacterAI ai = target.getAI();
				if (ai == null)
				{
					activeChar.sendMessage("ai == null");
					return false;
				}
				Creature actor = ai.getActor();
				if (actor == null)
				{
					activeChar.sendMessage("actor == null");
					return false;
				}
				activeChar.sendMessage("actor: " + actor);
				break;
			case admin_setvar:
				if (wordList.length != 3)
				{
					activeChar.sendMessage("Incorrect argument count!!!");
					return false;
				}
				ServerVariables.set(wordList[1], wordList[2]);
				activeChar.sendMessage("Value changed.");
				break;
			case admin_set_ai_interval:
				if (wordList.length != 2)
				{
					activeChar.sendMessage("Incorrect argument count!!!");
					return false;
				}
				int interval = Integer.parseInt(wordList[1]);
				int count = 0;
				int count2 = 0;
				for (final NpcInstance npc : GameObjectsStorage.getAllNpcsForIterate())
				{
					if ((npc == null) || (npc instanceof RaidBossInstance))
					{
						continue;
					}
					final CharacterAI char_ai = npc.getAI();
					if (char_ai instanceof DefaultAI)
					{
						try
						{
							final java.lang.reflect.Field field = lineage2.gameserver.ai.DefaultAI.class.getDeclaredField("AI_TASK_DELAY");
							field.setAccessible(true);
							field.set(char_ai, interval);
							if (char_ai.isActive())
							{
								char_ai.stopAITask();
								count++;
								WorldRegion region = npc.getCurrentRegion();
								if ((region != null) && region.isActive())
								{
									char_ai.startAITask();
									count2++;
								}
							}
						}
						catch (Exception e)
						{
						}
					}
				}
				activeChar.sendMessage(count + " AI stopped, " + count2 + " AI started");
				break;
			case admin_spawn2:
				StringTokenizer st = new StringTokenizer(fullString, " ");
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
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
	
	/**
	 * Method showHelpPage.
	 * @param targetChar Player
	 * @param filename String
	 */
	public static void showHelpPage(Player targetChar, String filename)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setFile("admin/" + filename);
		targetChar.sendPacket(adminReply);
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
			spawn.init();
			if (respawnTime == 0)
			{
				spawn.stopRespawn();
			}
			activeChar.sendMessage("Created " + template.name + " on " + target.getObjectId() + ".");
		}
		catch (Exception e)
		{
			activeChar.sendMessage("Target is not ingame.");
		}
	}
}
