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

import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.BuyListHolder;
import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExBuySellList;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.utils.BbsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommunityBoard implements ScriptFile, ICommunityBoardHandler
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CommunityBoard.class);
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: service loaded.");
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
	 * @return String[] * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#getBypassCommands()
	 */
	@Override
	public String[] getBypassCommands()
	{
		return new String[]
		{
			"_bbshome",
			"_bbslink",
			"_bbsmultisell",
			"_bbssell",
			"_bbspage",
			"_bbsscripts"
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
		if (!Config.ALLOW_COMMUNITYBOARD_IN_COMBAT && player.isInCombat())
		{
			player.sendMessage(new CustomMessage("scripts.services.community.CommunityBoard.NotUse", player));
			return;
		}
		StringTokenizer st = new StringTokenizer(bypass, "_");
		String cmd = st.nextToken();
		String html = "";
		if ("bbshome".equals(cmd))
		{
			StringTokenizer p = new StringTokenizer(Config.BBS_DEFAULT, "_");
			String dafault = p.nextToken();
			if (dafault.equals(cmd))
			{
				html = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/main.htm", player);
				html = BbsUtil.htmlAll(html, player);
			}
			else
			{
				onBypassCommand(player, Config.BBS_DEFAULT);
				return;
			}
		}
		else if ("bbslink".equals(cmd))
		{
			html = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "bbs_homepage.htm", player);
			html = BbsUtil.htmlAll(html, player);
		}
		else if (bypass.startsWith("_bbspage"))
		{
			String[] b = bypass.split(":");
			String page = b[1];
			html = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/" + page + ".htm", player);
			html = BbsUtil.htmlAll(html, player);
		}
		else if (bypass.startsWith("_bbsmultisell"))
		{
			if (!CheckCondition(player))
			{
				return;
			}
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String[] mBypass = st2.nextToken().split(":");
			String pBypass = st2.hasMoreTokens() ? st2.nextToken() : null;
			if (pBypass != null)
			{
				ICommunityBoardHandler handler = CommunityBoardManager.getInstance().getCommunityHandler(pBypass);
				if (handler != null)
				{
					handler.onBypassCommand(player, pBypass);
				}
			}
			int listId = Integer.parseInt(mBypass[1]);
			MultiSellHolder.getInstance().SeparateAndSend(listId, player, 0);
			return;
		}
		else if (bypass.startsWith("_bbssell"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			st2.nextToken().split(":");
			String pBypass = st2.hasMoreTokens() ? st2.nextToken() : null;
			if (pBypass != null)
			{
				ICommunityBoardHandler handler = CommunityBoardManager.getInstance().getCommunityHandler(pBypass);
				if (handler != null)
				{
					handler.onBypassCommand(player, pBypass);
				}
			}
			player.setIsBBSUse(true);
			NpcTradeList list = BuyListHolder.getInstance().getBuyList(-1);
			player.sendPacket(new ExBuySellList.BuyList(list, player, 0.), new ExBuySellList.SellRefundList(player, false));
			player.sendChanges();
			return;
		}
		else if (bypass.startsWith("_bbsscripts"))
		{
			StringTokenizer st2 = new StringTokenizer(bypass, ";");
			String sBypass = st2.nextToken().substring(12);
			String pBypass = st2.hasMoreTokens() ? st2.nextToken() : null;
			if (pBypass != null)
			{
				ICommunityBoardHandler handler = CommunityBoardManager.getInstance().getCommunityHandler(pBypass);
				if (handler != null)
				{
					handler.onBypassCommand(player, pBypass);
				}
			}
			String[] word = sBypass.split("\\s+");
			String[] args = sBypass.substring(word[0].length()).trim().split("\\s+");
			String[] path = word[0].split(":");
			if (path.length != 2)
			{
				return;
			}
			Scripts.getInstance().callScripts(player, path[0], path[1], word.length == 1 ? new Object[] {} : new Object[]
			{
				args
			});
			return;
		}
		ShowBoard.separateAndSend(html, player);
	}
	
	/**
	 * Method CheckCondition.
	 * @param player Player
	 * @return boolean
	 */
	private boolean CheckCondition(Player player)
	{
		if (!Config.ALLOW_COMMUNITYBOARD_IN_COMBAT && ((player.getPvpFlag() != 0) || player.isInDuel() || player.isInCombat() || player.isAttackingNow()))
		{
			player.sendMessage("During combat, you can not use this feature.");
			return false;
		}
		return true;
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
}
