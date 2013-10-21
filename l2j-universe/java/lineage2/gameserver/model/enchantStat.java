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
package lineage2.gameserver.model;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class enchantStat
{
	/**
	 * Field _level.
	 */
	private final int _grade;
	/**
	 * Field _minLevel.
	 */
	private final double _option1;
	/**
	 * Field _cost.
	 */
	private final double _option2;
	/**
	 * Constructor for enchantStat.
	 * param String name
	 * param int grade
	 * param Double option1
	 * param Double option2
	 * param Double option3
	 */
	public enchantStat(int grade, Double option1, Double option2)
	{
		_grade = grade;
		_option1 = option1;
		_option2 = option2;
	}
	
	/**
	 * Method getGrade.
	 * @return int
	 */
	public int getGrade()
	{
		return _grade;
	}
	
	/**
	 * Method getOption1.
	 * @return double
	 */
	public double getOption1()
	{
		return _option1;
	}
	
	/**
	 * Method getOption2.
	 * @return double
	 */
	public double getOption2()
	{
		return _option2;
	}
	
}
