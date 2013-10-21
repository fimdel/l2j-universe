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
package lineage2.loginserver.crypt;

import java.io.IOException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NewCrypt
{
	/**
	 * Field _crypt.
	 */
	private final BlowfishEngine _crypt;
	/**
	 * Field _decrypt.
	 */
	private final BlowfishEngine _decrypt;
	
	/**
	 * Constructor for NewCrypt.
	 * @param blowfishKey byte[]
	 */
	public NewCrypt(byte[] blowfishKey)
	{
		_crypt = new BlowfishEngine();
		_crypt.init(true, blowfishKey);
		_decrypt = new BlowfishEngine();
		_decrypt.init(false, blowfishKey);
	}
	
	/**
	 * Constructor for NewCrypt.
	 * @param key String
	 */
	public NewCrypt(String key)
	{
		this(key.getBytes());
	}
	
	/**
	 * Method verifyChecksum.
	 * @param raw byte[]
	 * @return boolean
	 */
	public static boolean verifyChecksum(byte[] raw)
	{
		return NewCrypt.verifyChecksum(raw, 0, raw.length);
	}
	
	/**
	 * Method verifyChecksum.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 * @return boolean
	 */
	public static boolean verifyChecksum(byte[] raw, final int offset, final int size)
	{
		if (((size & 3) != 0) || (size <= 4))
		{
			return false;
		}
		long chksum = 0;
		int count = size - 4;
		long check = -1;
		int i;
		for (i = offset; i < count; i += 4)
		{
			check = raw[i] & 0xff;
			check |= (raw[i + 1] << 8) & 0xff00;
			check |= (raw[i + 2] << 0x10) & 0xff0000;
			check |= (raw[i + 3] << 0x18) & 0xff000000;
			chksum ^= check;
		}
		check = raw[i] & 0xff;
		check |= (raw[i + 1] << 8) & 0xff00;
		check |= (raw[i + 2] << 0x10) & 0xff0000;
		check |= (raw[i + 3] << 0x18) & 0xff000000;
		return check == chksum;
	}
	
	/**
	 * Method appendChecksum.
	 * @param raw byte[]
	 */
	public static void appendChecksum(byte[] raw)
	{
		NewCrypt.appendChecksum(raw, 0, raw.length);
	}
	
	/**
	 * Method appendChecksum.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 */
	public static void appendChecksum(byte[] raw, final int offset, final int size)
	{
		long chksum = 0;
		int count = size - 4;
		long ecx;
		int i;
		for (i = offset; i < count; i += 4)
		{
			ecx = raw[i] & 0xff;
			ecx |= (raw[i + 1] << 8) & 0xff00;
			ecx |= (raw[i + 2] << 0x10) & 0xff0000;
			ecx |= (raw[i + 3] << 0x18) & 0xff000000;
			chksum ^= ecx;
		}
		ecx = raw[i] & 0xff;
		ecx |= (raw[i + 1] << 8) & 0xff00;
		ecx |= (raw[i + 2] << 0x10) & 0xff0000;
		ecx |= (raw[i + 3] << 0x18) & 0xff000000;
		raw[i] = (byte) (chksum & 0xff);
		raw[i + 1] = (byte) ((chksum >> 0x08) & 0xff);
		raw[i + 2] = (byte) ((chksum >> 0x10) & 0xff);
		raw[i + 3] = (byte) ((chksum >> 0x18) & 0xff);
	}
	
	/**
	 * Method encXORPass.
	 * @param raw byte[]
	 * @param key int
	 */
	public static void encXORPass(byte[] raw, int key)
	{
		NewCrypt.encXORPass(raw, 0, raw.length, key);
	}
	
	/**
	 * Method encXORPass.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 * @param key int
	 */
	public static void encXORPass(byte[] raw, final int offset, final int size, int key)
	{
		int stop = size - 8;
		int pos = 4 + offset;
		int edx;
		int ecx = key;
		while (pos < stop)
		{
			edx = raw[pos] & 0xFF;
			edx |= (raw[pos + 1] & 0xFF) << 8;
			edx |= (raw[pos + 2] & 0xFF) << 16;
			edx |= (raw[pos + 3] & 0xFF) << 24;
			ecx += edx;
			edx ^= ecx;
			raw[pos++] = (byte) (edx & 0xFF);
			raw[pos++] = (byte) ((edx >> 8) & 0xFF);
			raw[pos++] = (byte) ((edx >> 16) & 0xFF);
			raw[pos++] = (byte) ((edx >> 24) & 0xFF);
		}
		raw[pos++] = (byte) (ecx & 0xFF);
		raw[pos++] = (byte) ((ecx >> 8) & 0xFF);
		raw[pos++] = (byte) ((ecx >> 16) & 0xFF);
		raw[pos] = (byte) ((ecx >> 24) & 0xFF);
	}
	
	/**
	 * Method decrypt.
	 * @param raw byte[]
	 * @return byte[] * @throws IOException
	 */
	public byte[] decrypt(byte[] raw) throws IOException
	{
		byte[] result = new byte[raw.length];
		int count = raw.length / 8;
		for (int i = 0; i < count; i++)
		{
			_decrypt.processBlock(raw, i * 8, result, i * 8);
		}
		return result;
	}
	
	/**
	 * Method decrypt.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 * @throws IOException
	 */
	public void decrypt(byte[] raw, final int offset, final int size) throws IOException
	{
		byte[] result = new byte[size];
		int count = size / 8;
		for (int i = 0; i < count; i++)
		{
			_decrypt.processBlock(raw, offset + (i * 8), result, i * 8);
		}
		System.arraycopy(result, 0, raw, offset, size);
	}
	
	/**
	 * Method crypt.
	 * @param raw byte[]
	 * @return byte[] * @throws IOException
	 */
	public byte[] crypt(byte[] raw) throws IOException
	{
		int count = raw.length / 8;
		byte[] result = new byte[raw.length];
		for (int i = 0; i < count; i++)
		{
			_crypt.processBlock(raw, i * 8, result, i * 8);
		}
		return result;
	}
	
	/**
	 * Method crypt.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 * @throws IOException
	 */
	public void crypt(byte[] raw, final int offset, final int size) throws IOException
	{
		int count = size / 8;
		byte[] result = new byte[size];
		for (int i = 0; i < count; i++)
		{
			_crypt.processBlock(raw, offset + (i * 8), result, i * 8);
		}
		System.arraycopy(result, 0, raw, offset, size);
	}
}
