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
package lineage2.gameserver.instancemanager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.Strings;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BypassManager
{
	/**
	 * Field p.
	 */
	private static final Pattern p = Pattern.compile("\"(bypass +-h +)(.+?)\"");
	
	/**
	 * @author Mobius
	 */
	public static enum BypassType
	{
		/**
		 * Field ENCODED.
		 */
		ENCODED,
		/**
		 * Field ENCODED_BBS.
		 */
		ENCODED_BBS,
		/**
		 * Field SIMPLE.
		 */
		SIMPLE,
		/**
		 * Field SIMPLE_BBS.
		 */
		SIMPLE_BBS,
		/**
		 * Field SIMPLE_DIRECT.
		 */
		SIMPLE_DIRECT
	}
	
	/**
	 * Method getBypassType.
	 * @param bypass String
	 * @return BypassType
	 */
	public static BypassType getBypassType(String bypass)
	{
		switch (bypass.charAt(0))
		{
			case '0':
				return BypassType.ENCODED;
			case '1':
				return BypassType.ENCODED_BBS;
			default:
				if (Strings.matches(bypass, "^(_mrsl|_diary|_match|manor_menu_select|_match|_olympiad).*", Pattern.DOTALL))
				{
					return BypassType.SIMPLE;
				}
				return BypassType.SIMPLE_DIRECT;
		}
	}
	
	/**
	 * Method encode.
	 * @param html String
	 * @param bypassStorage List<String>
	 * @param bbs boolean
	 * @return String
	 */
	public static String encode(String html, List<String> bypassStorage, boolean bbs)
	{
		Matcher m = p.matcher(html);
		StringBuffer sb = new StringBuffer();
		while (m.find())
		{
			String bypass = m.group(2);
			String code = bypass;
			String params = "";
			int i = bypass.indexOf(" $");
			boolean use_params = i >= 0;
			if (use_params)
			{
				code = bypass.substring(0, i);
				params = bypass.substring(i).replace("$", "\\$");
			}
			if (bbs)
			{
				m.appendReplacement(sb, "\"bypass -h 1" + Integer.toHexString(bypassStorage.size()) + params + "\"");
			}
			else
			{
				m.appendReplacement(sb, "\"bypass -h 0" + Integer.toHexString(bypassStorage.size()) + params + "\"");
			}
			bypassStorage.add(code);
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * Method decode.
	 * @param bypass String
	 * @param bypassStorage List<String>
	 * @param bbs boolean
	 * @param player Player
	 * @return DecodedBypass
	 */
	public static DecodedBypass decode(String bypass, List<String> bypassStorage, boolean bbs, Player player)
	{
		synchronized (bypassStorage)
		{
			String[] bypass_parsed = bypass.split(" ");
			int idx = Integer.parseInt(bypass_parsed[0].substring(1), 16);
			String bp;
			try
			{
				bp = bypassStorage.get(idx);
			}
			catch (Exception e)
			{
				bp = null;
			}
			if (bp == null)
			{
				Log.add("Can't decode bypass (bypass not exists): " + (bbs ? "[bbs] " : "") + bypass + " / Player: " + player.getName() + " / Npc: " + (player.getLastNpc() == null ? "null" : player.getLastNpc().getName()), "debug_bypass");
				return null;
			}
			DecodedBypass result = null;
			result = new DecodedBypass(bp, bbs);
			for (int i = 1; i < bypass_parsed.length; i++)
			{
				result.bypass += " " + bypass_parsed[i];
			}
			result.trim();
			return result;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class DecodedBypass
	{
		/**
		 * Field bypass.
		 */
		public String bypass;
		/**
		 * Field bbs.
		 */
		public boolean bbs;
		/**
		 * Field handler.
		 */
		public ICommunityBoardHandler handler;
		
		/**
		 * Constructor for DecodedBypass.
		 * @param _bypass String
		 * @param _bbs boolean
		 */
		public DecodedBypass(String _bypass, boolean _bbs)
		{
			bypass = _bypass;
			bbs = _bbs;
		}
		
		/**
		 * Constructor for DecodedBypass.
		 * @param _bypass String
		 * @param _handler ICommunityBoardHandler
		 */
		public DecodedBypass(String _bypass, ICommunityBoardHandler _handler)
		{
			bypass = _bypass;
			handler = _handler;
		}
		
		/**
		 * Method trim.
		 * @return DecodedBypass
		 */
		public DecodedBypass trim()
		{
			bypass = bypass.trim();
			return this;
		}
	}
}
