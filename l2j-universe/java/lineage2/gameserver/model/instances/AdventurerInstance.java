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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.instancemanager.RaidBossSpawnManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.network.serverpackets.ExShowQuestInfo;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdventurerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AdventurerInstance.class);
	
	/**
	 * Constructor for AdventurerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public AdventurerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.startsWith("npcfind_byid"))
		{
			try
			{
				int bossId = Integer.parseInt(command.substring(12).trim());
				switch (RaidBossSpawnManager.getInstance().getRaidBossStatusId(bossId))
				{
					case ALIVE:
					case DEAD:
						Spawner spawn = RaidBossSpawnManager.getInstance().getSpawnTable().get(bossId);
						Location loc = spawn.getCurrentSpawnRange().getRandomLoc(spawn.getReflection().getGeoIndex());
						player.sendPacket(new RadarControl(2, 2, loc), new RadarControl(0, 1, loc));
						break;
					case UNDEFINED:
						player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2AdventurerInstance.BossNotInGame", player).addNumber(bossId));
						break;
				}
			}
			catch (NumberFormatException e)
			{
				_log.warn("AdventurerInstance: Invalid Bypass to Server command parameter.");
			}
		}
		else if (command.startsWith("raidInfo"))
		{
			int bossLevel = Integer.parseInt(command.substring(9).trim());
			String filename = "adventurer_guildsman/raid_info/info.htm";
			if (bossLevel != 0)
			{
				filename = "adventurer_guildsman/raid_info/level" + bossLevel + ".htm";
			}
			showChatWindow(player, filename);
		}
		else if (command.equalsIgnoreCase("questlist"))
		{
			player.sendPacket(ExShowQuestInfo.STATIC);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String pom;
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		return "adventurer_guildsman/" + pom + ".htm";
	}
}
