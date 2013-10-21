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

import java.util.Map;

import lineage2.commons.collections.MultiValueSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LureTemplate
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _lengthBonus.
	 */
	private final int _lengthBonus;
	/**
	 * Field _revisionNumber.
	 */
	private final double _revisionNumber;
	/**
	 * Field _rateBonus.
	 */
	private final double _rateBonus;
	/**
	 * Field _lureType.
	 */
	private final LureType _lureType;
	/**
	 * Field _chances.
	 */
	private final Map<FishGroup, Integer> _chances;
	
	/**
	 * Constructor for LureTemplate.
	 * @param set MultiValueSet<String>
	 */
	@SuppressWarnings("unchecked")
	public LureTemplate(MultiValueSet<String> set)
	{
		_itemId = set.getInteger("item_id");
		_lengthBonus = set.getInteger("length_bonus");
		_revisionNumber = set.getDouble("revision_number");
		_rateBonus = set.getDouble("rate_bonus");
		_lureType = set.getEnum("type", LureType.class);
		_chances = (Map<FishGroup, Integer>) set.get("chances");
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getLengthBonus.
	 * @return int
	 */
	public int getLengthBonus()
	{
		return _lengthBonus;
	}
	
	/**
	 * Method getRevisionNumber.
	 * @return double
	 */
	public double getRevisionNumber()
	{
		return _revisionNumber;
	}
	
	/**
	 * Method getRateBonus.
	 * @return double
	 */
	public double getRateBonus()
	{
		return _rateBonus;
	}
	
	/**
	 * Method getLureType.
	 * @return LureType
	 */
	public LureType getLureType()
	{
		return _lureType;
	}
	
	/**
	 * Method getChances.
	 * @return Map<FishGroup,Integer>
	 */
	public Map<FishGroup, Integer> getChances()
	{
		return _chances;
	}
}
