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
public class Net
{
	/**
	 * Field address.
	 */
	private final int address;
	/**
	 * Field netmask.
	 */
	private final int netmask;
	
	/**
	 * Constructor for Net.
	 * @param net int
	 * @param mask int
	 */
	public Net(int net, int mask)
	{
		address = net;
		netmask = mask;
	}
	
	/**
	 * Method address.
	 * @return int
	 */
	public int address()
	{
		return address;
	}
	
	/**
	 * Method netmask.
	 * @return int
	 */
	public int netmask()
	{
		return netmask;
	}
	
	/**
	 * Method isInRange.
	 * @param address int
	 * @return boolean
	 */
	public boolean isInRange(int address)
	{
		return ((address & netmask) == this.address);
	}
	
	/**
	 * Method isInRange.
	 * @param address String
	 * @return boolean
	 */
	public boolean isInRange(String address)
	{
		return isInRange(parseAddress(address));
	}
	
	/**
	 * Method valueOf.
	 * @param s String
	 * @return Net
	 */
	public static Net valueOf(String s)
	{
		int address = 0;
		int netmask = 0;
		String[] mask = s.trim().split("\\b\\/\\b");
		if ((mask.length < 1) || (mask.length > 2))
		{
			throw new IllegalArgumentException("For input string: \"" + s + "\"");
		}
		if (mask.length == 1)
		{
			String[] octets = mask[0].split("\\.");
			if ((octets.length < 1) || (octets.length > 4))
			{
				throw new IllegalArgumentException("For input string: \"" + s + "\"");
			}
			int i;
			for (i = 1; i <= octets.length; i++)
			{
				if (!octets[i - 1].equals("*"))
				{
					address |= (Integer.parseInt(octets[i - 1]) << (32 - (i * 8)));
					netmask |= (0xff << (32 - (i * 8)));
				}
			}
		}
		else
		{
			address = parseAddress(mask[0]);
			netmask = parseNetmask(mask[1]);
		}
		return new Net(address, netmask);
	}
	
	/**
	 * Method parseAddress.
	 * @param s String
	 * @return int * @throws IllegalArgumentException
	 */
	public static int parseAddress(String s) throws IllegalArgumentException
	{
		int ip = 0;
		String[] octets = s.split("\\.");
		if (octets.length != 4)
		{
			throw new IllegalArgumentException("For input string: \"" + s + "\"");
		}
		for (int i = 1; i <= octets.length; i++)
		{
			ip |= (Integer.parseInt(octets[i - 1]) << (32 - (i * 8)));
		}
		return ip;
	}
	
	/**
	 * Method parseNetmask.
	 * @param s String
	 * @return int * @throws IllegalArgumentException
	 */
	public static int parseNetmask(String s) throws IllegalArgumentException
	{
		int mask = 0;
		String[] octets = s.split("\\.");
		if (octets.length == 1)
		{
			int bitmask = Integer.parseInt(octets[0]);
			if ((bitmask < 0) || (bitmask > 32))
			{
				throw new IllegalArgumentException("For input string: \"" + s + "\"");
			}
			mask = (0xffffffff << (32 - bitmask));
		}
		else
		{
			for (int i = 1; i <= octets.length; i++)
			{
				mask |= (Integer.parseInt(octets[i - 1]) << (32 - (i * 8)));
			}
		}
		return mask;
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o instanceof Net)
		{
			return ((((Net) o).address() == address) && (((Net) o).netmask() == netmask));
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
		sb.append(address >>> 24).append('.');
		sb.append((address << 8) >>> 24).append('.');
		sb.append((address << 16) >>> 24).append('.');
		sb.append((address << 24) >>> 24);
		sb.append('/');
		sb.append(netmask >>> 24).append('.');
		sb.append((netmask << 8) >>> 24).append('.');
		sb.append((netmask << 16) >>> 24).append('.');
		sb.append((netmask << 24) >>> 24);
		return sb.toString();
	}
}
