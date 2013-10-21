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
package lineage2.commons.compiler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MemoryByteCode extends SimpleJavaFileObject
{
	/**
	 * Field oStream.
	 */
	private ByteArrayOutputStream oStream;
	/**
	 * Field className.
	 */
	private final String className;
	
	/**
	 * Constructor for MemoryByteCode.
	 * @param className String
	 * @param uri URI
	 */
	public MemoryByteCode(String className, URI uri)
	{
		super(uri, Kind.CLASS);
		this.className = className;
	}
	
	/**
	 * Method openOutputStream.
	 * @return OutputStream * @see javax.tools.FileObject#openOutputStream()
	 */
	@Override
	public OutputStream openOutputStream()
	{
		oStream = new ByteArrayOutputStream();
		return oStream;
	}
	
	/**
	 * Method getBytes.
	 * @return byte[]
	 */
	public byte[] getBytes()
	{
		return oStream.toByteArray();
	}
	
	/**
	 * Method getName.
	 * @return String * @see javax.tools.FileObject#getName()
	 */
	@Override
	public String getName()
	{
		return className;
	}
}
