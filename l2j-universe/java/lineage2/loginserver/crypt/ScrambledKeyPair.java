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

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ScrambledKeyPair
{
	/**
	 * Field _pair.
	 */
	private final KeyPair _pair;
	/**
	 * Field _scrambledModulus.
	 */
	private final byte[] _scrambledModulus;
	
	/**
	 * Constructor for ScrambledKeyPair.
	 * @param pPair KeyPair
	 */
	public ScrambledKeyPair(KeyPair pPair)
	{
		_pair = pPair;
		_scrambledModulus = scrambleModulus(((RSAPublicKey) _pair.getPublic()).getModulus());
	}
	
	/**
	 * Method getKeyPair.
	 * @return KeyPair
	 */
	public KeyPair getKeyPair()
	{
		return _pair;
	}
	
	/**
	 * Method getScrambledModulus.
	 * @return byte[]
	 */
	public byte[] getScrambledModulus()
	{
		return _scrambledModulus;
	}
	
	/**
	 * Method scrambleModulus.
	 * @param modulus BigInteger
	 * @return byte[]
	 */
	private final static byte[] scrambleModulus(BigInteger modulus)
	{
		byte[] scrambledMod = modulus.toByteArray();
		if ((scrambledMod.length == 0x81) && (scrambledMod[0] == 0x00))
		{
			byte[] temp = new byte[0x80];
			System.arraycopy(scrambledMod, 1, temp, 0, 0x80);
			scrambledMod = temp;
		}
		for (int i = 0; i < 4; i++)
		{
			byte temp = scrambledMod[i];
			scrambledMod[i] = scrambledMod[0x4d + i];
			scrambledMod[0x4d + i] = temp;
		}
		for (int i = 0; i < 0x40; i++)
		{
			scrambledMod[i] = (byte) (scrambledMod[i] ^ scrambledMod[0x40 + i]);
		}
		for (int i = 0; i < 4; i++)
		{
			scrambledMod[0x0d + i] = (byte) (scrambledMod[0x0d + i] ^ scrambledMod[0x34 + i]);
		}
		for (int i = 0; i < 0x40; i++)
		{
			scrambledMod[0x40 + i] = (byte) (scrambledMod[0x40 + i] ^ scrambledMod[i]);
		}
		return scrambledMod;
	}
}
