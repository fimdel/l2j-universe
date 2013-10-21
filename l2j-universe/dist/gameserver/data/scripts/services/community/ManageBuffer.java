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

import java.util.List;
import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.dao.CommunityBufferDAO;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.ManageBbsBuffer;
import lineage2.gameserver.model.ManageBbsBuffer.SBufferScheme;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.BbsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vegax
 * @version $Revision: 1.0 $
 * Summons Buff fix by vegax for L2JEuropa
 */
public class ManageBuffer extends Functions implements ScriptFile, ICommunityBoardHandler
{
	/**
	 * Field _log.
	 */
	static final Logger _log = LoggerFactory.getLogger(ManageBuffer.class);
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.COMMUNITYBOARD_ENABLED && Config.COMMUNITYBOARD_BUFFER_ENABLED)
		{
			_log.info("CommunityBoard: Buffer Community service loaded.");
			CommunityBufferDAO.getInstance().select();
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
		if (Config.COMMUNITYBOARD_ENABLED && Config.COMMUNITYBOARD_BUFFER_ENABLED)
		{
			ManageBbsBuffer.getSchemeList().clear();
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
	 * @return String[] * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#getBypassCommands()
	 */
	@Override
	public String[] getBypassCommands()
	{
		return new String[]
		{
			"_bbsbuff",
			"_bbsbaim",
			"_bbsbsingle",
			"_bbsbsave",
			"_bbsbrestore",
			"_bbsbdelete",
			"_bbsbregen",
			"_bbsbcansel",
			"_bbsblist"
		};
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
		if (!CheckCondition(player))
		{
			return;
		}
		if (bypass.startsWith("_bbsbuff"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			ShowHtml(mBypass.length == 1 ? "index" : mBypass[1], player);
		}
		if (bypass.startsWith("_bbsblist"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			int pice = Config.COMMUNITYBOARD_BUFF_PICE * (mBypass[1].startsWith("mage") ? Config.COMMUNITI_LIST_MAGE_SUPPORT.size() : Config.COMMUNITI_LIST_FIGHTER_SUPPORT.size());
			if (player.getAdena() < pice)
			{
				player.sendMessage("It is not enough money!");
				ShowHtml(mBypass[2], player);
				return;
			}
			GroupBuff(player, mBypass[1].startsWith("mage") ? Config.COMMUNITI_LIST_MAGE_SUPPORT : Config.COMMUNITI_LIST_FIGHTER_SUPPORT);
			player.reduceAdena(pice);
			ShowHtml(mBypass[2], player);
		}
		else if (bypass.startsWith("_bbsbsingle"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			int id = Integer.parseInt(mBypass[1]);
			int lvl = Integer.parseInt(mBypass[2]);
			int time = Config.COMMUNITYBOARD_BUFF_TIME;
			int pice = Config.COMMUNITYBOARD_BUFF_PICE;
			String page = mBypass[3];
			if (player.getAdena() < pice)
			{
				player.sendMessage("It is not enough money!");
				ShowHtml(page, player);
				return;
			}
			if (!Config.COMMUNITYBOARD_BUFF_ALLOW.contains(id))
			{
				player.sendMessage("Invalid effect!");
				ShowHtml(page, player);
				return;
			}
			Skill skill = SkillTable.getInstance().getInfo(id, lvl);
			if (!player.getVarB("isPlayerBuff"))
			{
					for (Summon summon : player.getSummonList())
					{
						for (EffectTemplate et : skill.getEffectTemplates())
						{
							Env env = new Env(summon, summon, skill);
							Effect effect = et.getEffect(env);
							effect.setPeriod(time);
							summon.getEffectList().addEffect(effect);
							summon.updateEffectIconsImpl();
						}
					}
			}
			else
			{
				for (EffectTemplate et : skill.getEffectTemplates())
				{
					Env env = new Env(player, player, skill);
					Effect effect = et.getEffect(env);
					effect.setPeriod(time);
					player.getEffectList().addEffect(effect);
					player.updateEffectIconsImpl();
				}
			}
			player.reduceAdena(pice);
			ShowHtml(page, player);
		}
		else if (bypass.startsWith("_bbsbaim"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			player.setVar("isPlayerBuff", player.getVarB("isPlayerBuff") ? "0" : "1", -1);
			ShowHtml(mBypass[1], player);
		}
		else if (bypass.startsWith("_bbsbregen"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			int pice = Config.COMMUNITYBOARD_BUFF_PICE;
			if (player.getAdena() < (pice * 10))
			{
				player.sendMessage("It is not enough money!");
				ShowHtml(mBypass[1], player);
				return;
			}
			if (!player.getVarB("isPlayerBuff"))
			{
				if (player.getSummonList().getServitors() != null)
				{
					for (Summon summon : player.getSummonList())
					{
						summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp());
						summon.setCurrentCp(summon.getMaxCp(), true);
					}
				}
			}
			else
			{
				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
				player.setCurrentCp(player.getMaxCp());
			}
			player.reduceAdena(pice * 10);
			ShowHtml(mBypass[1], player);
		}
		else if (bypass.startsWith("_bbsbcansel"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			if (player.getVarB("isPlayerBuff") && (player.getEffectList().getEffectsBySkillId(Skill.SKILL_RAID_CURSE) == null))
			{
				player.getEffectList().stopAllEffects();
			}
			else if (!player.getVarB("isPlayerBuff") && (player.getSummonList().getServitors() != null))
			{
				for (Summon summon : player.getSummonList())
				{
					summon.getEffectList().stopAllEffects();
				}
			}
			ShowHtml(mBypass[1], player);
		}
		else if (bypass.startsWith("_bbsbsave"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			String name = "";
			try
			{
				if (mBypass[2].length() > 1)
				{
					name = mBypass[2].substring(1);
				}
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				player.sendMessage("You did not enter a name to save!");
				return;
			}
			SBufferScheme scheme = new SBufferScheme();
			if (ManageBbsBuffer.getCountOnePlayer(player.getObjectId()) >= 3)
			{
				player.sendMessage("Exceeded the number of schemes!");
				ShowHtml(mBypass[1], player);
				return;
			}
			if (ManageBbsBuffer.existName(player.getObjectId(), name))
			{
				player.sendMessage("Scheme with that name already exists!");
				ShowHtml(mBypass[1], player);
				return;
			}
			if (name.length() > 15)
			{
				name = name.substring(0, 15);
			}
			if (name.length() > 0)
			{
				scheme.obj_id = player.getObjectId();
				scheme.name = name;
				Effect skill[] = player.getEffectList().getAllFirstEffects();
				if (skill != null)
				{
					for (Effect element : skill)
					{
						if (Config.COMMUNITYBOARD_BUFF_ALLOW.contains(element.getSkill().getId()))
						{
							scheme.skills_id.add(element.getSkill().getId());
						}
					}
					if (scheme.skills_id != null)
					{
						CommunityBufferDAO.getInstance().insert(scheme);
					}
				}
			}
			ShowHtml(mBypass[1], player);
		}
		else if (bypass.startsWith("_bbsbdelete"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			CommunityBufferDAO.getInstance().delete(ManageBbsBuffer.getScheme(Integer.parseInt(mBypass[1]), player.getObjectId()));
			ShowHtml(mBypass[3], player);
		}
		else if (bypass.startsWith("_bbsbrestore"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			int pice = Config.COMMUNITYBOARD_BUFF_SAVE_PICE;
			if (player.getAdena() < pice)
			{
				player.sendMessage("It is not enough money!");
				ShowHtml(mBypass[3], player);
				return;
			}
			SBufferScheme scheme = ManageBbsBuffer.getScheme(Integer.parseInt(mBypass[1]), player.getObjectId());
			GroupBuff(player, scheme.skills_id);
			player.reduceAdena(pice);
			ShowHtml(mBypass[3], player);
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
	 * Method ShowHtml.
	 * @param name String
	 * @param player Player
	 */
	private void ShowHtml(String name, Player player)
	{
		String html = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/buffer/" + name + ".htm", player);
		html = html.replaceFirst("%aim%", player.getVarB("isPlayerBuff") ? "Character" : "Summon");
		html = html.replace("%pice%", GetStringCount(Config.COMMUNITYBOARD_BUFF_PICE));
		html = html.replace("%group_pice%", GetStringCount(Config.COMMUNITYBOARD_BUFF_SAVE_PICE));
		StringBuilder content = new StringBuilder("");
		content.append("<table width=120>");
		for (SBufferScheme sm : ManageBbsBuffer.getSchemePlayer(player.getObjectId()))
		{
			content.append("<tr>");
			content.append("<td>");
			content.append("<button value=\"" + sm.name + "\" action=\"bypass _bbsbrestore:" + sm.id + ":" + sm.name + ":" + name + ";\" width=105 height=20 back=\"L2UI_ct1.Button_DF_Down\" fore=\"L2UI_ct1.Button_DF\">");
			content.append("</td>");
			content.append("<td>");
			content.append("<button value=\"-\" action=\"bypass _bbsbdelete:" + sm.id + ":" + sm.name + ":" + name + ";\" width=20 height=20 back=\"L2UI_ct1.Button_DF_Down\" fore=\"L2UI_ct1.Button_DF\">");
			content.append("</td>");
			content.append("</tr>");
		}
		content.append("</table>");
		html = html.replace("%list_sheme%", content.toString());
		html = BbsUtil.htmlBuff(html, player);
		ShowBoard.separateAndSend(html, player);
	}
	
	/**
	 * Method GroupBuff.
	 * @param player Player
	 * @param list List<Integer>
	 */
	private void GroupBuff(Player player, List<Integer> list)
	{
		int time = Config.COMMUNITYBOARD_BUFF_TIME;
		Skill skill = null;
		for (int i : list)
		{
			int lvl = SkillTable.getInstance().getBaseLevel(i);
			if (!Config.COMMUNITYBOARD_BUFF_ALLOW.contains(i))
			{
				continue;
			}
			skill = SkillTable.getInstance().getInfo(i, lvl);
			if (!player.getVarB("isPlayerBuff") && (player.getSummonList().getServitors() != null))
			{
				for (EffectTemplate et : skill.getEffectTemplates())
				{
					for (Summon summon : player.getSummonList())
					{
						Env env = new Env(summon, summon, skill);
						Effect effect = et.getEffect(env);
						effect.setPeriod(time);
						summon.getEffectList().addEffect(effect);
						summon.updateEffectIconsImpl();
					}
				}
			}
			else
			{
				for (EffectTemplate et : skill.getEffectTemplates())
				{
					Env env = new Env(player, player, skill);
					Effect effect = et.getEffect(env);
					effect.setPeriod(time);
					player.getEffectList().addEffect(effect);
					player.updateEffectIconsImpl();
				}
			}
		}
	}
	
	/**
	 * Method CheckCondition.
	 * @param player Player
	 * @return boolean
	 */
	private static boolean CheckCondition(Player player)
	{
		if (player == null)
		{
			return false;
		}
		if (!Config.USE_BBS_BUFER_IS_COMBAT && ((player.getPvpFlag() != 0) || player.isInDuel() || player.isInCombat() || player.isAttackingNow()))
		{
			player.sendMessage("During combat, you can not use this feature.");
			return false;
		}
		if (player.isInOlympiadMode())
		{
			player.sendMessage("During the Olympics you can not use this feature.");
			return false;
		}
		if ((player.getReflection().getId() != 0) && !Config.COMMUNITYBOARD_INSTANCE_ENABLED)
		{
			player.sendMessage("Buff is only available in the real world.");
			return false;
		}
		if (!Config.COMMUNITYBOARD_BUFFER_ENABLED)
		{
			player.sendMessage("Buff off function.");
			return false;
		}
		if (!Config.COMMUNITYBOARD_EVENTS_ENABLED)
		{
			if (player.getTeam() != TeamType.NONE)
			{
				player.sendMessage("You can not use the buff during Events.");
				return false;
			}
		}
		return true;
	}
}
