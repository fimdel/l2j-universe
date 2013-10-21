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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.SubClassInfo;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExPCCafePointInfo;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.Mentoring;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.Util;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class AdminEditChar implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_edit_character.
		 */
		admin_edit_character,
		/**
		 * Field admin_character_actions.
		 */
		admin_character_actions,
		/**
		 * Field admin_current_player.
		 */
		admin_current_player,
		/**
		 * Field admin_nokarma.
		 */
		admin_nokarma,
		/**
		 * Field admin_setkarma.
		 */
		admin_setkarma,
		/**
		 * Field admin_character_list.
		 */
		admin_character_list,
		/**
		 * Field admin_show_characters.
		 */
		admin_show_characters,
		/**
		 * Field admin_find_character.
		 */
		admin_find_character,
		/**
		 * Field admin_save_modifications.
		 */
		admin_save_modifications,
		/**
		 * Field admin_rec.
		 */
		admin_rec,
		/**
		 * Field admin_settitle.
		 */
		admin_settitle,
		/**
		 * Field admin_setclass.
		 */
		admin_setclass,
		/**
		 * Field admin_setname.
		 */
		admin_setname,
		/**
		 * Field admin_setsex.
		 */
		admin_setsex,
		/**
		 * Field admin_setcolor.
		 */
		admin_setcolor,
		/**
		 * Field admin_add_exp_sp_to_character.
		 */
		admin_add_exp_sp_to_character,
		/**
		 * Field admin_add_exp_sp.
		 */
		admin_add_exp_sp,
		/**
		 * Field admin_sethero.
		 */
		admin_sethero,
		/**
		 * Field admin_setnoble.
		 */
		admin_setnoble,
		/**
		 * Field admin_trans.
		 */
		admin_trans,
		/**
		 * Field admin_setsubclass.
		 */
		admin_setsubclass,
		/**
		 * Field admin_setfame.
		 */
		admin_setfame,
		/**
		 * Field admin_setbday.
		 */
		admin_setbday,
		/**
		 * Field admin_give_item.
		 */
		admin_give_item,
		/**
		 * Field admin_add_bang.
		 */
		admin_add_bang,
		/**
		 * Field admin_set_bang.
		 */
		admin_set_bang,
		admin_reset_mentor_penalty
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
		if (activeChar.getPlayerAccess().CanRename)
		{
			if (fullString.startsWith("admin_settitle"))
			{
				try
				{
					String val = fullString.substring(15);
					GameObject target = activeChar.getTarget();
					Player player = null;
					if (target == null)
					{
						return false;
					}
					if (target.isPlayer())
					{
						player = (Player) target;
						player.setTitle(val);
						player.sendMessage("Your title has been changed by a GM");
						player.sendChanges();
					}
					else if (target.isNpc())
					{
						((NpcInstance) target).setTitle(val);
						target.decayMe();
						target.spawnMe();
					}
					return true;
				}
				catch (StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("You need to specify the new title.");
					return false;
				}
			}
			else if (fullString.startsWith("admin_setclass"))
			{
				try
				{
					String val = fullString.substring(15);
					int id = Integer.parseInt(val.trim());
					GameObject target = activeChar.getTarget();
					if ((target == null) || !target.isPlayer())
					{
						target = activeChar;
					}
					if (id > (ClassId.VALUES.length - 1))
					{
						activeChar.sendMessage("There are no classes over " + String.valueOf(ClassId.VALUES.length - 1) + "  id.");
						return false;
					}
					Player player = target.getPlayer();
					player.setClassId(id, true, false);
					player.sendMessage("Your class has been changed by a GM");
					player.broadcastCharInfo();
					return true;
				}
				catch (StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("You need to specify the new class id.");
					return false;
				}
			}
			else if (fullString.startsWith("admin_setname"))
			{
				try
				{
					String val = fullString.substring(14);
					GameObject target = activeChar.getTarget();
					Player player;
					if ((target != null) && target.isPlayer())
					{
						player = (Player) target;
					}
					else
					{
						return false;
					}
					if (mysql.simple_get_int("count(*)", "characters", "`char_name` like '" + val + "'") > 0)
					{
						activeChar.sendMessage("Name already exist.");
						return false;
					}
					Log.add("Character " + player.getName() + " renamed to " + val + " by GM " + activeChar.getName(), "renames");
					player.reName(val);
					player.sendMessage("Your name has been changed by a GM");
					return true;
				}
				catch (StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("You need to specify the new name.");
					return false;
				}
			}
		}
		if (!activeChar.getPlayerAccess().CanEditChar && !activeChar.getPlayerAccess().CanViewChar)
		{
			return false;
		}
		if (fullString.equals("admin_current_player"))
		{
			showCharacterList(activeChar, null);
		}
		else if (fullString.startsWith("admin_character_list"))
		{
			try
			{
				String val = fullString.substring(21);
				Player target = GameObjectsStorage.getPlayer(val);
				showCharacterList(activeChar, target);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
		else if (fullString.startsWith("admin_show_characters"))
		{
			try
			{
				String val = fullString.substring(22);
				int page = Integer.parseInt(val);
				listCharacters(activeChar, page);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
		else if (fullString.startsWith("admin_find_character"))
		{
			try
			{
				String val = fullString.substring(21);
				findCharacter(activeChar, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("You didnt enter a character name to find.");
				listCharacters(activeChar, 0);
			}
		}
		else if (!activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}
		else if (fullString.equals("admin_edit_character"))
		{
			editCharacter(activeChar);
		}
		else if (fullString.equals("admin_character_actions"))
		{
			showCharacterActions(activeChar);
		}
		else if (fullString.equals("admin_nokarma"))
		{
			setTargetKarma(activeChar, 0);
		}
		else if (fullString.startsWith("admin_setkarma"))
		{
			try
			{
				String val = fullString.substring(15);
				int karma = Integer.parseInt(val);
				setTargetKarma(activeChar, karma);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Please specify new karma value.");
			}
		}
		else if (fullString.startsWith("admin_save_modifications"))
		{
			try
			{
				String val = fullString.substring(24);
				adminModifyCharacter(activeChar, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Error while modifying character.");
				listCharacters(activeChar, 0);
			}
		}
		else if (fullString.equals("admin_rec"))
		{
			GameObject target = activeChar.getTarget();
			Player player = null;
			if ((target != null) && target.isPlayer())
			{
				player = (Player) target;
			}
			else
			{
				return false;
			}
			player.setRecomHave(player.getRecomHave() + 1);
			player.sendMessage("You have been recommended by a GM");
			player.broadcastCharInfo();
		}
		else if (fullString.startsWith("admin_rec"))
		{
			try
			{
				String val = fullString.substring(10);
				int recVal = Integer.parseInt(val);
				GameObject target = activeChar.getTarget();
				Player player = null;
				if ((target != null) && target.isPlayer())
				{
					player = (Player) target;
				}
				else
				{
					return false;
				}
				player.setRecomHave(player.getRecomHave() + recVal);
				player.sendMessage("You have been recommended by a GM");
				player.broadcastCharInfo();
			}
			catch (NumberFormatException e)
			{
				activeChar.sendMessage("Command format is //rec <number>");
			}
		}
		else if (fullString.startsWith("admin_sethero"))
		{
			GameObject target = activeChar.getTarget();
			Player player;
			if ((wordList.length > 1) && (wordList[1] != null))
			{
				player = GameObjectsStorage.getPlayer(wordList[1]);
				if (player == null)
				{
					activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
					return false;
				}
			}
			else if ((target != null) && target.isPlayer())
			{
				player = (Player) target;
			}
			else
			{
				activeChar.sendMessage("You must specify the name or target character.");
				return false;
			}
			if (player.isHero())
			{
				player.setHero(false);
				player.updatePledgeClass();
				player.removeSkill(SkillTable.getInstance().getInfo(395, 1));
				player.removeSkill(SkillTable.getInstance().getInfo(396, 1));
				player.removeSkill(SkillTable.getInstance().getInfo(1374, 1));
				player.removeSkill(SkillTable.getInstance().getInfo(1375, 1));
				player.removeSkill(SkillTable.getInstance().getInfo(1376, 1));
			}
			else
			{
				player.setHero(true);
				player.updatePledgeClass();
				player.addSkill(SkillTable.getInstance().getInfo(395, 1));
				player.addSkill(SkillTable.getInstance().getInfo(396, 1));
				player.addSkill(SkillTable.getInstance().getInfo(1374, 1));
				player.addSkill(SkillTable.getInstance().getInfo(1375, 1));
				player.addSkill(SkillTable.getInstance().getInfo(1376, 1));
			}
			player.sendSkillList();
			player.sendMessage("Admin has changed your hero status.");
			player.broadcastUserInfo();
		}
		else if (fullString.startsWith("admin_setnoble"))
		{
			GameObject target = activeChar.getTarget();
			Player player;
			if ((wordList.length > 1) && (wordList[1] != null))
			{
				player = GameObjectsStorage.getPlayer(wordList[1]);
				if (player == null)
				{
					activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
					return false;
				}
			}
			else if ((target != null) && target.isPlayer())
			{
				player = (Player) target;
			}
			else
			{
				activeChar.sendMessage("You must specify the name or target character.");
				return false;
			}
			if (player.isNoble())
			{
				Olympiad.removeNoble(player);
				player.setNoble(false);
				player.sendMessage("Admin changed your noble status, now you are not nobless.");
			}
			else
			{
				Olympiad.addNoble(player);
				player.setNoble(true);
				player.sendMessage("Admin changed your noble status, now you are Nobless.");
			}
			player.updatePledgeClass();
			player.updateNobleSkills();
			player.sendSkillList();
			player.broadcastUserInfo();
		}
		else if (fullString.startsWith("admin_setsex"))
		{
			GameObject target = activeChar.getTarget();
			Player player = null;
			if ((target != null) && target.isPlayer())
			{
				player = (Player) target;
			}
			else
			{
				return false;
			}
			player.changeSex();
			player.sendMessage("Your gender has been changed by a GM");
			player.broadcastUserInfo();
		}
		else if (fullString.startsWith("admin_setcolor"))
		{
			try
			{
				String val = fullString.substring(15);
				GameObject target = activeChar.getTarget();
				Player player = null;
				if ((target != null) && target.isPlayer())
				{
					player = (Player) target;
				}
				else
				{
					return false;
				}
				player.setNameColor(Integer.decode("0x" + val));
				player.sendMessage("Your name color has been changed by a GM");
				player.broadcastUserInfo();
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("You need to specify the new color.");
			}
		}
		else if (fullString.startsWith("admin_add_exp_sp_to_character"))
		{
			addExpSp(activeChar);
		}
		else if (fullString.startsWith("admin_add_exp_sp"))
		{
			try
			{
				final String val = fullString.substring(16).trim();
				String[] vals = val.split(" ");
				long exp = NumberUtils.toLong(vals[0], 0L);
				int sp = vals.length > 1 ? NumberUtils.toInt(vals[1], 0) : 0;
				adminAddExpSp(activeChar, exp, sp);
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //add_exp_sp <exp> <sp>");
			}
		}
		else if (fullString.startsWith("admin_trans"))
		{
			StringTokenizer st = new StringTokenizer(fullString);
			if (st.countTokens() > 1)
			{
				st.nextToken();
				int transformId = 0;
				try
				{
					transformId = Integer.parseInt(st.nextToken());
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Specify a valid integer value.");
					return false;
				}
				if ((transformId != 0) && (activeChar.getTransformation() != 0))
				{
					activeChar.sendPacket(Msg.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN);
					return false;
				}
				activeChar.setTransformation(transformId);
				activeChar.sendMessage("Transforming...");
			}
			else
			{
				activeChar.sendMessage("Usage: //trans <ID>");
			}
		}
		else if (fullString.startsWith("admin_setsubclass"))
		{
			final GameObject target = activeChar.getTarget();
			if ((target == null) || !target.isPlayer())
			{
				activeChar.sendPacket(Msg.SELECT_TARGET);
				return false;
			}
			final Player player = (Player) target;
			StringTokenizer st = new StringTokenizer(fullString);
			if (st.countTokens() > 1)
			{
				st.nextToken();
				int classId = Short.parseShort(st.nextToken());
				if (!player.addSubClass(classId, true, 0, 0, false, 0))
				{
					activeChar.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.SubclassCouldNotBeAdded", activeChar));
					return false;
				}
				player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);
			}
			else
			{
				setSubclass(activeChar, player);
			}
		}
		else if (fullString.startsWith("admin_setfame"))
		{
			try
			{
				String val = fullString.substring(14);
				int fame = Integer.parseInt(val);
				setTargetFame(activeChar, fame);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Please specify new fame value.");
			}
		}
		else if (fullString.startsWith("admin_setbday"))
		{
			String msgUsage = "Usage: //setbday YYYY-MM-DD";
			String date = fullString.substring(14);
			if ((date.length() != 10) || !Util.isMatchingRegexp(date, "[0-9]{4}-[0-9]{2}-[0-9]{2}"))
			{
				activeChar.sendMessage(msgUsage);
				return false;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				dateFormat.parse(date);
			}
			catch (ParseException e)
			{
				activeChar.sendMessage(msgUsage);
			}
			if ((activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
			{
				activeChar.sendMessage("Please select a character.");
				return false;
			}
			if (!mysql.set("update characters set createtime = UNIX_TIMESTAMP('" + date + "') where obj_Id = " + activeChar.getTarget().getObjectId()))
			{
				activeChar.sendMessage(msgUsage);
				return false;
			}
			activeChar.sendMessage("New Birthday for " + activeChar.getTarget().getName() + ": " + date);
			activeChar.getTarget().getPlayer().sendMessage("Admin changed your birthday to: " + date);
		}
		else if (fullString.startsWith("admin_give_item"))
		{
			if (wordList.length < 3)
			{
				activeChar.sendMessage("Usage: //give_item id count <target>");
				return false;
			}
			int id = Integer.parseInt(wordList[1]);
			int count = Integer.parseInt(wordList[2]);
			if ((id < 1) || (count < 1) || (activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
			{
				activeChar.sendMessage("Usage: //give_item id count <target>");
				return false;
			}
			ItemFunctions.addItem(activeChar.getTarget().getPlayer(), id, count, true);
		}
		else if (fullString.startsWith("admin_add_bang"))
		{
			if (!Config.ALT_PCBANG_POINTS_ENABLED)
			{
				activeChar.sendMessage("Error! Pc Bang Points service disabled!");
				return true;
			}
			if (wordList.length < 1)
			{
				activeChar.sendMessage("Usage: //add_bang count <target>");
				return false;
			}
			int count = Integer.parseInt(wordList[1]);
			if ((count < 1) || (activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
			{
				activeChar.sendMessage("Usage: //add_bang count <target>");
				return false;
			}
			Player target = activeChar.getTarget().getPlayer();
			target.addPcBangPoints(count, false);
			activeChar.sendMessage("You have added " + count + " Pc Bang Points to " + target.getName());
		}
		else if (fullString.startsWith("admin_set_bang"))
		{
			if (!Config.ALT_PCBANG_POINTS_ENABLED)
			{
				activeChar.sendMessage("Error! Pc Bang Points service disabled!");
				return true;
			}
			if (wordList.length < 1)
			{
				activeChar.sendMessage("Usage: //set_bang count <target>");
				return false;
			}
			int count = Integer.parseInt(wordList[1]);
			if ((count < 1) || (activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
			{
				activeChar.sendMessage("Usage: //set_bang count <target>");
				return false;
			}
			Player target = activeChar.getTarget().getPlayer();
			target.setPcBangPoints(count);
			target.sendMessage("Your Pc Bang Points count is now " + count);
			target.sendPacket(new ExPCCafePointInfo(target, count, 1, 2, 12));
			activeChar.sendMessage("You have set " + target.getName() + "'s Pc Bang Points to " + count);
		}
		else if (fullString.startsWith("admin_reset_mentor_penalty"))
		{
			if (activeChar.getTarget().getPlayer() == null)
			{
				activeChar.sendMessage("You have no target selected.");
				return false;
			}
			if (Mentoring.getTimePenalty(activeChar.getTargetId()) > 0)
			{
				Mentoring.setTimePenalty(activeChar.getTargetId(), 0, -1);
				activeChar.getTarget().getPlayer().sendMessage("Your mentor penalty has been lifted by a GM.");
				activeChar.sendMessage(activeChar.getTarget().getPlayer().getName() + "'s mentor penalty has been lifted.");
			}
			else
			{
				activeChar.sendMessage("The selected character has no penalty.");
				return false;
			}
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
	 * Method listCharacters.
	 * @param activeChar Player
	 * @param page int
	 */
	private void listCharacters(Player activeChar, int page)
	{
		List<Player> players = GameObjectsStorage.getAllPlayers();
		int MaxCharactersPerPage = 20;
		int MaxPages = players.size() / MaxCharactersPerPage;
		if (players.size() > (MaxCharactersPerPage * MaxPages))
		{
			MaxPages++;
		}
		if (page > MaxPages)
		{
			page = MaxPages;
		}
		int CharactersStart = MaxCharactersPerPage * page;
		int CharactersEnd = players.size();
		if ((CharactersEnd - CharactersStart) > MaxCharactersPerPage)
		{
			CharactersEnd = CharactersStart + MaxCharactersPerPage;
		}
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=270>You can find a character by writing his name and</td></tr>");
		replyMSG.append("<tr><td width=270>clicking Find bellow.<br></td></tr>");
		replyMSG.append("<tr><td width=270>Note: Names should be written case sensitive.</td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<center><table><tr><td>");
		replyMSG.append("<edit var=\"character_name\" width=80></td><td><button value=\"Find\" action=\"bypass -h admin_find_character $character_name\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
		replyMSG.append("</td></tr></table></center><br><br>");
		for (int x = 0; x < MaxPages; x++)
		{
			int pagenr = x + 1;
			replyMSG.append("<center><a action=\"bypass -h admin_show_characters " + x + "\">Page " + pagenr + "</a></center>");
		}
		replyMSG.append("<br>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=80>Name:</td><td width=110>Class:</td><td width=40>Level:</td></tr>");
		for (int i = CharactersStart; i < CharactersEnd; i++)
		{
			Player p = players.get(i);
			replyMSG.append("<tr><td width=80>" + "<a action=\"bypass -h admin_character_list " + p.getName() + "\">" + p.getName() + "</a></td><td width=110>" + HtmlUtils.htmlClassName(p.getClassId().getId()) + "</td><td width=40>" + p.getLevel() + "</td></tr>");
		}
		replyMSG.append("</table>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method showCharacterList.
	 * @param activeChar Player
	 * @param player Player
	 */
	public static void showCharacterList(Player activeChar, Player player)
	{
		if (player == null)
		{
			GameObject target = activeChar.getTarget();
			if ((target != null) && target.isPlayer())
			{
				player = (Player) target;
			}
			else
			{
				return;
			}
		}
		else
		{
			activeChar.setTarget(player);
		}
		String clanName = "No Clan";
		if (player.getClan() != null)
		{
			clanName = player.getClan().getName() + "/" + player.getClan().getLevel();
		}
		NumberFormat df = NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(1);
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_show_characters 0\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table><br>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=100>Account/IP:</td><td>" + player.getAccountName() + "/" + player.getIP() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Name/Level:</td><td>" + player.getName() + "/" + player.getLevel() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Class/Id:</td><td>" + HtmlUtils.htmlClassName(player.getClassId().getId()) + "/" + player.getClassId().getId() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Clan/Level:</td><td>" + clanName + "</td></tr>");
		replyMSG.append("<tr><td width=100>Exp/Sp:</td><td>" + player.getExp() + "/" + player.getSp() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Cur/Max Hp:</td><td>" + (int) player.getCurrentHp() + "/" + player.getMaxHp() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Cur/Max Mp:</td><td>" + (int) player.getCurrentMp() + "/" + player.getMaxMp() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Cur/Max Load:</td><td>" + player.getCurrentLoad() + "/" + player.getMaxLoad() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Patk/Matk:</td><td>" + player.getPAtk(null) + "/" + player.getMAtk(null, null) + "</td></tr>");
		replyMSG.append("<tr><td width=100>Pdef/Mdef:</td><td>" + player.getPDef(null) + "/" + player.getMDef(null, null) + "</td></tr>");
		replyMSG.append("<tr><td width=100>PAtkSpd/MAtkSpd:</td><td>" + player.getPAtkSpd() + "/" + player.getMAtkSpd() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Acc/Evas:</td><td>" + player.getAccuracy() + "/" + player.getEvasionRate(null) + "</td></tr>");
		replyMSG.append("<tr><td width=100>Crit/MCrit:</td><td>" + player.getCriticalHit(null, null) + "/" + df.format(player.getMagicCriticalRate(null, null)) + "%</td></tr>");
		replyMSG.append("<tr><td width=100>CritDmg/MCritDmg:</td><td>" + player.getCriticalDmg(null, null) + "/" + player.getMagicCriticalDmg(null, null) + "</td></tr>");
		replyMSG.append("<tr><td width=100>Walk/Run:</td><td>" + player.getWalkSpeed() + "/" + player.getRunSpeed() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Karma/Fame:</td><td>" + player.getKarma() + "/" + player.getFame() + "</td></tr>");
		replyMSG.append("<tr><td width=100>PvP/PK:</td><td>" + player.getPvpKills() + "/" + player.getPkKills() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Coordinates:</td><td>" + player.getX() + "," + player.getY() + "," + player.getZ() + "</td></tr>");
		replyMSG.append("<tr><td width=100>Direction:</td><td>" + PositionUtils.getDirectionTo(player, activeChar) + "</td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<table<tr>");
		replyMSG.append("<td><button value=\"Skills\" action=\"bypass -h admin_show_skills\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Effects\" action=\"bypass -h admin_show_effects\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Actions\" action=\"bypass -h admin_character_actions\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr><tr>");
		replyMSG.append("<td><button value=\"Stats\" action=\"bypass -h admin_edit_character\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Exp & Sp\" action=\"bypass -h admin_add_exp_sp_to_character\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td></td>");
		replyMSG.append("</tr></table></body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method setTargetKarma.
	 * @param activeChar Player
	 * @param newKarma int
	 */
	private void setTargetKarma(Player activeChar, int newKarma)
	{
		GameObject target = activeChar.getTarget();
		if (target == null)
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return;
		}
		Player player;
		if (target.isPlayer())
		{
			player = (Player) target;
		}
		else
		{
			return;
		}
		if (newKarma >= 0)
		{
			int oldKarma = player.getKarma();
			player.setKarma(newKarma);
			player.sendMessage("Admin has changed your karma from " + oldKarma + " to " + newKarma + ".");
			activeChar.sendMessage("Successfully Changed karma for " + player.getName() + " from (" + oldKarma + ") to (" + newKarma + ").");
		}
		else
		{
			activeChar.sendMessage("You must enter a value for karma greater than or equal to 0.");
		}
	}
	
	/**
	 * Method setTargetFame.
	 * @param activeChar Player
	 * @param newFame int
	 */
	private void setTargetFame(Player activeChar, int newFame)
	{
		GameObject target = activeChar.getTarget();
		if (target == null)
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return;
		}
		Player player;
		if (target.isPlayer())
		{
			player = (Player) target;
		}
		else
		{
			return;
		}
		if (newFame >= 0)
		{
			int oldFame = player.getFame();
			player.setFame(newFame, "Admin");
			player.sendMessage("Admin has changed your fame from " + oldFame + " to " + newFame + ".");
			activeChar.sendMessage("Successfully Changed fame for " + player.getName() + " from (" + oldFame + ") to (" + newFame + ").");
		}
		else
		{
			activeChar.sendMessage("You must enter a value for fame greater than or equal to 0.");
		}
	}
	
	/**
	 * Method adminModifyCharacter.
	 * @param activeChar Player
	 * @param modifications String
	 */
	private void adminModifyCharacter(Player activeChar, String modifications)
	{
		GameObject target = activeChar.getTarget();
		if ((target == null) || !target.isPlayer())
		{
			activeChar.sendPacket(Msg.SELECT_TARGET);
			return;
		}
		Player player = (Player) target;
		String[] strvals = modifications.split("&");
		Integer[] vals = new Integer[strvals.length];
		for (int i = 0; i < strvals.length; i++)
		{
			strvals[i] = strvals[i].trim();
			vals[i] = strvals[i].isEmpty() ? null : Integer.valueOf(strvals[i]);
		}
		if (vals[0] != null)
		{
			player.setCurrentHp(vals[0], false);
		}
		if (vals[1] != null)
		{
			player.setCurrentMp(vals[1]);
		}
		if (vals[2] != null)
		{
			player.setKarma(vals[2]);
		}
		if (vals[3] != null)
		{
			player.setPvpFlag(vals[3]);
		}
		if (vals[4] != null)
		{
			player.setPvpKills(vals[4]);
		}
		if (vals[5] != null)
		{
			player.setClassId(vals[5], true, false);
		}
		editCharacter(activeChar);
		player.broadcastCharInfo();
		player.decayMe();
		player.spawnMe(activeChar.getLoc());
	}
	
	/**
	 * Method editCharacter.
	 * @param activeChar Player
	 */
	private void editCharacter(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		if ((target == null) || !target.isPlayer())
		{
			activeChar.sendPacket(Msg.SELECT_TARGET);
			return;
		}
		Player player = (Player) target;
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		replyMSG.append("<center>Editing character: " + player.getName() + "</center><br>");
		replyMSG.append("<table width=250>");
		replyMSG.append("<tr><td width=40></td><td width=70>Curent:</td><td width=70>Max:</td><td width=70></td></tr>");
		replyMSG.append("<tr><td width=40>HP:</td><td width=70>" + player.getCurrentHp() + "</td><td width=70>" + player.getMaxHp() + "</td><td width=70>Karma: " + player.getKarma() + "</td></tr>");
		replyMSG.append("<tr><td width=40>MP:</td><td width=70>" + player.getCurrentMp() + "</td><td width=70>" + player.getMaxMp() + "</td><td width=70>Pvp Kills: " + player.getPvpKills() + "</td></tr>");
		replyMSG.append("<tr><td width=40>Load:</td><td width=70>" + player.getCurrentLoad() + "</td><td width=70>" + player.getMaxLoad() + "</td><td width=70>Pvp Flag: " + player.getPvpFlag() + "</td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("<table width=270><tr><td>Class<?> Template Id: " + player.getClassId() + "/" + player.getClassId().getId() + "</td></tr></table><br>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td>Note: Fill all values before saving the modifications.</td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=50>Hp:</td><td><edit var=\"hp\" width=50></td><td width=50>Mp:</td><td><edit var=\"mp\" width=50></td></tr>");
		replyMSG.append("<tr><td width=50>Pvp Flag:</td><td><edit var=\"pvpflag\" width=50></td><td width=50>Karma:</td><td><edit var=\"karma\" width=50></td></tr>");
		replyMSG.append("<tr><td width=50>Class<?> Id:</td><td><edit var=\"classid\" width=50></td><td width=50>Pvp Kills:</td><td><edit var=\"pvpkills\" width=50></td></tr>");
		replyMSG.append("</table><br>");
		replyMSG.append("<center><button value=\"Save Changes\" action=\"bypass -h admin_save_modifications $hp & $mp & $karma & $pvpflag & $pvpkills & $classid &\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center><br>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method showCharacterActions.
	 * @param activeChar Player
	 */
	private void showCharacterActions(Player activeChar)
	{
		GameObject target = activeChar.getTarget();
		Player player;
		if ((target != null) && target.isPlayer())
		{
			player = (Player) target;
		}
		else
		{
			return;
		}
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table><br><br>");
		replyMSG.append("<center>Admin Actions for: " + player.getName() + "</center><br>");
		replyMSG.append("<center><table width=200><tr>");
		replyMSG.append("<td width=100>Argument(*):</td><td width=100><edit var=\"arg\" width=100></td>");
		replyMSG.append("</tr></table><br></center>");
		replyMSG.append("<table width=270>");
		replyMSG.append("<tr><td width=90><button value=\"Teleport\" action=\"bypass -h admin_teleportto " + player.getName() + "\" width=85 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=90><button value=\"Recall\" action=\"bypass -h admin_recall " + player.getName() + "\" width=85 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=90><button value=\"Quests\" action=\"bypass -h admin_quests " + player.getName() + "\" width=85 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method findCharacter.
	 * @param activeChar Player
	 * @param CharacterToFind String
	 */
	private void findCharacter(Player activeChar, String CharacterToFind)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		int CharactersFound = 0;
		StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_show_characters 0\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		for (Player element : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (element.getName().startsWith(CharacterToFind))
			{
				CharactersFound = CharactersFound + 1;
				replyMSG.append("<table width=270>");
				replyMSG.append("<tr><td width=80>Name</td><td width=110>Class</td><td width=40>Level</td></tr>");
				replyMSG.append("<tr><td width=80><a action=\"bypass -h admin_character_list " + element.getName() + "\">" + element.getName() + "</a></td><td width=110>" + HtmlUtils.htmlClassName(element.getClassId().getId()) + "</td><td width=40>" + element.getLevel() + "</td></tr>");
				replyMSG.append("</table>");
			}
		}
		if (CharactersFound == 0)
		{
			replyMSG.append("<table width=270>");
			replyMSG.append("<tr><td width=270>Your search did not find any characters.</td></tr>");
			replyMSG.append("<tr><td width=270>Please try again.<br></td></tr>");
			replyMSG.append("</table><br>");
			replyMSG.append("<center><table><tr><td>");
			replyMSG.append("<edit var=\"character_name\" width=80></td><td><button value=\"Find\" action=\"bypass -h admin_find_character $character_name\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
			replyMSG.append("</td></tr></table></center>");
		}
		else
		{
			replyMSG.append("<center><br>Found " + CharactersFound + " character");
			if (CharactersFound == 1)
			{
				replyMSG.append('.');
			}
			else if (CharactersFound > 1)
			{
				replyMSG.append("s.");
			}
		}
		replyMSG.append("</center></body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method addExpSp.
	 * @param activeChar Player
	 */
	private void addExpSp(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		Player player;
		if ((target != null) && target.isPlayer() && ((activeChar == target) || activeChar.getPlayerAccess().CanEditCharAll))
		{
			player = (Player) target;
		}
		else
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return;
		}
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuilder replyMSG = new StringBuilder("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>Character Selection Menu</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<br><br>");
		replyMSG.append("<table width=270><tr><td>Name: " + player.getName() + "</td></tr>");
		replyMSG.append("<tr><td>Lv: " + player.getLevel() + " " + HtmlUtils.htmlClassName(player.getClassId().getId()) + "</td></tr>");
		replyMSG.append("<tr><td>Exp: " + player.getExp() + "</td></tr>");
		replyMSG.append("<tr><td>Sp: " + player.getSp() + "</td></tr></table>");
		replyMSG.append("<br><table width=270><tr><td>Note: Dont forget that modifying players skills can</td></tr>");
		replyMSG.append("<tr><td>ruin the game...</td></tr></table><br>");
		replyMSG.append("<table width=270><tr><td>Note: Fill all values before saving the modifications.,</td></tr>");
		replyMSG.append("<tr><td>Note: Use 0 if no changes are needed.</td></tr></table><br>");
		replyMSG.append("<center><table><tr>");
		replyMSG.append("<td>Exp: <edit var=\"exp_to_add\" width=50></td>");
		replyMSG.append("<td>Sp:  <edit var=\"sp_to_add\" width=50></td>");
		replyMSG.append("<td>&nbsp;<button value=\"Save Changes\" action=\"bypass -h admin_add_exp_sp $exp_to_add & $sp_to_add &\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table></center>");
		replyMSG.append("<center><table><tr>");
		replyMSG.append("<td>LvL: <edit var=\"lvl\" width=50></td>");
		replyMSG.append("<td>&nbsp;<button value=\"Set Level\" action=\"bypass -h admin_setlevel $lvl\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table></center>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method adminAddExpSp.
	 * @param activeChar Player
	 * @param exp long
	 * @param sp int
	 */
	private void adminAddExpSp(Player activeChar, long exp, int sp)
	{
		if (!activeChar.getPlayerAccess().CanEditCharAll)
		{
			activeChar.sendMessage("You have not enough privileges, for use this function.");
			return;
		}
		final GameObject target = activeChar.getTarget();
		if (target == null)
		{
			activeChar.sendPacket(Msg.SELECT_TARGET);
			return;
		}
		if (!target.isPlayable())
		{
			activeChar.sendPacket(Msg.INVALID_TARGET);
			return;
		}
		Playable playable = (Playable) target;
		playable.addExpAndSp(exp, sp);
		activeChar.sendMessage("Added " + exp + " experience and " + sp + " SP to " + playable.getName() + ".");
	}
	
	/**
	 * Method setSubclass.
	 * @param activeChar Player
	 * @param player Player
	 */
	private void setSubclass(final Player activeChar, final Player player)
	{
		StringBuilder content = new StringBuilder("<html><body>");
		NpcHtmlMessage html = new NpcHtmlMessage(5);
		Set<ClassId> subsAvailable = SubClassInfo.getAvailableSubClasses(player, null, null, true);
		if ((subsAvailable != null) && !subsAvailable.isEmpty())
		{
			content.append("Add Subclass:<br>Which subclass do you wish to add?<br>");
			for (ClassId subClass : subsAvailable)
			{
				content.append("<a action=\"bypass -h admin_setsubclass " + subClass.ordinal() + "\">" + formatClassForDisplay(subClass) + "</a><br>");
			}
		}
		else
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.NoSubAtThisTime", activeChar));
			return;
		}
		content.append("</body></html>");
		html.setHtml(content.toString());
		activeChar.sendPacket(html);
	}
	
	/**
	 * Method formatClassForDisplay.
	 * @param className ClassId
	 * @return String
	 */
	private String formatClassForDisplay(ClassId className)
	{
		String classNameStr = className.toString();
		char[] charArray = classNameStr.toCharArray();
		for (int i = 1; i < charArray.length; i++)
		{
			if (Character.isUpperCase(charArray[i]))
			{
				classNameStr = classNameStr.substring(0, i) + " " + classNameStr.substring(i);
			}
		}
		return classNameStr;
	}
}
