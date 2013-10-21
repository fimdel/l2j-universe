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
public final class AttributeCap
{
	private final Integer _capVal;
	
	private final Double _attackVal;
	
	private final Double _defenseVal;
	/**
	 * Constructor for enchantStat.
	 * param String name
	 * param int grade
	 * param Double option1
	 * param Double option2
	 * param Double option3
	 */
	
	public AttributeCap(Integer capValues, Double attackValue, Double defenseValue)
	{
		_capVal = capValues;
		_attackVal = attackValue;
		_defenseVal = defenseValue;
	}

	public Integer getCap()
	{
		return _capVal;
	}
	/**
	 * Method getOption1.
	 * @return double
	 */
	public double getAttackBonus()
	{
		return _attackVal;
	}
	
	/**
	 * Method getOption2.
	 * @return double
	 */
	public double getDefenseBonus()
	{
		return _defenseVal;
	}
	
}
