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

import java.util.Map;

import lineage2.commons.text.PrintfFormat;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminQuests implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_quests.
		 */
		admin_quests,
		/**
		 * Field admin_quest.
		 */
		admin_quest
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
		if (!activeChar.getPlayerAccess().CanEditCharAll)
		{
			return false;
		}
		switch (command)
		{
			case admin_quests:
				return ShowQuestList(getTargetChar(wordList, 1, activeChar), activeChar);
			case admin_quest:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //quest id|name [SHOW|STATE|VAR|CLEAR] ...");
					return true;
				}
				Quest _quest = QuestManager.getQuest2(wordList[1]);
				if (_quest == null)
				{
					activeChar.sendMessage("Quest " + wordList[1] + " undefined");
					return true;
				}
				if ((wordList.length < 3) || wordList[2].equalsIgnoreCase("SHOW"))
				{
					return cmd_Show(_quest, wordList, activeChar);
				}
				if (wordList[2].equalsIgnoreCase("STATE"))
				{
					return cmd_State(_quest, wordList, activeChar);
				}
				if (wordList[2].equalsIgnoreCase("VAR"))
				{
					return cmd_Var(_quest, wordList, activeChar);
				}
				if (wordList[2].equalsIgnoreCase("CLEAR"))
				{
					return cmd_Clear(_quest, wordList, activeChar);
				}
				return cmd_Show(_quest, wordList, activeChar);
		}
		return true;
	}
	
	/**
	 * Method cmd_Clear.
	 * @param _quest Quest
	 * @param wordList String[]
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean cmd_Clear(Quest _quest, String[] wordList, Player activeChar)
	{
		Player targetChar = getTargetChar(wordList, 3, activeChar);
		QuestState qs = targetChar.getQuestState(_quest.getName());
		if (qs == null)
		{
			activeChar.sendMessage("Player " + targetChar.getName() + " havn't Quest [" + _quest.getName() + "]");
			return false;
		}
		qs.exitCurrentQuest(true);
		return ShowQuestList(targetChar, activeChar);
	}
	
	/**
	 * Method cmd_Show.
	 * @param _quest Quest
	 * @param wordList String[]
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean cmd_Show(Quest _quest, String[] wordList, Player activeChar)
	{
		Player targetChar = getTargetChar(wordList, 3, activeChar);
		QuestState qs = targetChar.getQuestState(_quest.getName());
		if (qs == null)
		{
			activeChar.sendMessage("Player " + targetChar.getName() + " havn't Quest [" + _quest.getName() + "]");
			return false;
		}
		return ShowQuestState(qs, activeChar);
	}
	
	/**
	 * Field fmtHEAD.
	 */
	private static final PrintfFormat fmtHEAD = new PrintfFormat("<center><font color=\"LEVEL\">%s [id=%d]</font><br><edit var=\"new_val\" width=100 height=12></center><br>");
	/**
	 * Field fmtRow.
	 */
	private static final PrintfFormat fmtRow = new PrintfFormat("<tr><td>%s</td><td>%s</td><td width=30>%s</td></tr>");
	/**
	 * Field fmtSetButton.
	 */
	private static final PrintfFormat fmtSetButton = new PrintfFormat("<button value=\"Set\" action=\"bypass -h admin_quest %d %s %s %s %s\" width=30 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">");
	/**
	 * Field fmtFOOT.
	 */
	private static final PrintfFormat fmtFOOT = new PrintfFormat("<br><br><br><center><button value=\"Clear Quest\" action=\"bypass -h admin_quest %d CLEAR %s\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"> <button value=\"Quests List\" action=\"bypass -h admin_quests %s\" width=100 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></center>");
	
	/**
	 * Method ShowQuestState.
	 * @param qs QuestState
	 * @param activeChar Player
	 * @return boolean
	 */
	private static boolean ShowQuestState(QuestState qs, Player activeChar)
	{
		Map<String, String> vars = qs.getVars();
		int id = qs.getQuest().getQuestIntId();
		String char_name = qs.getPlayer().getName();
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append(fmtHEAD.sprintf(new Object[]
		{
			qs.getQuest().getClass().getSimpleName(),
			id
		}));
		replyMSG.append("<table width=260>");
		replyMSG.append(fmtRow.sprintf(new Object[]
		{
			"PLAYER: ",
			char_name,
			""
		}));
		replyMSG.append(fmtRow.sprintf(new Object[]
		{
			"STATE: ",
			qs.getStateName(),
			fmtSetButton.sprintf(new Object[]
			{
				id,
				"STATE",
				"$new_val",
				char_name,
				""
			})
		}));
		for (String key : vars.keySet())
		{
			if (!key.equalsIgnoreCase("<state>"))
			{
				replyMSG.append(fmtRow.sprintf(new Object[]
				{
					key + ": ",
					vars.get(key),
					fmtSetButton.sprintf(new Object[]
					{
						id,
						"VAR",
						key,
						"$new_val",
						char_name
					})
				}));
			}
		}
		replyMSG.append(fmtRow.sprintf(new Object[]
		{
			"<edit var=\"new_name\" width=50 height=12>",
			"~new var~",
			fmtSetButton.sprintf(new Object[]
			{
				id,
				"VAR",
				"$new_name",
				"$new_val",
				char_name
			})
		}));
		replyMSG.append("</table>");
		replyMSG.append(fmtFOOT.sprintf(new Object[]
		{
			id,
			char_name,
			char_name
		}));
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
		vars.clear();
		return true;
	}
	
	/**
	 * Field fmtListRow.
	 */
	private static final PrintfFormat fmtListRow = new PrintfFormat("<tr><td><a action=\"bypass -h admin_quest %d %s\">%s</a></td><td>%s</td></tr>");
	/**
	 * Field fmtListNew.
	 */
	private static final PrintfFormat fmtListNew = new PrintfFormat("<tr><td><edit var=\"new_quest\" width=100 height=12></td><td><button value=\"Add\" action=\"bypass -h admin_quest $new_quest STATE 2 %s\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></td></tr>");
	
	/**
	 * Method ShowQuestList.
	 * @param targetChar Player
	 * @param activeChar Player
	 * @return boolean
	 */
	private static boolean ShowQuestList(Player targetChar, Player activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body><table width=260>");
		for (QuestState qs : targetChar.getAllQuestsStates())
		{
			if ((qs != null) && (qs.getQuest().getQuestIntId() != 255))
			{
				replyMSG.append(fmtListRow.sprintf(new Object[]
				{
					qs.getQuest().getQuestIntId(),
					targetChar.getName(),
					qs.getQuest().getName(),
					qs.getStateName()
				}));
			}
		}
		replyMSG.append(fmtListNew.sprintf(new Object[]
		{
			targetChar.getName()
		}));
		replyMSG.append("</table></body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
		return true;
	}
	
	/**
	 * Method cmd_Var.
	 * @param _quest Quest
	 * @param wordList String[]
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean cmd_Var(Quest _quest, String[] wordList, Player activeChar)
	{
		if (wordList.length < 5)
		{
			activeChar.sendMessage("USAGE: //quest id|name VAR varname newvalue [target]");
			return false;
		}
		Player targetChar = getTargetChar(wordList, 5, activeChar);
		QuestState qs = targetChar.getQuestState(_quest.getName());
		if (qs == null)
		{
			activeChar.sendMessage("Player " + targetChar.getName() + " havn't Quest [" + _quest.getName() + "], init quest by command:");
			activeChar.sendMessage("//quest id|name STATE 1|2|3 [target]");
			return false;
		}
		if (wordList[4].equalsIgnoreCase("~") || wordList[4].equalsIgnoreCase("#"))
		{
			qs.unset(wordList[3]);
		}
		else
		{
			qs.set(wordList[3], wordList[4]);
		}
		return ShowQuestState(qs, activeChar);
	}
	
	/**
	 * Method cmd_State.
	 * @param _quest Quest
	 * @param wordList String[]
	 * @param activeChar Player
	 * @return boolean
	 */
	private boolean cmd_State(Quest _quest, String[] wordList, Player activeChar)
	{
		if (wordList.length < 4)
		{
			activeChar.sendMessage("USAGE: //quest id|name STATE 1|2|3 [target]");
			return false;
		}
		int state = 0;
		try
		{
			state = Integer.parseInt(wordList[3]);
		}
		catch (Exception e)
		{
			activeChar.sendMessage("Wrong State ID: " + wordList[3]);
			return false;
		}
		Player targetChar = getTargetChar(wordList, 4, activeChar);
		QuestState qs = targetChar.getQuestState(_quest.getName());
		if (qs == null)
		{
			activeChar.sendMessage("Init Quest [" + _quest.getName() + "] for " + targetChar.getName());
			qs = _quest.newQuestState(targetChar, state);
			qs.set("cond", "1");
		}
		else
		{
			qs.setState(state);
		}
		return ShowQuestState(qs, activeChar);
	}
	
	/**
	 * Method getTargetChar.
	 * @param wordList String[]
	 * @param wordListIndex int
	 * @param activeChar Player
	 * @return Player
	 */
	private Player getTargetChar(String[] wordList, int wordListIndex, Player activeChar)
	{
		if ((wordListIndex >= 0) && (wordList.length > wordListIndex))
		{
			Player player = World.getPlayer(wordList[wordListIndex]);
			if (player == null)
			{
				activeChar.sendMessage("Can't find player: " + wordList[wordListIndex]);
			}
			return player;
		}
		GameObject my_target = activeChar.getTarget();
		if ((my_target != null) && my_target.isPlayer())
		{
			return (Player) my_target;
		}
		return activeChar;
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
}
