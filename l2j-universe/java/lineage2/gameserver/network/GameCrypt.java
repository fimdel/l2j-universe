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
package lineage2.gameserver.network;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameCrypt
{
	/**
	 * Field _outKey. Field _inKey.
	 */
	private final byte[] _inKey = new byte[16], _outKey = new byte[16];
	/**
	 * Field _isEnabled.
	 */
	private boolean _isEnabled = false;
	
	/**
	 * Method setKey.
	 * @param key byte[]
	 */
	public void setKey(byte[] key)
	{
		System.arraycopy(key, 0, _inKey, 0, 16);
		System.arraycopy(key, 0, _outKey, 0, 16);
	}
	
	/**
	 * Method setKey.
	 * @param key byte[]
	 * @param value boolean
	 */
	public void setKey(byte[] key, boolean value)
	{
		setKey(key);
	}
	
	/**
	 * Method decrypt.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 * @return boolean
	 */
	public boolean decrypt(byte[] raw, final int offset, final int size)
	{
		if (!_isEnabled)
		{
			return true;
		}
		int temp = 0;
		for (int i = 0; i < size; i++)
		{
			int temp2 = raw[offset + i] & 0xFF;
			raw[offset + i] = (byte) (temp2 ^ _inKey[i & 15] ^ temp);
			temp = temp2;
		}
		int old = _inKey[8] & 0xff;
		old |= (_inKey[9] << 8) & 0xff00;
		old |= (_inKey[10] << 0x10) & 0xff0000;
		old |= (_inKey[11] << 0x18) & 0xff000000;
		old += size;
		_inKey[8] = (byte) (old & 0xff);
		_inKey[9] = (byte) ((old >> 0x08) & 0xff);
		_inKey[10] = (byte) ((old >> 0x10) & 0xff);
		_inKey[11] = (byte) ((old >> 0x18) & 0xff);
		return true;
	}
	
	/**
	 * Method encrypt.
	 * @param raw byte[]
	 * @param offset int
	 * @param size int
	 */
	public void encrypt(byte[] raw, final int offset, final int size)
	{
		if (!_isEnabled)
		{
			_isEnabled = true;
			return;
		}
		int temp = 0;
		for (int i = 0; i < size; i++)
		{
			int temp2 = raw[offset + i] & 0xFF;
			temp = temp2 ^ _outKey[i & 15] ^ temp;
			raw[offset + i] = (byte) temp;
		}
		int old = _outKey[8] & 0xff;
		old |= (_outKey[9] << 8) & 0xff00;
		old |= (_outKey[10] << 0x10) & 0xff0000;
		old |= (_outKey[11] << 0x18) & 0xff000000;
		old += size;
		_outKey[8] = (byte) (old & 0xff);
		_outKey[9] = (byte) ((old >> 0x08) & 0xff);
		_outKey[10] = (byte) ((old >> 0x10) & 0xff);
		_outKey[11] = (byte) ((old >> 0x18) & 0xff);
	}
}
