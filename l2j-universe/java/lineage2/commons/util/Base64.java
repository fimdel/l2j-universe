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
package lineage2.commons.util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Base64
{
	/**
	 * Field NO_OPTIONS. (value is 0)
	 */
	public final static int NO_OPTIONS = 0;
	/**
	 * Field ENCODE. (value is 1)
	 */
	public final static int ENCODE = 1;
	/**
	 * Field DECODE. (value is 0)
	 */
	public final static int DECODE = 0;
	/**
	 * Field GZIP. (value is 2)
	 */
	public final static int GZIP = 2;
	/**
	 * Field DONT_BREAK_LINES. (value is 8)
	 */
	public final static int DONT_BREAK_LINES = 8;
	/**
	 * Field MAX_LINE_LENGTH. (value is 76)
	 */
	private final static int MAX_LINE_LENGTH = 76;
	/**
	 * Field EQUALS_SIGN.
	 */
	private final static byte EQUALS_SIGN = (byte) '=';
	/**
	 * Field NEW_LINE.
	 */
	private final static byte NEW_LINE = (byte) '\n';
	/**
	 * Field PREFERRED_ENCODING. (value is ""UTF-8"")
	 */
	private final static String PREFERRED_ENCODING = "UTF-8";
	/**
	 * Field ALPHABET.
	 */
	private final static byte[] ALPHABET;
	/**
	 * Field _NATIVE_ALPHABET.
	 */
	private final static byte[] _NATIVE_ALPHABET =
	{
		(byte) 'A',
		(byte) 'B',
		(byte) 'C',
		(byte) 'D',
		(byte) 'E',
		(byte) 'F',
		(byte) 'G',
		(byte) 'H',
		(byte) 'I',
		(byte) 'J',
		(byte) 'K',
		(byte) 'L',
		(byte) 'M',
		(byte) 'N',
		(byte) 'O',
		(byte) 'P',
		(byte) 'Q',
		(byte) 'R',
		(byte) 'S',
		(byte) 'T',
		(byte) 'U',
		(byte) 'V',
		(byte) 'W',
		(byte) 'X',
		(byte) 'Y',
		(byte) 'Z',
		(byte) 'a',
		(byte) 'b',
		(byte) 'c',
		(byte) 'd',
		(byte) 'e',
		(byte) 'f',
		(byte) 'g',
		(byte) 'h',
		(byte) 'i',
		(byte) 'j',
		(byte) 'k',
		(byte) 'l',
		(byte) 'm',
		(byte) 'n',
		(byte) 'o',
		(byte) 'p',
		(byte) 'q',
		(byte) 'r',
		(byte) 's',
		(byte) 't',
		(byte) 'u',
		(byte) 'v',
		(byte) 'w',
		(byte) 'x',
		(byte) 'y',
		(byte) 'z',
		(byte) '0',
		(byte) '1',
		(byte) '2',
		(byte) '3',
		(byte) '4',
		(byte) '5',
		(byte) '6',
		(byte) '7',
		(byte) '8',
		(byte) '9',
		(byte) '+',
		(byte) '/'
	};
	static
	{
		byte[] __bytes;
		try
		{
			__bytes = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/").getBytes(PREFERRED_ENCODING);
		}
		catch (java.io.UnsupportedEncodingException use)
		{
			__bytes = _NATIVE_ALPHABET;
		}
		ALPHABET = __bytes;
	}
	/**
	 * Field DECODABET.
	 */
	final static byte[] DECODABET =
	{
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-5,
		-5,
		-9,
		-9,
		-5,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-5,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		62,
		-9,
		-9,
		-9,
		63,
		52,
		53,
		54,
		55,
		56,
		57,
		58,
		59,
		60,
		61,
		-9,
		-9,
		-9,
		-1,
		-9,
		-9,
		-9,
		0,
		1,
		2,
		3,
		4,
		5,
		6,
		7,
		8,
		9,
		10,
		11,
		12,
		13,
		14,
		15,
		16,
		17,
		18,
		19,
		20,
		21,
		22,
		23,
		24,
		25,
		-9,
		-9,
		-9,
		-9,
		-9,
		-9,
		26,
		27,
		28,
		29,
		30,
		31,
		32,
		33,
		34,
		35,
		36,
		37,
		38,
		39,
		40,
		41,
		42,
		43,
		44,
		45,
		46,
		47,
		48,
		49,
		50,
		51,
		-9,
		-9,
		-9,
		-9
	};
	/**
	 * Field WHITE_SPACE_ENC. (value is -5)
	 */
	private final static byte WHITE_SPACE_ENC = -5;
	/**
	 * Field EQUALS_SIGN_ENC. (value is -1)
	 */
	private final static byte EQUALS_SIGN_ENC = -1;
	
	/**
	 * Constructor for Base64.
	 */
	private Base64()
	{
	}
	
	/**
	 * Method encode3to4.
	 * @param b4 byte[]
	 * @param threeBytes byte[]
	 * @param numSigBytes int
	 * @return byte[]
	 */
	static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes)
	{
		encode3to4(threeBytes, 0, numSigBytes, b4, 0);
		return b4;
	}
	
	/**
	 * Method encode3to4.
	 * @param source byte[]
	 * @param srcOffset int
	 * @param numSigBytes int
	 * @param destination byte[]
	 * @param destOffset int
	 * @return byte[]
	 */
	static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset)
	{
		int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0) | (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0) | (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);
		switch (numSigBytes)
		{
			case 3:
				destination[destOffset] = ALPHABET[(inBuff >>> 18)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
				destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
				destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];
				return destination;
			case 2:
				destination[destOffset] = ALPHABET[(inBuff >>> 18)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
				destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
				destination[destOffset + 3] = EQUALS_SIGN;
				return destination;
			case 1:
				destination[destOffset] = ALPHABET[(inBuff >>> 18)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
				destination[destOffset + 2] = EQUALS_SIGN;
				destination[destOffset + 3] = EQUALS_SIGN;
				return destination;
			default:
				return destination;
		}
	}
	
	/**
	 * Method encodeObject.
	 * @param serializableObject java.io.Serializable
	 * @return String
	 */
	public static String encodeObject(java.io.Serializable serializableObject)
	{
		return encodeObject(serializableObject, NO_OPTIONS);
	}
	
	/**
	 * Method encodeObject.
	 * @param serializableObject java.io.Serializable
	 * @param options int
	 * @return String
	 */
	public static String encodeObject(java.io.Serializable serializableObject, int options)
	{
		java.io.ByteArrayOutputStream baos = null;
		java.io.OutputStream b64os = null;
		java.io.ObjectOutputStream oos = null;
		java.util.zip.GZIPOutputStream gzos = null;
		int gzip = (options & GZIP);
		int dontBreakLines = (options & DONT_BREAK_LINES);
		try
		{
			baos = new java.io.ByteArrayOutputStream();
			b64os = new Base64.OutputStream(baos, ENCODE | dontBreakLines);
			if (gzip == GZIP)
			{
				gzos = new java.util.zip.GZIPOutputStream(b64os);
				oos = new java.io.ObjectOutputStream(gzos);
			}
			else
			{
				oos = new java.io.ObjectOutputStream(b64os);
			}
			oos.writeObject(serializableObject);
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				oos.close();
			}
			catch (Exception e)
			{
			}
			try
			{
				gzos.close();
			}
			catch (Exception e)
			{
			}
			try
			{
				b64os.close();
			}
			catch (Exception e)
			{
			}
			try
			{
				baos.close();
			}
			catch (Exception e)
			{
			}
		}
		try
		{
			return new String(baos.toByteArray(), PREFERRED_ENCODING);
		}
		catch (java.io.UnsupportedEncodingException uue)
		{
			return new String(baos.toByteArray());
		}
	}
	
	/**
	 * Method encodeBytes.
	 * @param source byte[]
	 * @return String
	 */
	public static String encodeBytes(byte[] source)
	{
		return encodeBytes(source, 0, source.length, NO_OPTIONS);
	}
	
	/**
	 * Method encodeBytes.
	 * @param source byte[]
	 * @param options int
	 * @return String
	 */
	public static String encodeBytes(byte[] source, int options)
	{
		return encodeBytes(source, 0, source.length, options);
	}
	
	/**
	 * Method encodeBytes.
	 * @param source byte[]
	 * @param off int
	 * @param len int
	 * @return String
	 */
	public static String encodeBytes(byte[] source, int off, int len)
	{
		return encodeBytes(source, off, len, NO_OPTIONS);
	}
	
	/**
	 * Method encodeBytes.
	 * @param source byte[]
	 * @param off int
	 * @param len int
	 * @param options int
	 * @return String
	 */
	public static String encodeBytes(byte[] source, int off, int len, int options)
	{
		int dontBreakLines = (options & DONT_BREAK_LINES);
		int gzip = (options & GZIP);
		if (gzip == GZIP)
		{
			java.io.ByteArrayOutputStream baos = null;
			java.util.zip.GZIPOutputStream gzos = null;
			Base64.OutputStream b64os = null;
			try
			{
				baos = new java.io.ByteArrayOutputStream();
				b64os = new Base64.OutputStream(baos, ENCODE | dontBreakLines);
				gzos = new java.util.zip.GZIPOutputStream(b64os);
				gzos.write(source, off, len);
				gzos.close();
			}
			catch (java.io.IOException e)
			{
				e.printStackTrace();
				return null;
			}
			finally
			{
				try
				{
					gzos.close();
				}
				catch (Exception e)
				{
				}
				try
				{
					b64os.close();
				}
				catch (Exception e)
				{
				}
				try
				{
					baos.close();
				}
				catch (Exception e)
				{
				}
			}
			try
			{
				return new String(baos.toByteArray(), PREFERRED_ENCODING);
			}
			catch (java.io.UnsupportedEncodingException uue)
			{
				return new String(baos.toByteArray());
			}
		}
		boolean breakLines = dontBreakLines == 0;
		int len43 = (len * 4) / 3;
		byte[] outBuff = new byte[(len43) + ((len % 3) > 0 ? 4 : 0) + (breakLines ? (len43 / MAX_LINE_LENGTH) : 0)];
		int d = 0;
		int e = 0;
		int len2 = len - 2;
		int lineLength = 0;
		for (; d < len2; d += 3, e += 4)
		{
			encode3to4(source, d + off, 3, outBuff, e);
			lineLength += 4;
			if (breakLines && (lineLength == MAX_LINE_LENGTH))
			{
				outBuff[e + 4] = NEW_LINE;
				e++;
				lineLength = 0;
			}
		}
		if (d < len)
		{
			encode3to4(source, d + off, len - d, outBuff, e);
			e += 4;
		}
		try
		{
			return new String(outBuff, 0, e, PREFERRED_ENCODING);
		}
		catch (java.io.UnsupportedEncodingException uue)
		{
			return new String(outBuff, 0, e);
		}
	}
	
	/**
	 * Method decode4to3.
	 * @param source byte[]
	 * @param srcOffset int
	 * @param destination byte[]
	 * @param destOffset int
	 * @return int
	 */
	static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset)
	{
		if (source[srcOffset + 2] == EQUALS_SIGN)
		{
			int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12);
			destination[destOffset] = (byte) (outBuff >>> 16);
			return 1;
		}
		else if (source[srcOffset + 3] == EQUALS_SIGN)
		{
			int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);
			destination[destOffset] = (byte) (outBuff >>> 16);
			destination[destOffset + 1] = (byte) (outBuff >>> 8);
			return 2;
		}
		else
		{
			try
			{
				int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6) | ((DECODABET[source[srcOffset + 3]] & 0xFF));
				destination[destOffset] = (byte) (outBuff >> 16);
				destination[destOffset + 1] = (byte) (outBuff >> 8);
				destination[destOffset + 2] = (byte) (outBuff);
				return 3;
			}
			catch (Exception e)
			{
				return -1;
			}
		}
	}
	
	/**
	 * Method decode.
	 * @param source byte[]
	 * @param off int
	 * @param len int
	 * @return byte[]
	 */
	public static byte[] decode(byte[] source, int off, int len)
	{
		int len34 = (len * 3) / 4;
		byte[] outBuff = new byte[len34];
		int outBuffPosn = 0;
		byte[] b4 = new byte[4];
		int b4Posn = 0;
		int i = 0;
		byte sbiCrop = 0;
		byte sbiDecode = 0;
		for (i = off; i < (off + len); i++)
		{
			sbiCrop = (byte) (source[i] & 0x7f);
			sbiDecode = DECODABET[sbiCrop];
			if (sbiDecode >= WHITE_SPACE_ENC)
			{
				if (sbiDecode >= EQUALS_SIGN_ENC)
				{
					b4[b4Posn++] = sbiCrop;
					if (b4Posn > 3)
					{
						outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
						b4Posn = 0;
						if (sbiCrop == EQUALS_SIGN)
						{
							break;
						}
					}
				}
			}
			else
			{
				return null;
			}
		}
		byte[] out = new byte[outBuffPosn];
		System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
		return out;
	}
	
	/**
	 * Method decode.
	 * @param s String
	 * @return byte[]
	 */
	public static byte[] decode(String s)
	{
		byte[] bytes;
		try
		{
			bytes = s.getBytes(PREFERRED_ENCODING);
		}
		catch (java.io.UnsupportedEncodingException uee)
		{
			bytes = s.getBytes();
		}
		bytes = decode(bytes, 0, bytes.length);
		if (bytes.length >= 2)
		{
			int head = (bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);
			if ((bytes.length >= 4) && (java.util.zip.GZIPInputStream.GZIP_MAGIC == head))
			{
				java.io.ByteArrayInputStream bais = null;
				java.util.zip.GZIPInputStream gzis = null;
				java.io.ByteArrayOutputStream baos = null;
				byte[] buffer = new byte[2048];
				int length = 0;
				try
				{
					baos = new java.io.ByteArrayOutputStream();
					bais = new java.io.ByteArrayInputStream(bytes);
					gzis = new java.util.zip.GZIPInputStream(bais);
					while ((length = gzis.read(buffer)) >= 0)
					{
						baos.write(buffer, 0, length);
					}
					bytes = baos.toByteArray();
				}
				catch (java.io.IOException e)
				{
				}
				finally
				{
					try
					{
						baos.close();
					}
					catch (Exception e)
					{
					}
					try
					{
						gzis.close();
					}
					catch (Exception e)
					{
					}
					try
					{
						bais.close();
					}
					catch (Exception e)
					{
					}
				}
			}
		}
		return bytes;
	}
	
	/**
	 * Method decodeToObject.
	 * @param encodedObject String
	 * @return Object
	 */
	public static Object decodeToObject(String encodedObject)
	{
		byte[] objBytes = decode(encodedObject);
		java.io.ByteArrayInputStream bais = null;
		java.io.ObjectInputStream ois = null;
		Object obj = null;
		try
		{
			bais = new java.io.ByteArrayInputStream(objBytes);
			ois = new java.io.ObjectInputStream(bais);
			obj = ois.readObject();
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
		}
		catch (java.lang.ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bais.close();
			}
			catch (Exception e)
			{
			}
			try
			{
				ois.close();
			}
			catch (Exception e)
			{
			}
		}
		return obj;
	}
	
	/**
	 * @author Mobius
	 */
	public static class InputStream extends java.io.FilterInputStream
	{
		/**
		 * Field encode.
		 */
		private final boolean encode;
		/**
		 * Field position.
		 */
		private int position;
		/**
		 * Field buffer.
		 */
		private final byte[] buffer;
		/**
		 * Field bufferLength.
		 */
		private final int bufferLength;
		/**
		 * Field numSigBytes.
		 */
		private int numSigBytes;
		/**
		 * Field lineLength.
		 */
		private int lineLength;
		/**
		 * Field breakLines.
		 */
		private final boolean breakLines;
		
		/**
		 * Constructor for InputStream.
		 * @param _in java.io.InputStream
		 */
		public InputStream(java.io.InputStream _in)
		{
			this(_in, DECODE);
		}
		
		/**
		 * Constructor for InputStream.
		 * @param _in java.io.InputStream
		 * @param options int
		 */
		public InputStream(java.io.InputStream _in, int options)
		{
			super(_in);
			breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;
			encode = (options & ENCODE) == ENCODE;
			bufferLength = encode ? 4 : 3;
			buffer = new byte[bufferLength];
			position = -1;
			lineLength = 0;
		}
		
		/**
		 * Method read.
		 * @return int * @throws java.io.IOException
		 */
		@Override
		public int read() throws java.io.IOException
		{
			if (position < 0)
			{
				if (encode)
				{
					byte[] b3 = new byte[3];
					int numBinaryBytes = 0;
					for (int i = 0; i < 3; i++)
					{
						try
						{
							int b = in.read();
							if (b >= 0)
							{
								b3[i] = (byte) b;
								numBinaryBytes++;
							}
						}
						catch (java.io.IOException e)
						{
							if (i == 0)
							{
								throw e;
							}
						}
					}
					if (numBinaryBytes > 0)
					{
						encode3to4(b3, 0, numBinaryBytes, buffer, 0);
						position = 0;
						numSigBytes = 4;
					}
					else
					{
						return -1;
					}
				}
				else
				{
					byte[] b4 = new byte[4];
					int i = 0;
					for (i = 0; i < 4; i++)
					{
						int b = 0;
						do
						{
							b = in.read();
						}
						while ((b >= 0) && (DECODABET[b & 0x7f] <= WHITE_SPACE_ENC));
						if (b < 0)
						{
							break;
						}
						b4[i] = (byte) b;
					}
					if (i == 4)
					{
						numSigBytes = decode4to3(b4, 0, buffer, 0);
						position = 0;
					}
					else if (i == 0)
					{
						return -1;
					}
					else
					{
						throw new java.io.IOException("Improperly padded Base64 input.");
					}
				}
			}
			if (position >= 0)
			{
				if (position >= numSigBytes)
				{
					return -1;
				}
				if (encode && breakLines && (lineLength >= MAX_LINE_LENGTH))
				{
					lineLength = 0;
					return '\n';
				}
				lineLength++;
				int b = buffer[position++];
				if (position >= bufferLength)
				{
					position = -1;
				}
				return b & 0xFF;
			}
			throw new java.io.IOException("Error in Base64 code reading stream.");
		}
		
		/**
		 * Method read.
		 * @param dest byte[]
		 * @param off int
		 * @param len int
		 * @return int * @throws java.io.IOException
		 */
		@Override
		public int read(byte[] dest, int off, int len) throws java.io.IOException
		{
			int i;
			int b;
			for (i = 0; i < len; i++)
			{
				b = read();
				if (b >= 0)
				{
					dest[off + i] = (byte) b;
				}
				else if (i == 0)
				{
					return -1;
				}
				else
				{
					break;
				}
			}
			return i;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class OutputStream extends java.io.FilterOutputStream
	{
		/**
		 * Field encode.
		 */
		private final boolean encode;
		/**
		 * Field position.
		 */
		private int position;
		/**
		 * Field buffer.
		 */
		private byte[] buffer;
		/**
		 * Field bufferLength.
		 */
		private final int bufferLength;
		/**
		 * Field lineLength.
		 */
		private int lineLength;
		/**
		 * Field breakLines.
		 */
		private final boolean breakLines;
		/**
		 * Field b4.
		 */
		private final byte[] b4;
		/**
		 * Field suspendEncoding.
		 */
		private boolean suspendEncoding;
		
		/**
		 * Constructor for OutputStream.
		 * @param _out java.io.OutputStream
		 */
		public OutputStream(java.io.OutputStream _out)
		{
			this(_out, ENCODE);
		}
		
		/**
		 * Constructor for OutputStream.
		 * @param _out java.io.OutputStream
		 * @param options int
		 */
		public OutputStream(java.io.OutputStream _out, int options)
		{
			super(_out);
			breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;
			encode = (options & ENCODE) == ENCODE;
			bufferLength = encode ? 3 : 4;
			buffer = new byte[bufferLength];
			position = 0;
			lineLength = 0;
			suspendEncoding = false;
			b4 = new byte[4];
		}
		
		/**
		 * Method write.
		 * @param theByte int
		 * @throws java.io.IOException
		 */
		@Override
		public void write(int theByte) throws java.io.IOException
		{
			if (suspendEncoding)
			{
				super.out.write(theByte);
				return;
			}
			if (encode)
			{
				buffer[position++] = (byte) theByte;
				if (position >= bufferLength)
				{
					out.write(encode3to4(b4, buffer, bufferLength));
					lineLength += 4;
					if (breakLines && (lineLength >= MAX_LINE_LENGTH))
					{
						out.write(NEW_LINE);
						lineLength = 0;
					}
					position = 0;
				}
			}
			else
			{
				if (DECODABET[theByte & 0x7f] > WHITE_SPACE_ENC)
				{
					buffer[position++] = (byte) theByte;
					if (position >= bufferLength)
					{
						int len = Base64.decode4to3(buffer, 0, b4, 0);
						out.write(b4, 0, len);
						position = 0;
					}
				}
				else if (DECODABET[theByte & 0x7f] != WHITE_SPACE_ENC)
				{
					throw new java.io.IOException("Invalid character in Base64 data.");
				}
			}
		}
		
		/**
		 * Method write.
		 * @param theBytes byte[]
		 * @param off int
		 * @param len int
		 * @throws java.io.IOException
		 */
		@Override
		public void write(byte[] theBytes, int off, int len) throws java.io.IOException
		{
			if (suspendEncoding)
			{
				super.out.write(theBytes, off, len);
				return;
			}
			for (int i = 0; i < len; i++)
			{
				write(theBytes[off + i]);
			}
		}
		
		/**
		 * Method flushBase64.
		 * @throws java.io.IOException
		 */
		public void flushBase64() throws java.io.IOException
		{
			if (position > 0)
			{
				if (encode)
				{
					out.write(encode3to4(b4, buffer, position));
					position = 0;
				}
				else
				{
					throw new java.io.IOException("Base64 input not properly padded.");
				}
			}
		}
		
		/**
		 * Method close.
		 * @throws java.io.IOException * @see java.io.Closeable#close()
		 */
		@Override
		public void close() throws java.io.IOException
		{
			flushBase64();
			super.close();
			buffer = null;
			out = null;
		}
		
		/**
		 * Method suspendEncoding.
		 * @throws java.io.IOException
		 */
		public void suspendEncoding() throws java.io.IOException
		{
			flushBase64();
			suspendEncoding = true;
		}
		
		/**
		 * Method resumeEncoding.
		 */
		public void resumeEncoding()
		{
			suspendEncoding = false;
		}
	}
}
