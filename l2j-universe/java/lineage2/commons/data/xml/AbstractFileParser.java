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
import java.io.FileInputStream;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class AbstractFileParser<H extends AbstractHolder> extends AbstractParser<H>
{
	/**
	 * Constructor for AbstractFileParser.
	 * @param holder H
	 */
	protected AbstractFileParser(H holder)
	{
		super(holder);
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	public abstract File getXMLFile();
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	public abstract String getDTDFileName();
	
	/**
	 * Method parse.
	 */
	@Override
	protected final void parse()
	{
		File file = getXMLFile();
		
		if (!file.exists())
		{
			warn("file " + file.getAbsolutePath() + " not exists");
			return;
		}
		
		File dtd = new File(file.getParent(), getDTDFileName());
		if (!dtd.exists())
		{
			info("DTD file: " + dtd.getName() + " not exists.");
			return;
		}
		
		initDTD(dtd);
		
		try
		{
			parseDocument(new FileInputStream(file), file.getName());
		}
		catch (Exception e)
		{
			warn("Exception: " + e, e);
		}
	}
}
