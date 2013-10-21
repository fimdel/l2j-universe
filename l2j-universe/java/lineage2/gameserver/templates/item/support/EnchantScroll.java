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
package lineage2.gameserver.templates.item.support;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnchantScroll extends EnchantItem
{
	/**
	 * Field _resultType.
	 */
	private final FailResultType _resultType;
	/**
	 * Field _visualEffect.
	 */
	private final boolean _visualEffect;
	
	/**
	 * Constructor for EnchantScroll.
	 * @param itemId int
	 * @param chance int
	 * @param maxEnchant int
	 * @param resultType FailResultType
	 * @param visualEffect boolean
	 */
	public EnchantScroll(int itemId, int chance, int maxEnchant, FailResultType resultType, boolean visualEffect)
	{
		super(itemId, chance, maxEnchant);
		_resultType = resultType;
		_visualEffect = visualEffect;
	}
	
	/**
	 * Method getResultType.
	 * @return FailResultType
	 */
	public FailResultType getResultType()
	{
		return _resultType;
	}
	
	/**
	 * Method isHasVisualEffect.
	 * @return boolean
	 */
	public boolean isHasVisualEffect()
	{
		return _visualEffect;
	}
}
