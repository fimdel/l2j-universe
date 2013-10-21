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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class XMLUtil
{
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(XMLUtil.class);
	
	/**
	 * Method getAttributeValue.
	 * @param n Node
	 * @param item String
	 * @return String
	 */
	public static String getAttributeValue(Node n, String item)
	{
		final Node d = n.getAttributes().getNamedItem(item);
		if (d == null)
		{
			return StringUtils.EMPTY;
		}
		final String val = d.getNodeValue();
		if (val == null)
		{
			return StringUtils.EMPTY;
		}
		return val;
	}
	
	/**
	 * Method getAttributeBooleanValue.
	 * @param n Node
	 * @param item String
	 * @param dflt boolean
	 * @return boolean
	 */
	public static boolean getAttributeBooleanValue(Node n, String item, boolean dflt)
	{
		final Node d = n.getAttributes().getNamedItem(item);
		if (d == null)
		{
			return dflt;
		}
		final String val = d.getNodeValue();
		if (val == null)
		{
			return dflt;
		}
		return Boolean.parseBoolean(val);
	}
	
	/**
	 * Method getAttributeIntValue.
	 * @param n Node
	 * @param item String
	 * @param dflt int
	 * @return int
	 */
	public static int getAttributeIntValue(Node n, String item, int dflt)
	{
		final Node d = n.getAttributes().getNamedItem(item);
		if (d == null)
		{
			return dflt;
		}
		final String val = d.getNodeValue();
		if (val == null)
		{
			return dflt;
		}
		return Integer.parseInt(val);
	}
	
	/**
	 * Method getAttributeLongValue.
	 * @param n Node
	 * @param item String
	 * @param dflt long
	 * @return long
	 */
	public static long getAttributeLongValue(Node n, String item, long dflt)
	{
		final Node d = n.getAttributes().getNamedItem(item);
		if (d == null)
		{
			return dflt;
		}
		final String val = d.getNodeValue();
		if (val == null)
		{
			return dflt;
		}
		return Long.parseLong(val);
	}
}
