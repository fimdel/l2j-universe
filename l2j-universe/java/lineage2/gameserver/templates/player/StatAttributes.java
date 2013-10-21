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
package lineage2.gameserver.templates.player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StatAttributes
{
	/**
	 * Field _int.
	 */
	private int _int = 0;
	/**
	 * Field _str.
	 */
	private int _str = 0;
	/**
	 * Field _con.
	 */
	private int _con = 0;
	/**
	 * Field _men.
	 */
	private int _men = 0;
	/**
	 * Field _dex.
	 */
	private int _dex = 0;
	/**
	 * Field _wit.
	 */
	private int _wit = 0;
	
	/**
	 * Constructor for StatAttributes.
	 * @param _int int
	 * @param str int
	 * @param con int
	 * @param men int
	 * @param dex int
	 * @param wit int
	 */
	public StatAttributes(int _int, int str, int con, int men, int dex, int wit)
	{
		this._int = _int;
		_str = str;
		_con = con;
		_men = men;
		_dex = dex;
		_wit = wit;
	}
	
	/**
	 * Method getINT.
	 * @return int
	 */
	public int getINT()
	{
		return _int;
	}
	
	/**
	 * Method getSTR.
	 * @return int
	 */
	public int getSTR()
	{
		return _str;
	}
	
	/**
	 * Method getCON.
	 * @return int
	 */
	public int getCON()
	{
		return _con;
	}
	
	/**
	 * Method getMEN.
	 * @return int
	 */
	public int getMEN()
	{
		return _men;
	}
	
	/**
	 * Method getDEX.
	 * @return int
	 */
	public int getDEX()
	{
		return _dex;
	}
	
	/**
	 * Method getWIT.
	 * @return int
	 */
	public int getWIT()
	{
		return _wit;
	}
}
