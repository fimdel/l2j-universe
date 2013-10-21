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
package lineage2.commons.data.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastList;
import lineage2.commons.util.fileio.XMLFilter;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class DocumentParser
{
	/**
	 * Field _log.
	 */
	private final static Logger _log = Logger.getLogger(DocumentParser.class);
	/**
	 * Field xmlFilter.
	 */
	private static final XMLFilter xmlFilter = new XMLFilter();
	/**
	 * Field _currentFile.
	 */
	private File _currentFile;
	/**
	 * Field _currentDocument.
	 */
	private Document _currentDocument;
	
	/**
	 * Method load.
	 */
	public abstract void load();
	
	/**
	 * Method parseFile.
	 * @param f File
	 */
	protected void parseFile(File f)
	{
		if (!xmlFilter.accept(f))
		{
			_log.warn("Could not parse " + f.getName() + " is not a file or it doesn't exist!");
			return;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(true);
		dbf.setIgnoringComments(true);
		_currentDocument = null;
		_currentFile = f;
		try
		{
			dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new XMLErrorHandler());
			_currentDocument = db.parse(f);
		}
		catch (Exception e)
		{
			_log.warn("Could not parse " + f.getName() + " file: " + e.getMessage());
			return;
		}
		parseDocument();
	}
	
	/**
	 * Method getCurrentFile.
	 * @return File
	 */
	public File getCurrentFile()
	{
		return _currentFile;
	}
	
	/**
	 * Method getCurrentDocument.
	 * @return Document
	 */
	protected Document getCurrentDocument()
	{
		return _currentDocument;
	}
	
	/**
	 * Method parseDirectory.
	 * @param path String
	 * @return boolean
	 */
	protected boolean parseDirectory(String path)
	{
		return parseDirectory(new File(path));
	}
	
	/**
	 * Method parseDirectory.
	 * @param dir File
	 * @return boolean
	 */
	protected boolean parseDirectory(File dir)
	{
		if (!dir.exists())
		{
			_log.warn("Folder " + dir.getAbsolutePath() + " doesn't exist!");
			return false;
		}
		FastList<File> listOfFiles = getAllFileList(dir, "xml");
		for (File f : listOfFiles)
		{
			parseFile(f);
		}
		return true;
	}
	
	/**
	 * Method parseDocument.
	 * @param doc Document
	 */
	protected void parseDocument(Document doc)
	{
	}
	
	/**
	 * Method parseDocument.
	 */
	protected abstract void parseDocument();
	
	/**
	 * Method parseInt.
	 * @param n NamedNodeMap
	 * @param name String
	 * @return int
	 */
	protected static int parseInt(NamedNodeMap n, String name)
	{
		return Integer.parseInt(n.getNamedItem(name).getNodeValue());
	}
	
	/**
	 * Method parseInteger.
	 * @param n NamedNodeMap
	 * @param name String
	 * @return Integer
	 */
	protected static Integer parseInteger(NamedNodeMap n, String name)
	{
		return Integer.valueOf(n.getNamedItem(name).getNodeValue());
	}
	
	/**
	 * Method parseInt.
	 * @param n Node
	 * @return int
	 */
	protected static int parseInt(Node n)
	{
		return Integer.parseInt(n.getNodeValue());
	}
	
	/**
	 * Method parseInteger.
	 * @param n Node
	 * @return Integer
	 */
	protected static Integer parseInteger(Node n)
	{
		return Integer.valueOf(n.getNodeValue());
	}
	
	/**
	 * Method parseLong.
	 * @param n NamedNodeMap
	 * @param name String
	 * @return Long
	 */
	protected static Long parseLong(NamedNodeMap n, String name)
	{
		return Long.valueOf(n.getNamedItem(name).getNodeValue());
	}
	
	/**
	 * Method parseLong.
	 * @param n Node
	 * @return Long
	 */
	protected static Long parseLong(Node n)
	{
		return Long.valueOf(n.getNodeValue());
	}
	
	/**
	 * Method parseBoolean.
	 * @param n NamedNodeMap
	 * @param name String
	 * @return boolean
	 */
	protected static boolean parseBoolean(NamedNodeMap n, String name)
	{
		Node b = n.getNamedItem(name);
		return (b != null) && (Boolean.parseBoolean(b.getNodeValue()));
	}
	
	/**
	 * Method parseBoolean.
	 * @param n Node
	 * @return boolean
	 */
	protected static boolean parseBoolean(Node n)
	{
		return Boolean.parseBoolean(n.getNodeValue());
	}
	
	/**
	 * Method parseByte.
	 * @param n Node
	 * @return byte
	 */
	protected static byte parseByte(Node n)
	{
		return Byte.valueOf(n.getNodeValue()).byteValue();
	}
	
	/**
	 * Method parseDouble.
	 * @param n NamedNodeMap
	 * @param name String
	 * @return double
	 */
	protected static double parseDouble(NamedNodeMap n, String name)
	{
		return Double.valueOf(n.getNamedItem(name).getNodeValue()).doubleValue();
	}
	
	/**
	 * Method parseFloat.
	 * @param n NamedNodeMap
	 * @param name String
	 * @return float
	 */
	protected static float parseFloat(NamedNodeMap n, String name)
	{
		return Float.valueOf(n.getNamedItem(name).getNodeValue()).floatValue();
	}
	
	/**
	 * Method parseFloat.
	 * @param n Node
	 * @return float
	 */
	protected static float parseFloat(Node n)
	{
		return Float.valueOf(n.getNodeValue()).floatValue();
	}
	
	/**
	 * @author Mobius
	 */
	protected class XMLErrorHandler implements ErrorHandler
	{
		/**
		 * Constructor for XMLErrorHandler.
		 */
		protected XMLErrorHandler()
		{
		}
		
		/**
		 * Method warning.
		 * @param e SAXParseException
		 * @throws SAXParseException * @see org.xml.sax.ErrorHandler#warning(SAXParseException)
		 */
		@Override
		public void warning(SAXParseException e) throws SAXParseException
		{
			throw e;
		}
		
		/**
		 * Method error.
		 * @param e SAXParseException
		 * @throws SAXParseException * @see org.xml.sax.ErrorHandler#error(SAXParseException)
		 */
		@Override
		public void error(SAXParseException e) throws SAXParseException
		{
			throw e;
		}
		
		/**
		 * Method fatalError.
		 * @param e SAXParseException
		 * @throws SAXParseException * @see org.xml.sax.ErrorHandler#fatalError(SAXParseException)
		 */
		@Override
		public void fatalError(SAXParseException e) throws SAXParseException
		{
			throw e;
		}
	}
	
	/**
	 * Method getAllFileList.
	 * @param dir File
	 * @param pathName String
	 * @return FastList<File>
	 */
	public static FastList<File> getAllFileList(File dir, String pathName)
	{
		FastList<File> list = new FastList<>();
		if ((!dir.toString().endsWith("/")) && (!dir.toString().endsWith("\\")))
		{
			dir = new File(new StringBuilder().append(dir.toString()).append('/').toString());
		}
		if (!dir.exists())
		{
			_log.warn(new StringBuilder().append(" Folder ").append(dir.getAbsolutePath()).append(" doesn't exist!").toString());
		}
		for (File file : dir.listFiles())
		{
			if (file.isDirectory())
			{
				for (File fileName : getAllFileList(file, pathName))
				{
					if (fileName.toString().endsWith(pathName))
					{
						list.add(fileName);
					}
				}
			}
			else
			{
				if (!file.toString().endsWith(pathName))
				{
					continue;
				}
				list.add(file);
			}
		}
		return list;
	}
}
