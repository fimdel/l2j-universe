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
package lineage2.commons.data.xml;

import lineage2.commons.logging.LoggerObject;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class AbstractHolder extends LoggerObject
{
	/**
	 * Method log.
	 */
	public void log()
	{
		info(String.format("loaded %d%s(s) count.", size(), formatOut(getClass().getSimpleName().replace("Holder", "")).toLowerCase()));
	}
	
	/**
	 * Method process.
	 */
	protected void process()
	{
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	public abstract int size();
	
	/**
	 * Method clear.
	 */
	public abstract void clear();
	
	/**
	 * Method formatOut.
	 * @param st String
	 * @return String
	 */
	private static String formatOut(String st)
	{
		char[] chars = st.toCharArray();
		StringBuffer buf = new StringBuffer(chars.length);
		for (char ch : chars)
		{
			if (Character.isUpperCase(ch))
			{
				buf.append(' ');
			}
			buf.append(Character.toLowerCase(ch));
		}
		return buf.toString();
	}
}
