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
package lineage2.gameserver.network.telnet;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class TelnetCommand implements Comparable<TelnetCommand>
{
	/**
	 * Field command.
	 */
	private final String command;
	/**
	 * Field acronyms.
	 */
	private final String[] acronyms;
	
	/**
	 * Constructor for TelnetCommand.
	 * @param command String
	 */
	public TelnetCommand(String command)
	{
		this(command, ArrayUtils.EMPTY_STRING_ARRAY);
	}
	
	/**
	 * Constructor for TelnetCommand.
	 * @param command String
	 * @param acronyms String[]
	 */
	public TelnetCommand(String command, String... acronyms)
	{
		this.command = command;
		this.acronyms = acronyms;
	}
	
	/**
	 * Method getCommand.
	 * @return String
	 */
	public String getCommand()
	{
		return command;
	}
	
	/**
	 * Method getAcronyms.
	 * @return String[]
	 */
	public String[] getAcronyms()
	{
		return acronyms;
	}
	
	/**
	 * Method getUsage.
	 * @return String
	 */
	public abstract String getUsage();
	
	/**
	 * Method handle.
	 * @param args String[]
	 * @return String
	 */
	public abstract String handle(String[] args);
	
	/**
	 * Method equals.
	 * @param command String
	 * @return boolean
	 */
	public boolean equals(String command)
	{
		for (String acronym : acronyms)
		{
			if (command.equals(acronym))
			{
				return true;
			}
		}
		return this.command.equalsIgnoreCase(command);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return command;
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return true;
		}
		if (o instanceof TelnetCommand)
		{
			return command.equals(((TelnetCommand) o).command);
		}
		return false;
	}
	
	/**
	 * Method compareTo.
	 * @param o TelnetCommand
	 * @return int
	 */
	@Override
	public int compareTo(TelnetCommand o)
	{
		return command.compareTo(o.command);
	}
}
