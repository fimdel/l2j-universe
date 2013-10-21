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
package lineage2.commons.data.xml.helpers;

import lineage2.commons.data.xml.AbstractParser;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ErrorHandlerImpl implements ErrorHandler
{
	/**
	 * Field _parser.
	 */
	private final AbstractParser<?> _parser;
	
	/**
	 * Constructor for ErrorHandlerImpl.
	 * @param parser AbstractParser<?>
	 */
	public ErrorHandlerImpl(AbstractParser<?> parser)
	{
		_parser = parser;
	}
	
	/**
	 * Method warning.
	 * @param exception SAXParseException
	 * @see org.xml.sax.ErrorHandler#warning(SAXParseException)
	 */
	@Override
	public void warning(SAXParseException exception)
	{
		_parser.warn("File: " + _parser.getCurrentFileName() + ":" + exception.getLineNumber() + " warning: " + exception.getMessage());
	}
	
	/**
	 * Method error.
	 * @param exception SAXParseException
	 * @see org.xml.sax.ErrorHandler#error(SAXParseException)
	 */
	@Override
	public void error(SAXParseException exception)
	{
		_parser.error("File: " + _parser.getCurrentFileName() + ":" + exception.getLineNumber() + " error: " + exception.getMessage());
	}
	
	/**
	 * Method fatalError.
	 * @param exception SAXParseException
	 * @see org.xml.sax.ErrorHandler#fatalError(SAXParseException)
	 */
	@Override
	public void fatalError(SAXParseException exception)
	{
		_parser.error("File: " + _parser.getCurrentFileName() + ":" + exception.getLineNumber() + " fatal: " + exception.getMessage());
	}
}
