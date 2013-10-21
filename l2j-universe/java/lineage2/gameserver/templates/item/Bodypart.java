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
package lineage2.gameserver.templates.item;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum Bodypart
{
	/**
	 * Field NONE.
	 */
	NONE(ItemTemplate.SLOT_NONE),
	/**
	 * Field CHEST.
	 */
	CHEST(ItemTemplate.SLOT_CHEST),
	/**
	 * Field BELT.
	 */
	BELT(ItemTemplate.SLOT_BELT),
	/**
	 * Field RIGHT_BRACELET.
	 */
	RIGHT_BRACELET(ItemTemplate.SLOT_R_BRACELET),
	/**
	 * Field LEFT_BRACELET.
	 */
	LEFT_BRACELET(ItemTemplate.SLOT_L_BRACELET),
	/**
	 * Field FULL_ARMOR.
	 */
	FULL_ARMOR(ItemTemplate.SLOT_FULL_ARMOR),
	/**
	 * Field HEAD.
	 */
	HEAD(ItemTemplate.SLOT_HEAD),
	/**
	 * Field HAIR.
	 */
	HAIR(ItemTemplate.SLOT_HAIR),
	/**
	 * Field FACE.
	 */
	FACE(ItemTemplate.SLOT_DHAIR),
	/**
	 * Field HAIR_ALL.
	 */
	HAIR_ALL(ItemTemplate.SLOT_HAIRALL),
	/**
	 * Field UNDERWEAR.
	 */
	UNDERWEAR(ItemTemplate.SLOT_UNDERWEAR),
	/**
	 * Field BACK.
	 */
	BACK(ItemTemplate.SLOT_BACK),
	/**
	 * Field NECKLACE.
	 */
	NECKLACE(ItemTemplate.SLOT_NECK),
	/**
	 * Field LEGS.
	 */
	LEGS(ItemTemplate.SLOT_LEGS),
	/**
	 * Field FEET.
	 */
	FEET(ItemTemplate.SLOT_FEET),
	/**
	 * Field GLOVES.
	 */
	GLOVES(ItemTemplate.SLOT_GLOVES),
	/**
	 * Field RIGHT_HAND.
	 */
	RIGHT_HAND(ItemTemplate.SLOT_R_HAND),
	/**
	 * Field LEFT_HAND.
	 */
	LEFT_HAND(ItemTemplate.SLOT_L_HAND),
	/**
	 * Field LEFT_RIGHT_HAND.
	 */
	LEFT_RIGHT_HAND(ItemTemplate.SLOT_LR_HAND),
	/**
	 * Field RIGHT_EAR.
	 */
	RIGHT_EAR(ItemTemplate.SLOT_R_EAR),
	/**
	 * Field LEFT_EAR.
	 */
	LEFT_EAR(ItemTemplate.SLOT_L_EAR),
	/**
	 * Field RIGHT_FINGER.
	 */
	RIGHT_FINGER(ItemTemplate.SLOT_R_FINGER),
	/**
	 * Field FORMAL_WEAR.
	 */
	FORMAL_WEAR(ItemTemplate.SLOT_FORMAL_WEAR),
	/**
	 * Field TALISMAN.
	 */
	TALISMAN(ItemTemplate.SLOT_DECO),
	/**
	 * Field LEFT_FINGER.
	 */
	LEFT_FINGER(ItemTemplate.SLOT_L_FINGER),
	/**
	 * Field WOLF.
	 */
	WOLF(ItemTemplate.SLOT_WOLF, CHEST),
	/**
	 * Field GREAT_WOLF.
	 */
	GREAT_WOLF(ItemTemplate.SLOT_GWOLF, CHEST),
	/**
	 * Field HATCHLING.
	 */
	HATCHLING(ItemTemplate.SLOT_HATCHLING, CHEST),
	/**
	 * Field STRIDER.
	 */
	STRIDER(ItemTemplate.SLOT_STRIDER, CHEST),
	/**
	 * Field BABY_PET.
	 */
	BABY_PET(ItemTemplate.SLOT_BABYPET, CHEST),
	/**
	 * Field PENDANT.
	 */
	PENDANT(ItemTemplate.SLOT_PENDANT, NECKLACE);
	/**
	 * Field _mask.
	 */
	private int _mask;
	/**
	 * Field _real.
	 */
	private Bodypart _real;
	
	/**
	 * Constructor for Bodypart.
	 * @param mask int
	 */
	Bodypart(int mask)
	{
		this(mask, null);
	}
	
	/**
	 * Constructor for Bodypart.
	 * @param mask int
	 * @param real Bodypart
	 */
	Bodypart(int mask, Bodypart real)
	{
		_mask = mask;
		_real = real;
	}
	
	/**
	 * Method mask.
	 * @return int
	 */
	public int mask()
	{
		return _mask;
	}
	
	/**
	 * Method getReal.
	 * @return Bodypart
	 */
	public Bodypart getReal()
	{
		return _real;
	}
}
