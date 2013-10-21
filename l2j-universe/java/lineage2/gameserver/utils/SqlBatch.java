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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SqlBatch
{
	/**
	 * Field _header.
	 */
	private final String _header;
	/**
	 * Field _tail.
	 */
	private final String _tail;
	/**
	 * Field _sb.
	 */
	private StringBuilder _sb;
	/**
	 * Field _result.
	 */
	private final StringBuilder _result;
	/**
	 * Field _limit.
	 */
	private long _limit = Long.MAX_VALUE;
	/**
	 * Field isEmpty.
	 */
	private boolean isEmpty = true;
	
	/**
	 * Constructor for SqlBatch.
	 * @param header String
	 * @param tail String
	 */
	public SqlBatch(String header, String tail)
	{
		_header = header + "\n";
		_tail = (tail != null) && (tail.length() > 0) ? " " + tail + ";\n" : ";\n";
		_sb = new StringBuilder(_header);
		_result = new StringBuilder();
	}
	
	/**
	 * Constructor for SqlBatch.
	 * @param header String
	 */
	public SqlBatch(String header)
	{
		this(header, null);
	}
	
	/**
	 * Method writeStructure.
	 * @param str String
	 */
	public void writeStructure(String str)
	{
		_result.append(str);
	}
	
	/**
	 * Method write.
	 * @param str String
	 */
	public void write(String str)
	{
		isEmpty = false;
		if ((_sb.length() + str.length()) < (_limit - _tail.length()))
		{
			_sb.append(str + ",\n");
		}
		else
		{
			_sb.append(str + _tail);
			_result.append(_sb.toString());
			_sb = new StringBuilder(_header);
		}
	}
	
	/**
	 * Method writeBuffer.
	 */
	public void writeBuffer()
	{
		String last = _sb.toString();
		if (last.length() > 0)
		{
			_result.append(last.substring(0, last.length() - 2) + _tail);
		}
		_sb = new StringBuilder(_header);
	}
	
	/**
	 * Method close.
	 * @return String
	 */
	public String close()
	{
		if (_sb.length() > _header.length())
		{
			writeBuffer();
		}
		return _result.toString();
	}
	
	/**
	 * Method setLimit.
	 * @param l long
	 */
	public void setLimit(long l)
	{
		_limit = l;
	}
	
	/**
	 * Method isEmpty.
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return isEmpty;
	}
}
