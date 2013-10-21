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
package lineage2.gameserver.utils;

import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SysString;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HtmlUtils
{
	/**
	 * Field PREV_BUTTON. (value is ""<button value=\"&$1037;\" action=\"bypass %prev_bypass%\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"")
	 */
	public static final String PREV_BUTTON = "<button value=\"&$1037;\" action=\"bypass %prev_bypass%\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	/**
	 * Field NEXT_BUTTON. (value is ""<button value=\"&$1038;\" action=\"bypass %next_bypass%\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"")
	 */
	public static final String NEXT_BUTTON = "<button value=\"&$1038;\" action=\"bypass %next_bypass%\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	
	/**
	 * Method htmlResidenceName.
	 * @param id int
	 * @return String
	 */
	public static String htmlResidenceName(int id)
	{
		return "&%" + id + ";";
	}
	
	/**
	 * Method htmlNpcName.
	 * @param npcId int
	 * @return String
	 */
	public static String htmlNpcName(int npcId)
	{
		return "&@" + npcId + ";";
	}
	
	/**
	 * Method htmlSysString.
	 * @param sysString SysString
	 * @return String
	 */
	public static String htmlSysString(SysString sysString)
	{
		return htmlSysString(sysString.getId());
	}
	
	/**
	 * Method htmlSysString.
	 * @param id int
	 * @return String
	 */
	public static String htmlSysString(int id)
	{
		return "&$" + id + ";";
	}
	
	/**
	 * Method htmlItemName.
	 * @param itemId int
	 * @return String
	 */
	public static String htmlItemName(int itemId)
	{
		return "&#" + itemId + ";";
	}
	
	/**
	 * Method htmlClassName.
	 * @param classId int
	 * @return String
	 */
	public static String htmlClassName(int classId)
	{
		return "<ClassId>" + classId + "</ClassId>";
	}
	
	/**
	 * Method htmlNpcString.
	 * @param id NpcString
	 * @param params Object[]
	 * @return String
	 */
	public static String htmlNpcString(NpcString id, Object... params)
	{
		return htmlNpcString(id.getId(), params);
	}
	
	/**
	 * Method htmlNpcString.
	 * @param id int
	 * @param params Object[]
	 * @return String
	 */
	public static String htmlNpcString(int id, Object... params)
	{
		String replace = "<fstring";
		if (params.length > 0)
		{
			for (int i = 0; i < params.length; i++)
			{
				replace += " p" + (i + 1) + "=\"" + String.valueOf(params[i]) + "\"";
			}
		}
		replace += ">" + id + "</fstring>";
		return replace;
	}
	
	/**
	 * Method htmlButton.
	 * @param value String
	 * @param action String
	 * @param width int
	 * @return String
	 */
	public static String htmlButton(String value, String action, int width)
	{
		return htmlButton(value, action, width, 22);
	}
	
	/**
	 * Method htmlButton.
	 * @param value String
	 * @param action String
	 * @param width int
	 * @param height int
	 * @return String
	 */
	public static String htmlButton(String value, String action, int width, int height)
	{
		return String.format("<button value=\"%s\" action=\"%s\" back=\"L2UI_CT1.Button_DF_Small_Down\" width=%d height=%d fore=\"L2UI_CT1.Button_DF_Small\">", value, action, width, height);
	}
}
