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
package services;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleToCatacomb extends Functions
{
	/**
	 * Method DialogAppend_31212.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31212(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31213.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31213(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31214.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31214(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31215.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31215(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31216.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31216(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31217.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31217(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31218.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31218(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31219.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31219(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31220.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31220(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31221.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31221(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31222.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31222(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31223.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31223(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31224.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31224(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31767.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31767(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31768.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31768(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_32048.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_32048(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method getHtmlAppends.
	 * @param val Integer
	 * @return String
	 */
	public String getHtmlAppends(Integer val)
	{
		if ((val != 0) || !Config.ALT_TELE_TO_CATACOMBS)
		{
			return "";
		}
		Player player = getSelf();
		String append = "";
		append += "<br>";
		append += "Teleport to catacomb or necropolis.<br1> ";
		append += "You may teleport to any of the following hunting locations.<br>";
		if (player.getLevel() <= Config.GATEKEEPER_FREE)
		{
			append += "[scripts_Util:Gatekeeper -41567 209463 -5080 0|Necropolis of Sacrifice (20-30)]<br1>";
			append += "[scripts_Util:Gatekeeper 45248 124223 -5408 0|The Pilgrim's Necropolis (30-40)]<br1>";
			append += "[scripts_Util:Gatekeeper 110911 174013 -5439 0|Necropolis of Worship (40-50)]<br1>";
			append += "[scripts_Util:Gatekeeper -22101 77383 -5173 0|The Patriot's Necropolis (50-60)]<br1>";
			append += "[scripts_Util:Gatekeeper -52654 79149 -4741 0|Necropolis of Devotion (60-70)]<br1>";
			append += "[scripts_Util:Gatekeeper 117884 132796 -4831 0|Necropolis of Martyrdom (60-70)]<br1>";
			append += "[scripts_Util:Gatekeeper 82750 209250 -5401 0|The Saint's Necropolis (70-80)]<br1>";
			append += "[scripts_Util:Gatekeeper 171897 -17606 -4901 0|Disciples Necropolis(70-80)]<br>";
			append += "[scripts_Util:Gatekeeper 42322 143927 -5381 0|Catacomb of the Heretic (30-40)]<br1>";
			append += "[scripts_Util:Gatekeeper 45841 170307 -4981 0|Catacomb of the Branded (40-50)]<br1>";
			append += "[scripts_Util:Gatekeeper 77348 78445 -5125 0|Catacomb of the Apostate (50-60)]<br1>";
			append += "[scripts_Util:Gatekeeper 139955 79693 -5429 0|Catacomb of the Witch (60-70)]<br1>";
			append += "[scripts_Util:Gatekeeper -19827 13509 -4901 0|Catacomb of Dark Omens (70-80)]<br1>";
			append += "[scripts_Util:Gatekeeper 113573 84513 -6541 0|Catacomb of the Forbidden Path (70-80)]";
		}
		else
		{
			append += "[scripts_Util:Gatekeeper -41567 209463 -5080 10000|Necropolis of Sacrifice (20-30) - 10000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 45248 124223 -5408 20000|The Pilgrim's Necropolis (30-40) - 20000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 110911 174013 -5439 30000|Necropolis of Worship (40-50) - 30000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper -22101 77383 -5173 40000|The Patriot's Necropolis (50-60) - 40000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper -52654 79149 -4741 50000|Necropolis of Devotion (60-70) - 50000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 117884 132796 -4831 50000|Necropolis of Martyrdom (60-70) - 50000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 82750 209250 -5401 60000|The Saint's Necropolis (70-80) - 60000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 171897 -17606 -4901 60000|Disciples Necropolis(70-80) - 60000 Adena]<br>";
			append += "[scripts_Util:Gatekeeper 42322 143927 -5381 20000|Catacomb of the Heretic (30-40) - 20000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 45841 170307 -4981 30000|Catacomb of the Branded (40-50) - 30000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 77348 78445 -5125 40000|Catacomb of the Apostate (50-60) - 40000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 139955 79693 -5429 50000|Catacomb of the Witch (60-70) - 50000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper -19827 13509 -4901 60000|Catacomb of Dark Omens (70-80) - 60000 Adena]<br1>";
			append += "[scripts_Util:Gatekeeper 113573 84513 -6541 60000|Catacomb of the Forbidden Path (70-80) - 60000 Adena]";
		}
		return append;
	}
}
