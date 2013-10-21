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
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class AbstractDirParser<H extends AbstractHolder> extends AbstractParser<H>
{
	/**
	 * Constructor for AbstractDirParser.
	 * @param holder H
	 */
	protected AbstractDirParser(H holder)
	{
		super(holder);
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	public abstract File getXMLDir();
	
	/**
	 * Method isIgnored.
	 * @param f File
	 * @return boolean
	 */
	public abstract boolean isIgnored(File f);
	
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
		File dir = getXMLDir();
		
		if (!dir.exists())
		{
			warn("Dir " + dir.getAbsolutePath() + " not exists");
			return;
		}
		
		File dtd = new File(dir, getDTDFileName());
		if (!dtd.exists())
		{
			info("DTD file: " + dtd.getName() + " not exists.");
			return;
		}
		
		initDTD(dtd);
		
		try
		{
			Collection<File> files = FileUtils.listFiles(dir, FileFilterUtils.suffixFileFilter(".xml"), FileFilterUtils.directoryFileFilter());
			
			for (File f : files)
			{
				if (!f.isHidden())
				{
					if (!isIgnored(f))
					{
						try
						{
							parseDocument(new FileInputStream(f), f.getName());
						}
						catch (Exception e)
						{
							info("Exception: " + e + " in file: " + f.getName(), e);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			warn("Exception: " + e, e);
		}
	}
}
