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
package lineage2.commons.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StrTable
{
	/**
	 * Field rows.
	 */
	private final Map<Integer, Map<String, String>> rows = new HashMap<>();
	/**
	 * Field columns.
	 */
	private final Map<String, Integer> columns = new LinkedHashMap<>();
	/**
	 * Field titles.
	 */
	private final List<String> titles = new ArrayList<>();
	
	/**
	 * Constructor for StrTable.
	 * @param title String
	 */
	public StrTable(String title)
	{
		if (title != null)
		{
			titles.add(title);
		}
	}
	
	/**
	 * Constructor for StrTable.
	 */
	public StrTable()
	{
		this(null);
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val boolean
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, boolean val)
	{
		return set(rowIndex, colName, Boolean.toString(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val byte
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, byte val)
	{
		return set(rowIndex, colName, Byte.toString(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val char
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, char val)
	{
		return set(rowIndex, colName, String.valueOf(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val short
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, short val)
	{
		return set(rowIndex, colName, Short.toString(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val int
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, int val)
	{
		return set(rowIndex, colName, Integer.toString(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val long
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, long val)
	{
		return set(rowIndex, colName, Long.toString(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val float
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, float val)
	{
		return set(rowIndex, colName, Float.toString(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val double
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, double val)
	{
		return set(rowIndex, colName, Double.toString(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val Object
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, Object val)
	{
		return set(rowIndex, colName, String.valueOf(val));
	}
	
	/**
	 * Method set.
	 * @param rowIndex int
	 * @param colName String
	 * @param val String
	 * @return StrTable
	 */
	public StrTable set(int rowIndex, String colName, String val)
	{
		Map<String, String> row;
		if (rows.containsKey(rowIndex))
		{
			row = rows.get(rowIndex);
		}
		else
		{
			row = new HashMap<>();
			rows.put(rowIndex, row);
		}
		row.put(colName, val);
		int columnSize;
		if (!columns.containsKey(colName))
		{
			columnSize = Math.max(colName.length(), val.length());
		}
		else if (columns.get(colName) >= (columnSize = val.length()))
		{
			return this;
		}
		columns.put(colName, columnSize);
		return this;
	}
	
	/**
	 * Method addTitle.
	 * @param s String
	 * @return StrTable
	 */
	public StrTable addTitle(String s)
	{
		titles.add(s);
		return this;
	}
	
	/**
	 * Method right.
	 * @param result StringBuilder
	 * @param s String
	 * @param sz int
	 * @return StringBuilder
	 */
	private static StringBuilder right(StringBuilder result, String s, int sz)
	{
		result.append(s);
		if ((sz -= s.length()) > 0)
		{
			for (int i = 0; i < sz; i++)
			{
				result.append(' ');
			}
		}
		return result;
	}
	
	/**
	 * Method center.
	 * @param result StringBuilder
	 * @param s String
	 * @param sz int
	 * @return StringBuilder
	 */
	private static StringBuilder center(StringBuilder result, String s, int sz)
	{
		int offset = result.length();
		result.append(s);
		int i;
		while ((i = sz - (result.length() - offset)) > 0)
		{
			result.append(' ');
			if (i > 1)
			{
				result.insert(offset, " ");
			}
		}
		return result;
	}
	
	/**
	 * Method repeat.
	 * @param result StringBuilder
	 * @param s String
	 * @param sz int
	 * @return StringBuilder
	 */
	private static StringBuilder repeat(StringBuilder result, String s, int sz)
	{
		for (int i = 0; i < sz; i++)
		{
			result.append(s);
		}
		return result;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		if (columns.isEmpty())
		{
			return result.toString();
		}
		StringBuilder header = new StringBuilder("|");
		StringBuilder line = new StringBuilder("|");
		for (String c : columns.keySet())
		{
			center(header, c, columns.get(c) + 2).append('|');
			repeat(line, "-", columns.get(c) + 2).append('|');
		}
		if (!titles.isEmpty())
		{
			result.append(' ');
			repeat(result, "-", header.length() - 2).append(' ').append('\n');
			for (String title : titles)
			{
				result.append("| ");
				right(result, title, header.length() - 3).append('|').append('\n');
			}
		}
		result.append(' ');
		repeat(result, "-", header.length() - 2).append(' ').append('\n');
		result.append(header).append('\n');
		result.append(line).append('\n');
		for (Map<String, String> row : rows.values())
		{
			result.append('|');
			for (String c : columns.keySet())
			{
				center(result, row.containsKey(c) ? row.get(c) : "-", columns.get(c) + 2).append('|');
			}
			result.append('\n');
		}
		result.append(' ');
		repeat(result, "-", header.length() - 2).append(' ').append('\n');
		return result.toString();
	}
}
