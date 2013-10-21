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

import java.util.regex.Pattern;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Strings
{
	/**
	 * Method stripSlashes.
	 * @param s String
	 * @return String
	 */
	public static String stripSlashes(String s)
	{
		if (s == null)
		{
			return "";
		}
		s = s.replace("\\'", "'");
		s = s.replace("\\\\", "\\");
		return s;
	}
	
	/**
	 * Method parseBoolean.
	 * @param x Object
	 * @return Boolean
	 */
	public static Boolean parseBoolean(Object x)
	{
		if (x == null)
		{
			return false;
		}
		if (x instanceof Number)
		{
			return ((Number) x).intValue() > 0;
		}
		if (x instanceof Boolean)
		{
			return (Boolean) x;
		}
		if (x instanceof Double)
		{
			return Math.abs((Double) x) < 0.00001;
		}
		return !String.valueOf(x).isEmpty();
	}
	
	/**
	 * Field tr.
	 */
	private static String[] tr;
	/**
	 * Field trb.
	 */
	private static String[] trb;
	/**
	 * Field trcode.
	 */
	private static String[] trcode;
	
	/**
	 * Method reload.
	 */
	public static void reload()
	{
		// TODO: DROP STRINGS.JAVA!
		/*
		 * try { String[] pairs = FileUtils.readFileToString(new File(Config.DATAPACK_ROOT, "data/translit.txt")).split("\n"); tr = new String[pairs.length * 2]; for (int i = 0; i < pairs.length; i++) { String[] ss = pairs[i].split(" +"); tr[i * 2] = ss[0]; tr[(i * 2) + 1] = ss[1]; } pairs =
		 * FileUtils.readFileToString(new File(Config.DATAPACK_ROOT, "data/translit_back.txt")).split("\n"); trb = new String[pairs.length * 2]; for (int i = 0; i < pairs.length; i++) { String[] ss = pairs[i].split(" +"); trb[i * 2] = ss[0]; trb[(i * 2) + 1] = ss[1]; } pairs =
		 * FileUtils.readFileToString(new File(Config.DATAPACK_ROOT, "data/transcode.txt")).split("\n"); trcode = new String[pairs.length * 2]; for (int i = 0; i < pairs.length; i++) { String[] ss = pairs[i].split(" +"); trcode[i * 2] = ss[0]; trcode[(i * 2) + 1] = ss[1]; } } catch (IOException e) {
		 * _log.error("", e); } _log.info("Loaded " + (tr.length + tr.length + trcode.length) + " translit entries.");
		 */
	}
	
	/**
	 * Method translit.
	 * @param s String
	 * @return String
	 */
	public static String translit(String s)
	{
		for (int i = 0; i < tr.length; i += 2)
		{
			s = s.replace(tr[i], tr[i + 1]);
		}
		return s;
	}
	
	/**
	 * Method fromTranslit.
	 * @param s String
	 * @param type int
	 * @return String
	 */
	public static String fromTranslit(String s, int type)
	{
		if (type == 1)
		{
			for (int i = 0; i < trb.length; i += 2)
			{
				s = s.replace(trb[i], trb[i + 1]);
			}
		}
		else if (type == 2)
		{
			for (int i = 0; i < trcode.length; i += 2)
			{
				s = s.replace(trcode[i], trcode[i + 1]);
			}
		}
		return s;
	}
	
	/**
	 * Method replace.
	 * @param str String
	 * @param regex String
	 * @param flags int
	 * @param replace String
	 * @return String
	 */
	public static String replace(String str, String regex, int flags, String replace)
	{
		return Pattern.compile(regex, flags).matcher(str).replaceAll(replace);
	}
	
	/**
	 * Method matches.
	 * @param str String
	 * @param regex String
	 * @param flags int
	 * @return boolean
	 */
	public static boolean matches(String str, String regex, int flags)
	{
		return Pattern.compile(regex, flags).matcher(str).matches();
	}
	
	/**
	 * Method bbParse.
	 * @param s String
	 * @return String
	 */
	public static String bbParse(String s)
	{
		if (s == null)
		{
			return null;
		}
		s = s.replace("\r", "");
		s = s.replaceAll("(\\s|\"|\'|\\(|^|\n)\\*(.*?)\\*(\\s|\"|\'|\\)|\\?|\\.|!|:|;|,|$|\n)", "$1<font color=\"LEVEL\">$2</font>$3");
		s = s.replaceAll("(\\s|\"|\'|\\(|^|\n)\\$(.*?)\\$(\\s|\"|\'|\\)|\\?|\\.|!|:|;|,|$|\n)", "$1<font color=\"00FFFF\">$2</font>$3");
		s = replace(s, "^!(.*?)$", Pattern.MULTILINE, "<font color=\"FFFFFF\">$1</font>\n\n");
		s = s.replaceAll("%%\\s*\n", "<br1>");
		s = s.replaceAll("\n\n+", "<br>");
		s = replace(s, "\\[([^\\]\\|]*?)\\|([^\\]]*?)\\]", Pattern.DOTALL, "<a action=\"bypass -h $1\">$2</a>");
		s = s.replaceAll(" @", "\" msg=\"");
		return s;
	}
	
	/**
	 * Method joinStrings.
	 * @param glueStr String
	 * @param strings String[]
	 * @param startIdx int
	 * @param maxCount int
	 * @return String
	 */
	public static String joinStrings(String glueStr, String[] strings, int startIdx, int maxCount)
	{
		String result = "";
		if (startIdx < 0)
		{
			startIdx += strings.length;
			if (startIdx < 0)
			{
				return result;
			}
		}
		while ((startIdx < strings.length) && (maxCount != 0))
		{
			if (!result.isEmpty() && (glueStr != null) && !glueStr.isEmpty())
			{
				result += glueStr;
			}
			result += strings[startIdx++];
			maxCount--;
		}
		return result;
	}
	
	/**
	 * Method joinStrings.
	 * @param glueStr String
	 * @param strings String[]
	 * @param startIdx int
	 * @return String
	 */
	public static String joinStrings(String glueStr, String[] strings, int startIdx)
	{
		return joinStrings(glueStr, strings, startIdx, -1);
	}
	
	/**
	 * Method joinStrings.
	 * @param glueStr String
	 * @param strings String[]
	 * @return String
	 */
	public static String joinStrings(String glueStr, String[] strings)
	{
		return joinStrings(glueStr, strings, 0);
	}
	
	/**
	 * Method stripToSingleLine.
	 * @param s String
	 * @return String
	 */
	public static String stripToSingleLine(String s)
	{
		if (s.isEmpty())
		{
			return s;
		}
		s = s.replaceAll("\\\\n", "\n");
		int i = s.indexOf("\n");
		if (i > -1)
		{
			s = s.substring(0, i);
		}
		return s;
	}
}
