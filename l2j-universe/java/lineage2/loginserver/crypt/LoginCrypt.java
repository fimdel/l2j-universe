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

import lineage2.commons.util.Rnd;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LoginCrypt
{
	/**
	 * Field STATIC_BLOWFISH_KEY.
	 */
	private static final byte[] STATIC_BLOWFISH_KEY =
	{
		(byte) 0x6b,
		(byte) 0x60,
		(byte) 0xcb,
		(byte) 0x5b,
		(byte) 0x82,
		(byte) 0xce,
		(byte) 0x90,
		(byte) 0xb1,
		(byte) 0xcc,
		(byte) 0x2b,
		(byte) 0x6c,
		(byte) 0x55,
		(byte) 0x6c,
		(byte) 0x6c,
		(byte) 0x6c,
		(byte) 0x6c
	};
	/**
	 * Field _staticCrypt.
	 */
	private NewCrypt _staticCrypt;
	/**
	 * Field _crypt.
	 */
	private NewCrypt _crypt;
	/**
	 * Field _static.
	 */
	private boolean _static = true;
	
	/**
	 * Method setKey.
	 * @param key byte[]
	 */
	public void setKey(byte[] key)
	{
		_staticCrypt = new NewCrypt(STATIC_BLOWFISH_KEY);
		_crypt = new NewCrypt(key);
	}
	
	/**
	 * Method decrypt.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 * @return boolean * @throws IOException
	 */
	public boolean decrypt(byte[] raw, final int offset, final int size) throws IOException
	{
		_crypt.decrypt(raw, offset, size);
		return NewCrypt.verifyChecksum(raw, offset, size);
	}
	
	/**
	 * Method encrypt.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 * @return int * @throws IOException
	 */
	public int encrypt(byte[] raw, final int offset, int size) throws IOException
	{
		size += 4;
		if (_static)
		{
			size += 4;
			size += 8 - (size % 8);
			NewCrypt.encXORPass(raw, offset, size, Rnd.nextInt());
			_staticCrypt.crypt(raw, offset, size);
			_static = false;
		}
		else
		{
			size += 8 - (size % 8);
			NewCrypt.appendChecksum(raw, offset, size);
			_crypt.crypt(raw, offset, size);
		}
		return size;
	}
}
