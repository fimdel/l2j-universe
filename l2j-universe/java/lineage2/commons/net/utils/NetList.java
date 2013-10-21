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
package lineage2.commons.net.utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class NetList extends ArrayList<Net>
{
	/**
	 * Field serialVersionUID. (value is 4266033257195615387)
	 */
	private static final long serialVersionUID = 4266033257195615387L;
	
	/**
	 * Method isInRange.
	 * @param address String
	 * @return boolean
	 */
	public boolean isInRange(String address)
	{
		for (Net net : this)
		{
			if (net.isInRange(address))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Iterator<Net> itr = iterator(); itr.hasNext();)
		{
			sb.append(itr.next());
			if (itr.hasNext())
			{
				sb.append(',');
			}
		}
		return sb.toString();
	}
}
