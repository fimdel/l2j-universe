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
package services.community;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.SubClassType;
import lineage2.gameserver.network.serverpackets.ExSubjobInfo;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.BbsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @Translated by KadeL
 * @version $Revision: 1.0 $
 */
public class ManageProf implements ScriptFile, ICommunityBoardHandler
{
	/**
	 * Field _commands.
	 */
	private static final String[] _commands =
	{
		"_bbscareer;",
		"_bbsclass_change",
		"_bbsclass_upgrade"
	};
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ManageProf.class);
	/**
	 * Field jobLevel.
	 */
	int jobLevel = 0;
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: Manage Career service loaded.");
			CommunityBoardManager.getInstance().registerHandler(this);
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			CommunityBoardManager.getInstance().removeHandler(this);
		}
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * Method getBypassCommands.
	 * @return String[]
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#getBypassCommands()
	 */
	@Override
	public String[] getBypassCommands()
	{
		return _commands;
	}
	
	/**
	 * Method onBypassCommand.
	 * @param player Player
	 * @param bypass String
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#onBypassCommand(Player, String)
	 */
	@Override
	public void onBypassCommand(Player player, String bypass)
	{
		if (!player.checkAllowAction())
		{
			return;
		}
		if (bypass.equals(_commands[0]))
		{
			String html = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/career.htm", player);
			html = html.replace("%career%", String.valueOf(makeMessage(player)));
			ShowBoard.separateAndSend(BbsUtil.htmlAll(html, player), player);
		}
		else if (bypass.startsWith(_commands[1]))
		{
			String[] args = bypass.split(" ", -1);
			int val = Integer.parseInt(args[1]);
			long price = Long.parseLong(args[2]);
			if (player.getInventory().destroyItemByItemId(Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel], price))
			{
				changeClass(player, val);
				onBypassCommand(player, "_bbscareer;");
			}
			else if (Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel] == 57)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
		}
		else if (bypass.startsWith(_commands[2]))
		{
			if (player.getLevel() < 80)
			{
				player.sendMessage("You are too weak! Come get a Level 80!");
				return;
			}
			if (player.getActiveSubClass().isBase())
			{
				player.sendMessage("You must be on a Sub-Class!");
				return;
			}
			for (SubClass s : player.getSubClassList().values())
			{
				if (s.isDouble())
				{
					player.sendMessage("You already have a Dual-Class!");
					return;
				}
			}
			player.getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			AwakingManager.getInstance().onPlayerEnter(player);
			player.sendMessage("Congratulations! You have Dual-Classl.");
			player.sendPacket(new ExSubjobInfo(player, true));
		}
	}
	
	/**
	 * Method onWriteCommand.
	 * @param player Player
	 * @param bypass String
	 * @param arg1 String
	 * @param arg2 String
	 * @param arg3 String
	 * @param arg4 String
	 * @param arg5 String
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#onWriteCommand(Player, String, String, String, String, String, String)
	 */
	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}
	
	/**
	 * Method makeMessage.
	 * @param player Player
	 * @return String
	 */
	private String makeMessage(Player player)
	{
		ClassId classId = player.getClassId();
		jobLevel = player.getClassLevel();
		int level = player.getLevel();
		StringBuilder html = new StringBuilder();
		html.append("<br>");
		html.append("<table width=600>");
		html.append("<tr><td>");
		if (Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			jobLevel = 5;
		}
		if (((level >= 20) && (jobLevel == 1)) || ((level >= 40) && (jobLevel == 2)) || ((level >= 76) && (jobLevel == 3)) || ((level >= 85) && (jobLevel == 4) && Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel)))
		{
			ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel]);
			html.append("You have to pay: <font color=\"LEVEL\">");
			html.append(Config.CLASS_MASTERS_PRICE_LIST[jobLevel] + "</font> <font color=\"LEVEL\">" + item.getName() + "</font> for a Class Change.<br>");
			html.append("<center><table width=600><tr>");
			for (ClassId cid : ClassId.values())
			{
				if (cid == ClassId.INSPECTOR)
				{
					continue;
				}
				if (cid.childOf(classId) && (cid.getClassLevel().ordinal() == (classId.getClassLevel().ordinal() + 1)))
				{
					html.append("<td><center><button value=\"" + cid.name() + "\" action=\"bypass _bbsclass_change " + cid.getId() + " " + Config.CLASS_MASTERS_PRICE_LIST[jobLevel] + "\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td>");
				}
			}
			html.append("</tr>");
			html.append("<tr><td><center><button value=\"Dual-Class\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td></tr>");
			html.append("</table></center>");
			html.append("</td>");
			html.append("</tr>");
			html.append("</table>");
		}
		else
		{
			switch (jobLevel)
			{
				case 1:
					html.append("<center><button value=\"Dual-Class\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Welcome, <font color=F2C202>" + player.getName() + "</font>. Your current Class is <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("To change your Class, you have to reach <font color=F2C202>Level 20.</font><br>");
					html.append("To activate the Sub-Class you must reach <font color=F2C202>Level 76.</font><br>");
					html.append("In order to become a Nobless, you have to pump the <font color=F2C202>Sub-Class Level 76.</font><br>");
					break;
				case 2:
					html.append("<center><button value=\"Dual-Class\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Welcome, <font color=F2C202>" + player.getName() + "</font>. Your current Class is <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("To change your Class, you have to reach <font color=F2C202>Level 40.</font><br>");
					html.append("To activate the Sub-Class you must reach <font color=F2C202>Level 76.</font><br>");
					html.append("In order to become a Nobless, you have to pump the <font color=F2C202>Sub-Class Level 76.</font><br>");
					break;
				case 3:
					html.append("<center><button value=\"Dual-Class\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Welcome, <font color=F2C202>" + player.getName() + "</font>. Your current Class is <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("To change your Class, you have to reach <font color=F2C202>Level 76.</font><br>");
					html.append("To activate the Sub-Class you must reach <font color=F2C202>Level 76.</font><br>");
					html.append("In order to become a Nobless, you have to pump the <font color=F2C202>Sub-Class Level 76.</font><br>");
					break;
				case 4:
					html.append("<center><button value=\"Dual-Class\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Welcome, <font color=F2C202>" + player.getName() + "</font>. Your current Class is <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("To change your Class, you have to reach <font color=F2C202>Level 85.</font><br>");
					if (level < 76)
					{
						break;
					}
					html.append("You have reached <font color=F2C202>Level 76</font>, the activation of a Sub-Class is now available.<br>");
					if (!player.isNoble())
					{
						html.append("You can get a Noblesse. Visit the 'Shop'.<br>");
					}
					else
					{
						html.append("You already Nobless. Getting the nobless no longer available.<br>");
					}
					break;
				case 5:
					html.append("<center><button value=\"Dual-Class\" action=\"bypass _bbsclass_upgrade\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center>");
					html.append("Welcome, <font color=F2C202>" + player.getName() + "</font>. Your current Class is <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("For you there are no more jobs available, or Class-Master is not currently available.<br>");
					if (level < 76)
					{
						break;
					}
					html.append("You have reached <font color=F2C202>Level 76</font>, the activation of a Sub-Class is now available.<br>");
					if (!player.isNoble())
					{
						html.append("You can get a Noblesse. Visit the 'Shop'.<br>");
					}
					else
					{
						html.append("You already Nobless. Getting the nobless no longer available.<br>");
					}
					break;
			}
		}
		return html.toString();
	}
	
	/**
	 * Method changeClass.
	 * @param player Player
	 * @param val int
	 */
	private void changeClass(Player player, int val)
	{
		if (player.getClassId().isOfLevel(ClassLevel.Third))
		{
			player.sendPacket(Msg.YOU_HAVE_COMPLETED_THE_QUEST_FOR_3RD_OCCUPATION_CHANGE_AND_MOVED_TO_ANOTHER_CLASS_CONGRATULATIONS);
		}
		else
		{
			player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);
		}
		player.setClassId(val, false, false);
		//AwakingManager.getInstance().getRaceSkill(player);
		player.broadcastCharInfo();
	}
}
