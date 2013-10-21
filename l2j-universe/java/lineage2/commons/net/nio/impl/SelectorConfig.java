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
package lineage2.commons.net.nio.impl;

import java.nio.ByteOrder;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SelectorConfig
{
	/**
	 * Field READ_BUFFER_SIZE.
	 */
	public int READ_BUFFER_SIZE = 65536;
	/**
	 * Field WRITE_BUFFER_SIZE.
	 */
	public int WRITE_BUFFER_SIZE = 131072;
	/**
	 * Field MAX_SEND_PER_PASS.
	 */
	public int MAX_SEND_PER_PASS = 32;
	/**
	 * Field SLEEP_TIME.
	 */
	public long SLEEP_TIME = 10;
	/**
	 * Field INTEREST_DELAY.
	 */
	public long INTEREST_DELAY = 30;
	/**
	 * Field HEADER_SIZE.
	 */
	public int HEADER_SIZE = 2;
	/**
	 * Field PACKET_SIZE.
	 */
	public int PACKET_SIZE = 32768;
	/**
	 * Field HELPER_BUFFER_COUNT.
	 */
	public int HELPER_BUFFER_COUNT = 64;
	/**
	 * Field BYTE_ORDER.
	 */
	public ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
}
