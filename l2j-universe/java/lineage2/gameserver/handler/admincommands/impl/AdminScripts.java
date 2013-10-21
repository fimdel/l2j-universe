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

import lineage2.gameserver.data.xml.parser.ArmorItemParser;
import lineage2.gameserver.data.xml.parser.EtcItemParser;
import lineage2.gameserver.data.xml.parser.WeaponItemParser;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Scripts;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminScripts implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_scripts_reload.
		 */
		admin_scripts_reload,
		/**
		 * Field admin_sreload.
		 */
		admin_sreload,
		/**
		 * Field admin_sqreload.
		 */
		admin_sqreload
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
		if (!activeChar.isGM())
		{
			return false;
		}
		switch (command)
		{
			case admin_scripts_reload:
			case admin_sreload:
				if (wordList.length < 2)
				{
					return false;
				}
				String param = wordList[1];
				if (param.equalsIgnoreCase("all"))
				{
					activeChar.sendMessage("Scripts reload starting...");
					if (!Scripts.getInstance().reload())
					{
						activeChar.sendMessage("Scripts reloaded with errors. Loaded " + Scripts.getInstance().getClasses().size() + " classes.");
					}
					else
					{
						activeChar.sendMessage("Scripts successfully reloaded. Loaded " + Scripts.getInstance().getClasses().size() + " classes.");
					}
				}
				else if (param.equalsIgnoreCase("items"))
				{
					activeChar.sendMessage("EtcItem reload starting...");
					try
					{
						EtcItemParser.getInstance().reload();
					}
					catch (Exception e)
					{
						activeChar.sendMessage("EtcItem reloaded with errors.");
						return false;
					}
					activeChar.sendMessage("WeaponItem reload starting...");
					try
					{
						WeaponItemParser.getInstance().reload();
					}
					catch (Exception e)
					{
						activeChar.sendMessage("WeaponItem reloaded with errors.");
						return false;
					}
					activeChar.sendMessage("ArmorItem reload starting...");
					try
					{
						ArmorItemParser.getInstance().reload();
					}
					catch (Exception e)
					{
						activeChar.sendMessage("ArmorItem reloaded with errors.");
						return false;
					}
					activeChar.sendMessage("Items successfully reloaded.");
					break;
				}
				else if (!Scripts.getInstance().reload(param))
				{
					activeChar.sendMessage("Script(s) reloaded with errors.");
				}
				else
				{
					activeChar.sendMessage("Script(s) successfully reloaded.");
				}
				break;
			case admin_sqreload:
				if (wordList.length < 2)
				{
					return false;
				}
				String quest = wordList[1];
				if (!Scripts.getInstance().reload("quests/" + quest))
				{
					activeChar.sendMessage("Quest \"" + quest + "\" reloaded with errors.");
				}
				else
				{
					activeChar.sendMessage("Quest \"" + quest + "\" successfully reloaded.");
				}
				reloadQuestStates(activeChar);
				break;
		}
		return true;
	}
	
	/**
	 * Method reloadQuestStates.
	 * @param p Player
	 */
	private void reloadQuestStates(Player p)
	{
		for (QuestState qs : p.getAllQuestsStates())
		{
			p.removeQuestState(qs.getQuest().getName());
		}
		Quest.restoreQuestStates(p);
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
