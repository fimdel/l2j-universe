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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum ExItemType
{
	/**
	 * Field EMPTY0.
	 */
	EMPTY0(0),
	/**
	 * Field SWORD.
	 */
	SWORD(0),
	/**
	 * Field MAGIC_SWORD.
	 */
	MAGIC_SWORD(0),
	/**
	 * Field DAGGER.
	 */
	DAGGER(0),
	/**
	 * Field RAPIER.
	 */
	RAPIER(0),
	/**
	 * Field BIG_SWORD.
	 */
	BIG_SWORD(0),
	/**
	 * Field ANCIENT_SWORD.
	 */
	ANCIENT_SWORD(0),
	/**
	 * Field DUAL_SWORD.
	 */
	DUAL_SWORD(0),
	/**
	 * Field DUAL_DAGGER.
	 */
	DUAL_DAGGER(0),
	/**
	 * Field BLUNT_WEAPON.
	 */
	BLUNT_WEAPON(0),
	/**
	 * Field MAGIC_BLUNT_WEAPON.
	 */
	MAGIC_BLUNT_WEAPON(0),
	/**
	 * Field BIG_BLUNT_WEAPON.
	 */
	BIG_BLUNT_WEAPON(0),
	/**
	 * Field BIG_MAGIC_BLUNT_WEAPON.
	 */
	BIG_MAGIC_BLUNT_WEAPON(0),
	/**
	 * Field DUAL_BLUNT_WEAPON.
	 */
	DUAL_BLUNT_WEAPON(0),
	/**
	 * Field BOW.
	 */
	BOW(0),
	/**
	 * Field CROSSBOW.
	 */
	CROSSBOW(0),
	/**
	 * Field HAND_TO_HAND.
	 */
	HAND_TO_HAND(0),
	/**
	 * Field POLE.
	 */
	POLE(0),
	/**
	 * Field OTHER_WEAPON.
	 */
	OTHER_WEAPON(0),
	/**
	 * Field HELMET.
	 */
	HELMET(1),
	/**
	 * Field UPPER_PIECE.
	 */
	UPPER_PIECE(1),
	/**
	 * Field LOWER_PIECE.
	 */
	LOWER_PIECE(1),
	/**
	 * Field FULL_BODY.
	 */
	FULL_BODY(1),
	/**
	 * Field GLOVES.
	 */
	GLOVES(1),
	/**
	 * Field FEET.
	 */
	FEET(1),
	/**
	 * Field SHIELD.
	 */
	SHIELD(1),
	/**
	 * Field SIGIL.
	 */
	SIGIL(1),
	/**
	 * Field UNDERWEAR.
	 */
	UNDERWEAR(1),
	/**
	 * Field CLOAK.
	 */
	CLOAK(1),
	/**
	 * Field RING.
	 */
	RING(2),
	/**
	 * Field EARRING.
	 */
	EARRING(2),
	/**
	 * Field NECKLACE.
	 */
	NECKLACE(2),
	/**
	 * Field BELT.
	 */
	BELT(2),
	/**
	 * Field BRACELET.
	 */
	BRACELET(2),
	/**
	 * Field HAIR_ACCESSORY.
	 */
	HAIR_ACCESSORY(2),
	/**
	 * Field POTION.
	 */
	POTION(3),
	/**
	 * Field SCROLL_ENCHANT_WEAPON.
	 */
	SCROLL_ENCHANT_WEAPON(3),
	/**
	 * Field SCROLL_ENCHANT_ARMOR.
	 */
	SCROLL_ENCHANT_ARMOR(3),
	/**
	 * Field SCROLL_OTHER.
	 */
	SCROLL_OTHER(3),
	/**
	 * Field SOULSHOT.
	 */
	SOULSHOT(3),
	/**
	 * Field SPIRITSHOT.
	 */
	SPIRITSHOT(3),
	/**
	 * Field EMPTY41.
	 */
	EMPTY41(3),
	/**
	 * Field PET_EQUIPMENT.
	 */
	PET_EQUIPMENT(4),
	/**
	 * Field PET_SUPPLIES.
	 */
	PET_SUPPLIES(4),
	/**
	 * Field CRYSTAL.
	 */
	CRYSTAL(5),
	/**
	 * Field RECIPE.
	 */
	RECIPE(5),
	/**
	 * Field CRAFTING_MAIN_INGRIDIENTS.
	 */
	CRAFTING_MAIN_INGRIDIENTS(5),
	/**
	 * Field LIFE_STONE.
	 */
	LIFE_STONE(5),
	/**
	 * Field SOUL_CRYSTAL.
	 */
	SOUL_CRYSTAL(5),
	/**
	 * Field ATTRIBUTE_STONE.
	 */
	ATTRIBUTE_STONE(5),
	/**
	 * Field WEAPON_ENCHANT_STONE.
	 */
	WEAPON_ENCHANT_STONE(5),
	/**
	 * Field ARMOR_ENCHANT_STONE.
	 */
	ARMOR_ENCHANT_STONE(5),
	/**
	 * Field SPELLBOOK.
	 */
	SPELLBOOK(5),
	/**
	 * Field GEMSTONE.
	 */
	GEMSTONE(5),
	/**
	 * Field POUCH.
	 */
	POUCH(5),
	/**
	 * Field PIN.
	 */
	PIN(5),
	/**
	 * Field MAGIC_RUNE_CLIP.
	 */
	MAGIC_RUNE_CLIP(5),
	/**
	 * Field MAGIC_ORNAMENT.
	 */
	MAGIC_ORNAMENT(5),
	/**
	 * Field DYES.
	 */
	DYES(5),
	/**
	 * Field OTHER_ITEMS.
	 */
	OTHER_ITEMS(5);
	/**
	 * Field WEAPON_MASK. (value is 0)
	 */
	public static final int WEAPON_MASK = 0;
	/**
	 * Field ARMOR_MASK. (value is 1)
	 */
	public static final int ARMOR_MASK = 1;
	/**
	 * Field ACCESSORIES_MASK. (value is 2)
	 */
	public static final int ACCESSORIES_MASK = 2;
	/**
	 * Field SUPPLIES_MASK. (value is 3)
	 */
	public static final int SUPPLIES_MASK = 3;
	/**
	 * Field PET_GOODS_MASK. (value is 4)
	 */
	public static final int PET_GOODS_MASK = 4;
	/**
	 * Field ETC_MASK. (value is 5)
	 */
	public static final int ETC_MASK = 5;
	/**
	 * Field mask.
	 */
	private int mask;
	
	/**
	 * Constructor for ExItemType.
	 * @param mask int
	 */
	ExItemType(int mask)
	{
		this.mask = mask;
	}
	
	/**
	 * Method getMask.
	 * @return int
	 */
	public int getMask()
	{
		return mask;
	}
	
	/**
	 * Method getTypesForMask.
	 * @param mask int
	 * @return ExItemType[]
	 */
	public static ExItemType[] getTypesForMask(int mask)
	{
		if ((mask < WEAPON_MASK) && (mask > ETC_MASK))
		{
			return new ExItemType[0];
		}
		List<ExItemType> list = new ArrayList<>();
		for (ExItemType exType : values())
		{
			if (exType.getMask() == mask)
			{
				list.add(exType);
			}
		}
		return list.toArray(new ExItemType[list.size()]);
	}
	
	/**
	 * Method valueOf.
	 * @param ordinal int
	 * @return ExItemType
	 */
	public static ExItemType valueOf(int ordinal)
	{
		for (ExItemType type : values())
		{
			if (type.ordinal() == ordinal)
			{
				return type;
			}
		}
		return null;
	}
}
