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

import lineage2.gameserver.Config;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class WeaponTemplate extends ItemTemplate
{
	/**
	 * Field _soulShotCount.
	 */
	private final int _soulShotCount;
	/**
	 * Field _spiritShotCount.
	 */
	private final int _spiritShotCount;
	/**
	 * Field _kamaelConvert.
	 */
	private final int _kamaelConvert;
	/**
	 * Field _rndDam.
	 */
	private final int _rndDam;
	/**
	 * Field _atkReuse.
	 */
	private final int _atkReuse;
	/**
	 * Field _mpConsume.
	 */
	private final int _mpConsume;
	/**
	 * Field _critical.
	 */
	private int _critical;
	
	/**
	 * @author Mobius
	 */
	public enum WeaponType implements ItemType
	{
		/**
		 * Field NONE.
		 */
		NONE(1, "Shield", null),
		/**
		 * Field SWORD.
		 */
		SWORD(2, "Sword", Stats.SWORD_WPN_VULNERABILITY),
		/**
		 * Field BLUNT.
		 */
		BLUNT(3, "Blunt", Stats.BLUNT_WPN_VULNERABILITY),
		/**
		 * Field DAGGER.
		 */
		DAGGER(4, "Dagger", Stats.DAGGER_WPN_VULNERABILITY),
		/**
		 * Field BOW.
		 */
		BOW(5, "Bow", Stats.BOW_WPN_VULNERABILITY),
		/**
		 * Field POLE.
		 */
		POLE(6, "Pole", Stats.POLE_WPN_VULNERABILITY),
		/**
		 * Field ETC.
		 */
		ETC(7, "Etc", null),
		/**
		 * Field FIST.
		 */
		FIST(8, "Fist", Stats.FIST_WPN_VULNERABILITY),
		/**
		 * Field DUAL.
		 */
		DUAL(9, "Dual Sword", Stats.DUAL_WPN_VULNERABILITY),
		/**
		 * Field DUALFIST.
		 */
		DUALFIST(10, "Dual Fist", Stats.FIST_WPN_VULNERABILITY),
		/**
		 * Field BIGSWORD.
		 */
		BIGSWORD(11, "Big Sword", Stats.SWORD_WPN_VULNERABILITY),
		/**
		 * Field PET.
		 */
		PET(12, "Pet", Stats.FIST_WPN_VULNERABILITY),
		/**
		 * Field ROD.
		 */
		ROD(13, "Rod", null),
		/**
		 * Field BIGBLUNT.
		 */
		BIGBLUNT(14, "Big Blunt", Stats.BLUNT_WPN_VULNERABILITY),
		/**
		 * Field CROSSBOW.
		 */
		CROSSBOW(15, "Crossbow", Stats.CROSSBOW_WPN_VULNERABILITY),
		/**
		 * Field RAPIER.
		 */
		RAPIER(16, "Rapier", Stats.DAGGER_WPN_VULNERABILITY),
		/**
		 * Field ANCIENTSWORD.
		 */
		ANCIENTSWORD(17, "Ancient Sword", Stats.SWORD_WPN_VULNERABILITY),
		/**
		 * Field DUALDAGGER.
		 */
		DUALDAGGER(18, "Dual Dagger", Stats.DAGGER_WPN_VULNERABILITY),
		/**
		 * Field DUALBLUNT.
		 */
		DUALBLUNT(19, "Dual Blunt", null);
		/**
		 * Field VALUES.
		 */
		public final static WeaponType[] VALUES = values();
		/**
		 * Field _mask.
		 */
		private final long _mask;
		/**
		 * Field _name.
		 */
		private final String _name;
		/**
		 * Field _defence.
		 */
		private final Stats _defence;
		
		/**
		 * Constructor for WeaponType.
		 * @param id int
		 * @param name String
		 * @param defence Stats
		 */
		private WeaponType(int id, String name, Stats defence)
		{
			_mask = 1L << id;
			_name = name;
			_defence = defence;
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
		 * Method getDefence.
		 * @return Stats
		 */
		public Stats getDefence()
		{
			return _defence;
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
	 * Constructor for WeaponTemplate.
	 * @param set StatsSet
	 */
	public WeaponTemplate(StatsSet set)
	{
		super(set);
		type = set.getEnum("type", WeaponType.class);
		_soulShotCount = set.getInteger("soulshots", 0);
		_spiritShotCount = set.getInteger("spiritshots", 0);
		_kamaelConvert = set.getInteger("kamael_convert", 0);
		_rndDam = set.getInteger("rnd_dam", 0);
		_atkReuse = set.getInteger("atk_reuse", type == WeaponType.BOW ? Config.BOW_REUSE : type == WeaponType.CROSSBOW ? Config.CROSSBOW_REUSE : 0);
		_mpConsume = set.getInteger("mp_consume", 0);
		if (getItemType() == WeaponType.NONE)
		{
			_type1 = TYPE1_SHIELD_ARMOR;
			_type2 = TYPE2_SHIELD_ARMOR;
		}
		else
		{
			_type1 = TYPE1_WEAPON_RING_EARRING_NECKLACE;
			_type2 = TYPE2_WEAPON;
		}
		if (getItemType() == WeaponType.PET)
		{
			_type1 = ItemTemplate.TYPE1_WEAPON_RING_EARRING_NECKLACE;
			if (_bodyPart == ItemTemplate.SLOT_WOLF)
			{
				_type2 = ItemTemplate.TYPE2_PET_WOLF;
			}
			else if (_bodyPart == ItemTemplate.SLOT_GWOLF)
			{
				_type2 = ItemTemplate.TYPE2_PET_GWOLF;
			}
			else if (_bodyPart == ItemTemplate.SLOT_HATCHLING)
			{
				_type2 = ItemTemplate.TYPE2_PET_HATCHLING;
			}
			else
			{
				_type2 = ItemTemplate.TYPE2_PET_STRIDER;
			}
			_bodyPart = ItemTemplate.SLOT_R_HAND;
		}
	}
	
	/**
	 * Method getItemType.
	 * @return WeaponType
	 */
	@Override
	public WeaponType getItemType()
	{
		return (WeaponType) type;
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
	 * Method getSoulShotCount.
	 * @return int
	 */
	public int getSoulShotCount()
	{
		return _soulShotCount;
	}
	
	/**
	 * Method getSpiritShotCount.
	 * @return int
	 */
	public int getSpiritShotCount()
	{
		return _spiritShotCount;
	}
	
	/**
	 * Method getCritical.
	 * @return int
	 */
	public int getCritical()
	{
		return _critical;
	}
	
	/**
	 * Method getRandomDamage.
	 * @return int
	 */
	public int getRandomDamage()
	{
		return _rndDam;
	}
	
	/**
	 * Method getAttackReuseDelay.
	 * @return int
	 */
	public int getAttackReuseDelay()
	{
		return _atkReuse;
	}
	
	/**
	 * Method getMpConsume.
	 * @return int
	 */
	public int getMpConsume()
	{
		return _mpConsume;
	}
	
	/**
	 * Method getAttackRange.
	 * @return int
	 */
	public int getAttackRange()
	{
		switch (getItemType())
		{
			case BOW:
				return 460;
			case CROSSBOW:
				return 360;
			case POLE:
				return 40;
			default:
				return 0;
		}
	}
	
	/**
	 * Method attachFunc.
	 * @param f FuncTemplate
	 */
	@Override
	public void attachFunc(FuncTemplate f)
	{
		if ((f._stat == Stats.CRITICAL_BASE) && (f._order == 0x08))
		{
			_critical = (int) Math.round(f._value / 10);
		}
		super.attachFunc(f);
	}
	
	/**
	 * Method getKamaelConvert.
	 * @return int
	 */
	public int getKamaelConvert()
	{
		return _kamaelConvert;
	}
}
