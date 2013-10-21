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

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.eclipse.jdt.internal.compiler.tool.EclipseFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Compiler
{
	/**
	 * Field _log.
	 */
	static final Logger _log = LoggerFactory.getLogger(Compiler.class);
	/**
	 * Field javac.
	 */
	private static final JavaCompiler javac = new EclipseCompiler();
	/**
	 * Field listener.
	 */
	private final DiagnosticListener<JavaFileObject> listener = new DefaultDiagnosticListener();
	/**
	 * Field fileManager.
	 */
	private final StandardJavaFileManager fileManager = new EclipseFileManager(Locale.getDefault(), Charset.defaultCharset());
	/**
	 * Field memClassLoader.
	 */
	private final MemoryClassLoader memClassLoader = new MemoryClassLoader();
	/**
	 * Field memFileManager.
	 */
	private final MemoryJavaFileManager memFileManager = new MemoryJavaFileManager(fileManager, memClassLoader);
	
	/**
	 * Method compile.
	 * @param files File[]
	 * @return boolean
	 */
	public boolean compile(File... files)
	{
		List<String> options = new ArrayList<>();
		options.add("-1.7");
		options.add("-Xlint:all");
		options.add("-warn:none");
		options.add("-g");
		Writer writer = new StringWriter();
		JavaCompiler.CompilationTask compile = javac.getTask(writer, memFileManager, listener, options, null, fileManager.getJavaFileObjects(files));
		if (compile.call())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method compile.
	 * @param files Collection<File>
	 * @return boolean
	 */
	public boolean compile(Collection<File> files)
	{
		return compile(files.toArray(new File[files.size()]));
	}
	
	/**
	 * Method getClassLoader.
	 * @return MemoryClassLoader
	 */
	public MemoryClassLoader getClassLoader()
	{
		return memClassLoader;
	}
	
	/**
	 * @author Mobius
	 */
	private class DefaultDiagnosticListener implements DiagnosticListener<JavaFileObject>
	{
		/**
		 * Constructor for DefaultDiagnosticListener.
		 */
		public DefaultDiagnosticListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method report.
		 * @param diagnostic Diagnostic<? extends JavaFileObject>
		 * @see javax.tools.DiagnosticListener#report(Diagnostic<? extends JavaFileObject>)
		 */
		@Override
		public void report(Diagnostic<? extends JavaFileObject> diagnostic)
		{
			_log.error(diagnostic.getSource().getName() + (diagnostic.getPosition() == Diagnostic.NOPOS ? "" : ":" + diagnostic.getLineNumber() + "," + diagnostic.getColumnNumber()) + ": " + diagnostic.getMessage(Locale.getDefault()));
		}
	}
}
