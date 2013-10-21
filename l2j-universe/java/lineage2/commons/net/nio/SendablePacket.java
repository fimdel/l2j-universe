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
package lineage2.commons.net.nio;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class SendablePacket<T> extends AbstractPacket<T>
{
	/**
	 * Method writeC.
	 * @param data int
	 */
	public void writeC(int data)
	{
		getByteBuffer().put((byte) data);
	}
	
	/**
	 * Method writeF.
	 * @param value double
	 */
	protected void writeF(double value)
	{
		getByteBuffer().putDouble(value);
	}
	
	/**
	 * Method writeH.
	 * @param value int
	 */
	protected void writeH(int value)
	{
		getByteBuffer().putShort((short) value);
	}
	
	/**
	 * Method writeD.
	 * @param value int
	 */
	public void writeD(int value)
	{
		getByteBuffer().putInt(value);
	}
	
	/**
	 * Method writeQ.
	 * @param value long
	 */
	public void writeQ(long value)
	{
		getByteBuffer().putLong(value);
	}
	
	/**
	 * Method writeB.
	 * @param data byte[]
	 */
	protected void writeB(byte[] data)
	{
		getByteBuffer().put(data);
	}
	
	/**
	 * Method writeS.
	 * @param charSequence CharSequence
	 */
	public void writeS(CharSequence charSequence)
	{
		if (charSequence != null)
		{
			int length = charSequence.length();
			for (int i = 0; i < length; i++)
			{
				getByteBuffer().putChar(charSequence.charAt(i));
			}
		}
		getByteBuffer().putChar('\000');
	}
	
	/**
	 * Method write.
	 * @return boolean
	 */
	protected abstract boolean write();
}
