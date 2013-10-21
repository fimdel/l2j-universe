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
package lineage2.gameserver.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Files
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Files.class);
	
	/**
	 * Method writeFile.
	 * @param path String
	 * @param string String
	 */
	public static void writeFile(String path, String string)
	{
		try
		{
			FileUtils.writeStringToFile(new File(path), string, "UTF-8");
		}
		catch (IOException e)
		{
			_log.error("Error while saving file : " + path, e);
		}
	}
	
	/**
	 * Method copyFile.
	 * @param srcFile String
	 * @param destFile String
	 * @return boolean
	 */
	public static boolean copyFile(String srcFile, String destFile)
	{
		try
		{
			FileUtils.copyFile(new File(srcFile), new File(destFile), false);
			return true;
		}
		catch (IOException e)
		{
			_log.error("Error while copying file : " + srcFile + " to " + destFile, e);
		}
		return false;
	}
}
