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
package lineage2.commons.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LogUtils
{
	/**
	 * Constructor for LogUtils.
	 */
	private LogUtils()
	{
	}
	
	/**
	 * Method dumpStack.
	 * @return String
	 */
	public static String dumpStack()
	{
		return dumpStack(new Throwable());
	}
	
	/**
	 * Method dumpStack.
	 * @param t Throwable
	 * @return String
	 */
	public static String dumpStack(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.flush();
		pw.close();
		return sw.toString();
	}
}
