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
package lineage2.commons.versioning;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Locator
{
	/**
	 * Constructor for Locator.
	 */
	private Locator()
	{
	}
	
	/**
	 * Method getClassSource.
	 * @param c Class<?>
	 * @return File
	 */
	public static File getClassSource(Class<?> c)
	{
		String classResource = c.getName().replace('.', '/') + ".class";
		return getResourceSource(c.getClassLoader(), classResource);
	}
	
	/**
	 * Method getResourceSource.
	 * @param c ClassLoader
	 * @param resource String
	 * @return File
	 */
	public static File getResourceSource(ClassLoader c, String resource)
	{
		if (c == null)
		{
			c = Locator.class.getClassLoader();
		}
		URL url = null;
		if (c == null)
		{
			url = ClassLoader.getSystemResource(resource);
		}
		else
		{
			url = c.getResource(resource);
		}
		if (url != null)
		{
			String u = url.toString();
			if (u.startsWith("jar:file:"))
			{
				int pling = u.indexOf("!");
				String jarName = u.substring(4, pling);
				return new File(fromURI(jarName));
			}
			else if (u.startsWith("file:"))
			{
				int tail = u.indexOf(resource);
				String dirName = u.substring(0, tail);
				return new File(fromURI(dirName));
			}
		}
		return null;
	}
	
	/**
	 * Method fromURI.
	 * @param uri String
	 * @return String
	 */
	public static String fromURI(String uri)
	{
		URL url = null;
		try
		{
			url = new URL(uri);
		}
		catch (MalformedURLException emYouEarlEx)
		{
		}
		if ((url == null) || !"file".equals(url.getProtocol()))
		{
			throw new IllegalArgumentException("Can only handle valid file: URIs");
		}
		StringBuilder buf = new StringBuilder(url.getHost());
		if (buf.length() > 0)
		{
			buf.insert(0, File.separatorChar).insert(0, File.separatorChar);
		}
		String file = url.getFile();
		int queryPos = file.indexOf('?');
		buf.append(queryPos < 0 ? file : file.substring(0, queryPos));
		uri = buf.toString().replace('/', File.separatorChar);
		if ((File.pathSeparatorChar == ';') && uri.startsWith("\\") && (uri.length() > 2) && Character.isLetter(uri.charAt(1)) && (uri.lastIndexOf(':') > -1))
		{
			uri = uri.substring(1);
		}
		String path = decodeUri(uri);
		return path;
	}
	
	/**
	 * Method decodeUri.
	 * @param uri String
	 * @return String
	 */
	private static String decodeUri(String uri)
	{
		if (uri.indexOf('%') == -1)
		{
			return uri;
		}
		StringBuilder sb = new StringBuilder();
		CharacterIterator iter = new StringCharacterIterator(uri);
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next())
		{
			if (c == '%')
			{
				char c1 = iter.next();
				if (c1 != CharacterIterator.DONE)
				{
					int i1 = Character.digit(c1, 16);
					char c2 = iter.next();
					if (c2 != CharacterIterator.DONE)
					{
						int i2 = Character.digit(c2, 16);
						sb.append((char) ((i1 << 4) + i2));
					}
				}
			}
			else
			{
				sb.append(c);
			}
		}
		String path = sb.toString();
		return path;
	}
	
	/**
	 * Method getToolsJar.
	 * @return File
	 */
	public static File getToolsJar()
	{
		boolean toolsJarAvailable = false;
		try
		{
			Class.forName("com.sun.tools.javac.Main");
			toolsJarAvailable = true;
		}
		catch (Exception e)
		{
			try
			{
				Class.forName("sun.tools.javac.Main");
				toolsJarAvailable = true;
			}
			catch (Exception e2)
			{
			}
		}
		if (toolsJarAvailable)
		{
			return null;
		}
		String javaHome = System.getProperty("java.home");
		if (javaHome.toLowerCase(Locale.US).endsWith("jre"))
		{
			javaHome = javaHome.substring(0, javaHome.length() - 4);
		}
		File toolsJar = new File(javaHome + "/lib/tools.jar");
		if (!toolsJar.exists())
		{
			System.out.println("Unable to locate tools.jar. " + "Expected to find it in " + toolsJar.getPath());
			return null;
		}
		return toolsJar;
	}
	
	/**
	 * Method getLocationURLs.
	 * @param location File
	 * @return URL[] * @throws MalformedURLException
	 */
	public static URL[] getLocationURLs(File location) throws MalformedURLException
	{
		return getLocationURLs(location, new String[]
		{
			".jar"
		});
	}
	
	/**
	 * Method getLocationURLs.
	 * @param location File
	 * @param extensions String[]
	 * @return URL[] * @throws MalformedURLException
	 */
	public static URL[] getLocationURLs(File location, final String[] extensions) throws MalformedURLException
	{
		URL[] urls = new URL[0];
		if (!location.exists())
		{
			return urls;
		}
		if (!location.isDirectory())
		{
			urls = new URL[1];
			String path = location.getPath();
			for (String extension : extensions)
			{
				if (path.toLowerCase().endsWith(extension))
				{
					urls[0] = location.toURI().toURL();
					break;
				}
			}
			return urls;
		}
		File[] matches = location.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				for (String extension : extensions)
				{
					if (name.toLowerCase().endsWith(extension))
					{
						return true;
					}
				}
				return false;
			}
		});
		urls = new URL[matches.length];
		for (int i = 0; i < matches.length; ++i)
		{
			urls[i] = matches[i].toURI().toURL();
		}
		return urls;
	}
}
