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

import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ArmorTemplate.ArmorType;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EtcItemTemplate extends ItemTemplate
{
	/**
	 * @author Mobius
	 */
	public enum EtcItemType implements ItemType
	{
		/**
		 * Field ARROW.
		 */
		ARROW(1, "Arrow"),
		/**
		 * Field MATERIAL.
		 */
		MATERIAL(2, "Material"),
		/**
		 * Field PET_COLLAR.
		 */
		PET_COLLAR(3, "PetCollar"),
		/**
		 * Field POTION.
		 */
		POTION(4, "Potion"),
		/**
		 * Field RECIPE.
		 */
		RECIPE(5, "Recipe"),
		/**
		 * Field SCROLL.
		 */
		SCROLL(6, "Scroll"),
		/**
		 * Field QUEST.
		 */
		QUEST(7, "Quest"),
		/**
		 * Field MONEY.
		 */
		MONEY(8, "Money"),
		/**
		 * Field OTHER.
		 */
		OTHER(9, "Other"),
		/**
		 * Field SPELLBOOK.
		 */
		SPELLBOOK(10, "Spellbook"),
		/**
		 * Field SEED.
		 */
		SEED(11, "Seed"),
		/**
		 * Field BAIT.
		 */
		BAIT(12, "Bait"),
		/**
		 * Field SHOT.
		 */
		SHOT(13, "Shot"),
		/**
		 * Field BOLT.
		 */
		BOLT(14, "Bolt"),
		/**
		 * Field RUNE.
		 */
		RUNE(15, "Rune"),
		/**
		 * Field HERB.
		 */
		HERB(16, "Herb"),
		/**
		 * Field MERCENARY_TICKET.
		 */
		MERCENARY_TICKET(17, "Mercenary Ticket"),
		/**
		 * Field UNLIMITED_ARROW.
		 */
		UNLIMITED_ARROW(18, "Unlimited Arrow");
		/**
		 * Field _mask.
		 */
		private final long _mask;
		/**
		 * Field _name.
		 */
		private final String _name;
		
		/**
		 * Constructor for EtcItemType.
		 * @param id int
		 * @param name String
		 */
		EtcItemType(int id, String name)
		{
			_mask = 1L << (id + WeaponType.VALUES.length + ArmorType.VALUES.length);
			_name = name;
		}
		
		/**
		 * Method mask.
		 * @return long * @see lineage2.gameserver.templates.item.ItemType#mask()
		 */
		@Override
		public long mask()
		{
			return _mask;
		}
		
		/**
		 * Method toString.
		 * @return String
		 */
		@Override
		public String toString()
		{
			return _name;
		}
	}
	
	/**
	 * Constructor for EtcItemTemplate.
	 * @param set StatsSet
	 */
	public EtcItemTemplate(StatsSet set)
	{
		super(set);
		type = set.getEnum("type", EtcItemType.class);
		_type1 = TYPE1_ITEM_QUESTITEM_ADENA;
		switch (getItemType())
		{
			case QUEST:
				_type2 = TYPE2_QUEST;
				break;
			case MONEY:
				_type2 = TYPE2_MONEY;
				break;
			default:
				_type2 = TYPE2_OTHER;
				break;
		}
	}
	
	/**
	 * Method getItemType.
	 * @return EtcItemType
	 */
	@Override
	public EtcItemType getItemType()
	{
		return (EtcItemType) super.type;
	}
	
	/**
	 * Method getItemMask.
	 * @return long
	 */
	@Override
	public long getItemMask()
	{
		return getItemType().mask();
	}
	
	/**
	 * Method isShadowItem.
	 * @return boolean
	 */
	@Override
	public final boolean isShadowItem()
	{
		return false;
	}
	
	/**
	 * Method canBeEnchanted.
	 * @return boolean
	 */
	@Override
	public final boolean canBeEnchanted()
	{
		return false;
	}
}
