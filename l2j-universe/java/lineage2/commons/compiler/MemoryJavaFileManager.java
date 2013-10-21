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

import java.net.URI;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MemoryJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>
{
	/**
	 * Field cl.
	 */
	private final MemoryClassLoader cl;
	
	/**
	 * Constructor for MemoryJavaFileManager.
	 * @param sjfm StandardJavaFileManager
	 * @param xcl MemoryClassLoader
	 */
	public MemoryJavaFileManager(StandardJavaFileManager sjfm, MemoryClassLoader xcl)
	{
		super(sjfm);
		cl = xcl;
	}
	
	/**
	 * Method getJavaFileForOutput.
	 * @param location Location
	 * @param className String
	 * @param kind Kind
	 * @param sibling FileObject
	 * @return JavaFileObject * @see javax.tools.JavaFileManager#getJavaFileForOutput(Location, String, Kind, FileObject)
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
	{
		MemoryByteCode mbc = new MemoryByteCode(className.replace('/', '.').replace('\\', '.'), URI.create("file:///" + className.replace('.', '/').replace('\\', '/') + kind.extension));
		cl.addClass(mbc);
		return mbc;
	}
	
	/**
	 * Method getClassLoader.
	 * @param location Location
	 * @return ClassLoader * @see javax.tools.JavaFileManager#getClassLoader(Location)
	 */
	@Override
	public ClassLoader getClassLoader(Location location)
	{
		return cl;
	}
}
