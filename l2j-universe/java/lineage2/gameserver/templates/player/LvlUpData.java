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
public final class LvlUpData
{
	/**
	 * Field _hp.
	 */
	private final double _hp;
	/**
	 * Field _mp.
	 */
	private final double _mp;
	/**
	 * Field _cp.
	 */
	private final double _cp;
	
	/**
	 * Constructor for LvlUpData.
	 * @param hp double
	 * @param mp double
	 * @param cp double
	 */
	public LvlUpData(double hp, double mp, double cp)
	{
		_hp = hp;
		_mp = mp;
		_cp = cp;
	}
	
	/**
	 * Method getHP.
	 * @return double
	 */
	public double getHP()
	{
		return _hp;
	}
	
	/**
	 * Method getMP.
	 * @return double
	 */
	public double getMP()
	{
		return _mp;
	}
	
	/**
	 * Method getCP.
	 * @return double
	 */
	public double getCP()
	{
		return _cp;
	}
}
