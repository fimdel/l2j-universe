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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NetUtils
{
	/**
	 * Field PRIVATE.
	 */
	private final static NetList PRIVATE = new NetList();
	static
	{
		PRIVATE.add(Net.valueOf("127.0.0.0/8"));
		PRIVATE.add(Net.valueOf("10.0.0.0/8"));
		PRIVATE.add(Net.valueOf("172.16.0.0/12"));
		PRIVATE.add(Net.valueOf("192.168.0.0/16"));
		PRIVATE.add(Net.valueOf("169.254.0.0/16"));
	}
	
	/**
	 * Method isInternalIP.
	 * @param address String
	 * @return boolean
	 */
	public final static boolean isInternalIP(String address)
	{
		return PRIVATE.isInRange(address);
	}
}
