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

import java.util.Calendar;
import java.util.List;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.olympiad.CompType;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.olympiad.OlympiadDatabase;
import lineage2.gameserver.network.serverpackets.ExHeroList;
import lineage2.gameserver.network.serverpackets.ExReceiveOlympiad;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(OlympiadManagerInstance.class);
	
	/**
	 * Constructor for OlympiadManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public OlympiadManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		if (Config.ENABLE_OLYMPIAD && (template.npcId == 31688))
		{
			Olympiad.addOlympiadNpc(this);
		}
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
		if (!Config.ENABLE_OLYMPIAD)
		{
			return;
		}
		if (command.startsWith("OlympiadNoble"))
		{
			if (!Config.ENABLE_OLYMPIAD)
			{
				return;
			}
			int val = Integer.parseInt(command.substring(14));
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			switch (val)
			{
				case 1:
					Olympiad.unRegisterNoble(player);
					showChatWindow(player, 0);
					break;
				case 2:
					if (Olympiad.isRegistered(player))
					{
						player.sendPacket(html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "manager_noregister.htm"));
					}
					else
					{
						int nowWeek = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH);
						if (nowWeek < 4)
						{
							html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "manager_register_noclass.htm");
						}
						else
						{
							html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "manager_register_class.htm");
						}
						player.sendPacket(html);
					}
					break;
				case 4:
					Olympiad.registerNoble(player, CompType.NON_CLASSED);
					break;
				case 5:
					Olympiad.registerNoble(player, CompType.CLASSED);
					break;
				case 6:
					int passes = Olympiad.getNoblessePasses(player);
					if (passes > 0)
					{
						player.getInventory().addItem(Config.ALT_OLY_COMP_RITEM, passes);
						player.sendPacket(SystemMessage2.obtainItems(Config.ALT_OLY_COMP_RITEM, passes, 0));
					}
					else
					{
						player.sendPacket(html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "manager_nopoints.htm"));
					}
					break;
				case 7:
					MultiSellHolder.getInstance().SeparateAndSend(102, player, 0);
					break;
				case 9:
					MultiSellHolder.getInstance().SeparateAndSend(103, player, 0);
					break;
				default:
					_log.warn("Olympiad System: Couldnt send packet for request " + val);
					break;
			}
		}
		else if (command.startsWith("Olympiad"))
		{
			if (!Config.ENABLE_OLYMPIAD)
			{
				return;
			}
			int val = Integer.parseInt(command.substring(9, 10));
			NpcHtmlMessage reply = new NpcHtmlMessage(player, this);
			switch (val)
			{
				case 1:
					if (!Olympiad.inCompPeriod() || Olympiad.isOlympiadEnd())
					{
						player.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
						return;
					}
					player.sendPacket(new ExReceiveOlympiad.MatchList());
					break;
				case 2:
					int classId = Integer.parseInt(command.substring(11));
					if (classId >= 139)
					{
						reply.setFile(Olympiad.OLYMPIAD_HTML_PATH + "manager_ranking.htm");
						List<String> names = OlympiadDatabase.getClassLeaderBoard(classId);
						int index = 1;
						for (String name : names)
						{
							reply.replace("%place" + index + "%", String.valueOf(index));
							reply.replace("%rank" + index + "%", name);
							index++;
							if (index > 10)
							{
								break;
							}
						}
						for (; index <= 10; index++)
						{
							reply.replace("%place" + index + "%", "");
							reply.replace("%rank" + index + "%", "");
						}
						player.sendPacket(reply);
					}
					break;
				case 3:
					if (!Config.ENABLE_OLYMPIAD_SPECTATING)
					{
						break;
					}
					Olympiad.addSpectator(Integer.parseInt(command.substring(11)), player);
					break;
				case 4:
					player.sendPacket(new ExHeroList());
					break;
				case 5:
					if (Hero.getInstance().isInactiveHero(player.getObjectId()))
					{
						Hero.getInstance().activateHero(player);
						reply.setFile(Olympiad.OLYMPIAD_HTML_PATH + "monument_give_hero.htm");
					}
					else
					{
						reply.setFile(Olympiad.OLYMPIAD_HTML_PATH + "monument_dont_hero.htm");
					}
					player.sendPacket(reply);
					break;
				default:
					_log.warn("Olympiad System: Couldnt send packet for request " + val);
					break;
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		String fileName = Olympiad.OLYMPIAD_HTML_PATH;
		int npcId = getNpcId();
		switch (npcId)
		{
			case 31688:
				fileName += "manager";
				break;
			default:
				fileName += "monument";
				break;
		}
		if (player.isNoble())
		{
			fileName += "_n";
		}
		if (val > 0)
		{
			fileName += "-" + val;
		}
		fileName += ".htm";
		player.sendPacket(new NpcHtmlMessage(player, this, fileName, val));
	}
}
