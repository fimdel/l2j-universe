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

import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.gameserver.handler.items.IItemHandler;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.StatTemplate;
import lineage2.gameserver.stats.conditions.Condition;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.EtcItemTemplate.EtcItemType;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class ItemTemplate extends StatTemplate
{
	/**
	 * @author Mobius
	 */
	public static enum ReuseType
	{
		/**
		 * Field NORMAL.
		 */
		NORMAL(SystemMsg.THERE_ARE_S2_SECONDS_REMAINING_IN_S1S_REUSE_TIME, SystemMsg.THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_IN_S1S_REUSE_TIME, SystemMsg.THERE_ARE_S2_HOURS_S3_MINUTES_AND_S4_SECONDS_REMAINING_IN_S1S_REUSE_TIME)
		{
			@Override
			public long next(ItemInstance item)
			{
				return System.currentTimeMillis() + item.getTemplate().getReuseDelay();
			}
		},
		/**
		 * Field EVERY_DAY_AT_6_30.
		 */
		EVERY_DAY_AT_6_30(SystemMsg.THERE_ARE_S2_SECONDS_REMAINING_FOR_S1S_REUSE_TIME, SystemMsg.THERE_ARE_S2_MINUTES_S3_SECONDS_REMAINING_FOR_S1S_REUSE_TIME, SystemMsg.THERE_ARE_S2_HOURS_S3_MINUTES_S4_SECONDS_REMAINING_FOR_S1S_REUSE_TIME)
		{
			private final SchedulingPattern _pattern = new SchedulingPattern("30 6 * * *");
			
			@Override
			public long next(ItemInstance item)
			{
				return _pattern.next(System.currentTimeMillis());
			}
		};
		/**
		 * Field _messages.
		 */
		private SystemMsg[] _messages;
		
		/**
		 * Constructor for ReuseType.
		 * @param msg SystemMsg[]
		 */
		ReuseType(SystemMsg... msg)
		{
			_messages = msg;
		}
		
		/**
		 * Method next.
		 * @param item ItemInstance
		 * @return long
		 */
		public abstract long next(ItemInstance item);
		
		/**
		 * Method getMessages.
		 * @return SystemMsg[]
		 */
		public SystemMsg[] getMessages()
		{
			return _messages;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static enum ItemClass
	{
		/**
		 * Field ALL.
		 */
		ALL,
		/**
		 * Field WEAPON.
		 */
		WEAPON,
		/**
		 * Field ARMOR.
		 */
		ARMOR,
		/**
		 * Field JEWELRY.
		 */
		JEWELRY,
		/**
		 * Field ACCESSORY.
		 */
		ACCESSORY,
		/**
		 * Field CONSUMABLE.
		 */
		CONSUMABLE,
		/**
		 * Field MATHERIALS.
		 */
		MATHERIALS,
		/**
		 * Field PIECES.
		 */
		PIECES,
		/**
		 * Field RECIPIES.
		 */
		RECIPIES,
		/**
		 * Field SPELLBOOKS.
		 */
		SPELLBOOKS,
		/**
		 * Field MISC.
		 */
		MISC,
		/**
		 * Field OTHER.
		 */
		OTHER
	}
	
	/**
	 * Field ITEM_ID_PC_BANG_POINTS. (value is -100)
	 */
	public static final int ITEM_ID_PC_BANG_POINTS = -100;
	/**
	 * Field ITEM_ID_CLAN_REPUTATION_SCORE. (value is -200)
	 */
	public static final int ITEM_ID_CLAN_REPUTATION_SCORE = -200;
	/**
	 * Field ITEM_ID_FAME. (value is -300)
	 */
	public static final int ITEM_ID_FAME = -300;
	/**
	 * Field ITEM_ID_ADENA. (value is 57)
	 */
	public static final int ITEM_ID_ADENA = 57;
	/**
	 * Field ITEM_ID_CASTLE_CIRCLET.
	 */
	public static final int[] ITEM_ID_CASTLE_CIRCLET =
	{
		0,
		6838,
		6835,
		6839,
		6837,
		6840,
		6834,
		6836,
		8182,
		8183,
	};
	/**
	 * Field ITEM_ID_FORMAL_WEAR. (value is 6408)
	 */
	public static final int ITEM_ID_FORMAL_WEAR = 6408;
	/**
	 * Field TYPE1_WEAPON_RING_EARRING_NECKLACE. (value is 0)
	 */
	public static final int TYPE1_WEAPON_RING_EARRING_NECKLACE = 0;
	/**
	 * Field TYPE1_SHIELD_ARMOR. (value is 1)
	 */
	public static final int TYPE1_SHIELD_ARMOR = 1;
	/**
	 * Field TYPE1_OTHER. (value is 2)
	 */
	public static final int TYPE1_OTHER = 2;
	/**
	 * Field TYPE1_ITEM_QUESTITEM_ADENA. (value is 4)
	 */
	public static final int TYPE1_ITEM_QUESTITEM_ADENA = 4;
	/**
	 * Field TYPE2_WEAPON. (value is 0)
	 */
	public static final int TYPE2_WEAPON = 0;
	/**
	 * Field TYPE2_SHIELD_ARMOR. (value is 1)
	 */
	public static final int TYPE2_SHIELD_ARMOR = 1;
	/**
	 * Field TYPE2_ACCESSORY. (value is 2)
	 */
	public static final int TYPE2_ACCESSORY = 2;
	/**
	 * Field TYPE2_QUEST. (value is 3)
	 */
	public static final int TYPE2_QUEST = 3;
	/**
	 * Field TYPE2_MONEY. (value is 4)
	 */
	public static final int TYPE2_MONEY = 4;
	/**
	 * Field TYPE2_OTHER. (value is 5)
	 */
	public static final int TYPE2_OTHER = 5;
	/**
	 * Field TYPE2_PET_WOLF. (value is 6)
	 */
	public static final int TYPE2_PET_WOLF = 6;
	/**
	 * Field TYPE2_PET_HATCHLING. (value is 7)
	 */
	public static final int TYPE2_PET_HATCHLING = 7;
	/**
	 * Field TYPE2_PET_STRIDER. (value is 8)
	 */
	public static final int TYPE2_PET_STRIDER = 8;
	/**
	 * Field TYPE2_NODROP. (value is 9)
	 */
	public static final int TYPE2_NODROP = 9;
	/**
	 * Field TYPE2_PET_GWOLF. (value is 10)
	 */
	public static final int TYPE2_PET_GWOLF = 10;
	/**
	 * Field TYPE2_PENDANT. (value is 11)
	 */
	public static final int TYPE2_PENDANT = 11;
	/**
	 * Field TYPE2_PET_BABY. (value is 12)
	 */
	public static final int TYPE2_PET_BABY = 12;
	/**
	 * Field SLOT_NONE.
	 */
	public static final int SLOT_NONE = 0x00000;
	/**
	 * Field SLOT_UNDERWEAR.
	 */
	public static final int SLOT_UNDERWEAR = 0x00001;
	/**
	 * Field SLOT_R_EAR.
	 */
	public static final int SLOT_R_EAR = 0x00002;
	/**
	 * Field SLOT_L_EAR.
	 */
	public static final int SLOT_L_EAR = 0x00004;
	/**
	 * Field SLOT_NECK.
	 */
	public static final int SLOT_NECK = 0x00008;
	/**
	 * Field SLOT_R_FINGER.
	 */
	public static final int SLOT_R_FINGER = 0x00010;
	/**
	 * Field SLOT_L_FINGER.
	 */
	public static final int SLOT_L_FINGER = 0x00020;
	/**
	 * Field SLOT_HEAD.
	 */
	public static final int SLOT_HEAD = 0x00040;
	/**
	 * Field SLOT_R_HAND.
	 */
	public static final int SLOT_R_HAND = 0x00080;
	/**
	 * Field SLOT_L_HAND.
	 */
	public static final int SLOT_L_HAND = 0x00100;
	/**
	 * Field SLOT_GLOVES.
	 */
	public static final int SLOT_GLOVES = 0x00200;
	/**
	 * Field SLOT_CHEST.
	 */
	public static final int SLOT_CHEST = 0x00400;
	/**
	 * Field SLOT_LEGS.
	 */
	public static final int SLOT_LEGS = 0x00800;
	/**
	 * Field SLOT_FEET.
	 */
	public static final int SLOT_FEET = 0x01000;
	/**
	 * Field SLOT_BACK.
	 */
	public static final int SLOT_BACK = 0x02000;
	/**
	 * Field SLOT_LR_HAND.
	 */
	public static final int SLOT_LR_HAND = 0x04000;
	/**
	 * Field SLOT_FULL_ARMOR.
	 */
	public static final int SLOT_FULL_ARMOR = 0x08000;
	/**
	 * Field SLOT_HAIR.
	 */
	public static final int SLOT_HAIR = 0x10000;
	/**
	 * Field SLOT_FORMAL_WEAR.
	 */
	public static final int SLOT_FORMAL_WEAR = 0x20000;
	/**
	 * Field SLOT_DHAIR.
	 */
	public static final int SLOT_DHAIR = 0x40000;
	/**
	 * Field SLOT_HAIRALL.
	 */
	public static final int SLOT_HAIRALL = 0x80000;
	/**
	 * Field SLOT_R_BRACELET.
	 */
	public static final int SLOT_R_BRACELET = 0x100000;
	/**
	 * Field SLOT_L_BRACELET.
	 */
	public static final int SLOT_L_BRACELET = 0x200000;
	/**
	 * Field SLOT_DECO.
	 */
	public static final int SLOT_DECO = 0x400000;
	/**
	 * Field SLOT_BELT.
	 */
	public static final int SLOT_BELT = 0x10000000;
	/**
	 * Field SLOT_WOLF. (value is -100)
	 */
	public static final int SLOT_WOLF = -100;
	/**
	 * Field SLOT_HATCHLING. (value is -101)
	 */
	public static final int SLOT_HATCHLING = -101;
	/**
	 * Field SLOT_STRIDER. (value is -102)
	 */
	public static final int SLOT_STRIDER = -102;
	/**
	 * Field SLOT_BABYPET. (value is -103)
	 */
	public static final int SLOT_BABYPET = -103;
	/**
	 * Field SLOT_GWOLF. (value is -104)
	 */
	public static final int SLOT_GWOLF = -104;
	/**
	 * Field SLOT_PENDANT. (value is -105)
	 */
	public static final int SLOT_PENDANT = -105;
	/**
	 * Field SLOTS_ARMOR.
	 */
	public static final int SLOTS_ARMOR = SLOT_HEAD | SLOT_L_HAND | SLOT_GLOVES | SLOT_CHEST | SLOT_LEGS | SLOT_FEET | SLOT_BACK | SLOT_FULL_ARMOR;
	/**
	 * Field SLOTS_JEWELRY.
	 */
	public static final int SLOTS_JEWELRY = SLOT_R_EAR | SLOT_L_EAR | SLOT_NECK | SLOT_R_FINGER | SLOT_L_FINGER;
	/**
	 * Field CRYSTAL_NONE. (value is 0)
	 */
	public static final int CRYSTAL_NONE = 0;
	/**
	 * Field CRYSTAL_D. (value is 1458)
	 */
	public static final int CRYSTAL_D = 1458;
	/**
	 * Field CRYSTAL_C. (value is 1459)
	 */
	public static final int CRYSTAL_C = 1459;
	/**
	 * Field CRYSTAL_B. (value is 1460)
	 */
	public static final int CRYSTAL_B = 1460;
	/**
	 * Field CRYSTAL_A. (value is 1461)
	 */
	public static final int CRYSTAL_A = 1461;
	/**
	 * Field CRYSTAL_S. (value is 1462)
	 */
	public static final int CRYSTAL_S = 1462;
	/**
	 * Field CRYSTAL_R. (value is 17371)
	 */
	public static final int CRYSTAL_R = 17371;
	
	/**
	 * @author Mobius
	 */
	public static enum Grade
	{
		/**
		 * Field NONE.
		 */
		NONE(CRYSTAL_NONE, 0),
		/**
		 * Field D.
		 */
		D(CRYSTAL_D, 1),
		/**
		 * Field C.
		 */
		C(CRYSTAL_C, 2),
		/**
		 * Field B.
		 */
		B(CRYSTAL_B, 3),
		/**
		 * Field A.
		 */
		A(CRYSTAL_A, 4),
		/**
		 * Field S.
		 */
		S(CRYSTAL_S, 5),
		/**
		 * Field S80.
		 */
		S80(CRYSTAL_S, 5),
		/**
		 * Field S84.
		 */
		S84(CRYSTAL_S, 5),
		/**
		 * Field R.
		 */
		R(CRYSTAL_R, 6),
		/**
		 * Field R95.
		 */
		R95(CRYSTAL_R, 6),
		/**
		 * Field R99.
		 */
		R99(CRYSTAL_R, 6);
		/**
		 * Field cry.
		 */
		public final int cry;
		/**
		 * Field externalOrdinal.
		 */
		public final int externalOrdinal;
		
		/**
		 * Constructor for Grade.
		 * @param crystal int
		 * @param ext int
		 */
		private Grade(int crystal, int ext)
		{
			cry = crystal;
			externalOrdinal = ext;
		}
	}
	
	/**
	 * Field ATTRIBUTE_NONE. (value is -2)
	 */
	public static final int ATTRIBUTE_NONE = -2;
	/**
	 * Field ATTRIBUTE_FIRE. (value is 0)
	 */
	public static final int ATTRIBUTE_FIRE = 0;
	/**
	 * Field ATTRIBUTE_WATER. (value is 1)
	 */
	public static final int ATTRIBUTE_WATER = 1;
	/**
	 * Field ATTRIBUTE_WIND. (value is 2)
	 */
	public static final int ATTRIBUTE_WIND = 2;
	/**
	 * Field ATTRIBUTE_EARTH. (value is 3)
	 */
	public static final int ATTRIBUTE_EARTH = 3;
	/**
	 * Field ATTRIBUTE_HOLY. (value is 4)
	 */
	public static final int ATTRIBUTE_HOLY = 4;
	/**
	 * Field ATTRIBUTE_DARK. (value is 5)
	 */
	public static final int ATTRIBUTE_DARK = 5;
	/**
	 * Field _itemId.
	 */
	protected final int _itemId;
	/**
	 * Field _class.
	 */
	private final ItemClass _class;
	/**
	 * Field _name.
	 */
	protected final String _name;
	/**
	 * Field _addname.
	 */
	protected final String _addname;
	/**
	 * Field _icon.
	 */
	protected final String _icon;
	/**
	 * Field _icon32.
	 */
	protected final String _icon32;
	/**
	 * Field _type1.
	 */
	protected int _type1;
	/**
	 * Field _type2.
	 */
	protected int _type2;
	/**
	 * Field _weight.
	 */
	private final int _weight;
	/**
	 * Field _crystalType.
	 */
	protected final Grade _crystalType;
	/**
	 * Field _durability.
	 */
	private final int _durability;
	/**
	 * Field _bodyPart.
	 */
	protected int _bodyPart;
	/**
	 * Field _referencePrice.
	 */
	private final int _referencePrice;
	/**
	 * Field _crystalCount.
	 */
	private final int _crystalCount;
	/**
	 * Field _temporal.
	 */
	private final boolean _temporal;
	/**
	 * Field _isBlessed.
	 */
	private final boolean _isBlessedEquipment;
	/**
	 * Field _stackable.
	 */
	private final boolean _stackable;
	/**
	 * Field _crystallizable.
	 */
	private final boolean _crystallizable;
	/**
	 * Field _flags.
	 */
	private int _flags;
	/**
	 * Field _reuseType.
	 */
	private final ReuseType _reuseType;
	/**
	 * Field _reuseDelay.
	 */
	private final int _reuseDelay;
	/**
	 * Field _reuseGroup.
	 */
	private final int _reuseGroup;
	/**
	 * Field _agathionEnergy.
	 */
	private final int _agathionEnergy;
	/**
	 * Field _skills.
	 */
	protected Skill[] _skills;
	/**
	 * Field _enchant4Skill.
	 */
	private Skill _enchant4Skill = null;
	/**
	 * Field _unequippedSkill.
	 */
	private Skill _unequipeSkill = null;
	/**
	 * Field type.
	 */
	public ItemType type;
	/**
	 * Field _exItemType.
	 */
	public ExItemType _exItemType;
	/**
	 * Field _baseAttributes.
	 */
	private int[] _baseAttributes = new int[6];
	/**
	 * Field _enchantOptions.
	 */
	private IntObjectMap<int[]> _enchantOptions = Containers.emptyIntObjectMap();
	/**
	 * Field _condition.
	 */
	private Condition _condition;
	/**
	 * Field _handler.
	 */
	private IItemHandler _handler = IItemHandler.NULL;
	/**
	 * Field _blessed.
	 */
	private final boolean _blessed;
	/**
	 * Field _capsuled.
	 */
	private final boolean _capsuled;
	
	/**
	 * Constructor for ItemTemplate.
	 * @param set StatsSet
	 */
	protected ItemTemplate(final StatsSet set)
	{
		_itemId = set.getInteger("item_id");
		_class = set.getEnum("class", ItemClass.class, ItemClass.OTHER);
		_name = set.getString("name");
		_addname = set.getString("add_name", "");
		_icon = set.getString("icon", "");
		_icon32 = "<img src=icon." + _icon + " width=32 height=32>";
		_weight = set.getInteger("weight", 0);
		_crystallizable = set.getBool("crystallizable", false);
		_stackable = set.getBool("stackable", false);
		_isBlessedEquipment = set.getBool("isBlessed", false);
		_crystalType = set.getEnum("crystal_type", Grade.class, Grade.NONE);
		_durability = set.getInteger("durability", -1);
		_temporal = set.getBool("temporal", false);
		_bodyPart = set.getInteger("bodypart", 0);
		_referencePrice = set.getInteger("price", 0);
		_crystalCount = set.getInteger("crystal_count", 0);
		_reuseType = set.getEnum("reuse_type", ReuseType.class, ReuseType.NORMAL);
		_reuseDelay = set.getInteger("reuse_delay", 0);
		_reuseGroup = set.getInteger("delay_share_group", -_itemId);
		_agathionEnergy = set.getInteger("agathion_energy", 0);
		_exItemType = set.getEnum("ex_type", ExItemType.class, ExItemType.OTHER_ITEMS);
		_blessed = set.getBool("blessed", false);
		_capsuled = set.getBool("capsuled", false);
		for (ItemFlags f : ItemFlags.VALUES)
		{
			boolean flag = set.getBool(f.name().toLowerCase(), f.getDefaultValue());
			if (flag)
			{
				activeFlag(f);
			}
		}
		_funcTemplates = FuncTemplate.EMPTY_ARRAY;
		_skills = Skill.EMPTY_ARRAY;
	}
	
	/**
	 * Method getItemType.
	 * @return ItemType
	 */
	public ItemType getItemType()
	{
		return type;
	}
	
	/**
	 * Method getExItemType.
	 * @return ExItemType
	 */
	public ExItemType getExItemType()
	{
		return _exItemType;
	}
	
	/**
	 * Method getIcon.
	 * @return String
	 */
	public String getIcon()
	{
		return _icon;
	}
	
	/**
	 * Method getIcon32.
	 * @return String
	 */
	public String getIcon32()
	{
		return _icon32;
	}
	
	/**
	 * Method getDurability.
	 * @return int
	 */
	public final int getDurability()
	{
		return _durability;
	}
	
	/**
	 * Method isTemporal.
	 * @return boolean
	 */
	public final boolean isTemporal()
	{
		return _temporal;
	}

	
	/**
	 * Method isTemporal.
	 * @return boolean
	 */
	public final boolean isBlessedEquipment()
	{
		return _isBlessedEquipment;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public final int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getItemMask.
	 * @return long
	 */
	public abstract long getItemMask();
	
	/**
	 * Method getType2.
	 * @return int
	 */
	public final int getType2()
	{
		return _type2;
	}
	
	/**
	 * Method getBaseAttributeValue.
	 * @param element Element
	 * @return int
	 */
	public final int getBaseAttributeValue(Element element)
	{
		if (element == Element.NONE)
		{
			return 0;
		}
		return _baseAttributes[element.getId()];
	}
	
	/**
	 * Method setBaseAtributeElements.
	 * @param val int[]
	 */
	public void setBaseAtributeElements(int[] val)
	{
		_baseAttributes = val;
	}
	
	/**
	 * Method getType2ForPackets.
	 * @return int
	 */
	public final int getType2ForPackets()
	{
		int type2 = _type2;
		switch (_type2)
		{
			case TYPE2_PET_WOLF:
			case TYPE2_PET_HATCHLING:
			case TYPE2_PET_STRIDER:
			case TYPE2_PET_GWOLF:
			case TYPE2_PET_BABY:
				if (_bodyPart == ItemTemplate.SLOT_CHEST)
				{
					type2 = TYPE2_SHIELD_ARMOR;
				}
				else
				{
					type2 = TYPE2_WEAPON;
				}
				break;
			case TYPE2_PENDANT:
				type2 = TYPE2_ACCESSORY;
				break;
		}
		return type2;
	}
	
	/**
	 * Method getWeight.
	 * @return int
	 */
	public final int getWeight()
	{
		return _weight;
	}
	
	/**
	 * Method isCrystallizable.
	 * @return boolean
	 */
	public final boolean isCrystallizable()
	{
		return _crystallizable && !isStackable() && (getCrystalType() != Grade.NONE) && (getCrystalCount() > 0);
	}
	
	/**
	 * Method getCrystalType.
	 * @return Grade
	 */
	public final Grade getCrystalType()
	{
		return _crystalType;
	}
	
	/**
	 * Method getItemGrade.
	 * @return Grade
	 */
	public final Grade getItemGrade()
	{
		return getCrystalType();
	}
	
	/**
	 * Method getCrystalCount.
	 * @return int
	 */
	public final int getCrystalCount()
	{
		return _crystalCount;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * Method getAdditionalName.
	 * @return String
	 */
	public final String getAdditionalName()
	{
		return _addname;
	}
	
	/**
	 * Method getBodyPart.
	 * @return int
	 */
	public final int getBodyPart()
	{
		return _bodyPart;
	}
	
	/**
	 * Method getType1.
	 * @return int
	 */
	public final int getType1()
	{
		return _type1;
	}
	
	/**
	 * Method isStackable.
	 * @return boolean
	 */
	public final boolean isStackable()
	{
		return _stackable;
	}
	
	/**
	 * Method getReferencePrice.
	 * @return int
	 */
	public final int getReferencePrice()
	{
		return _referencePrice;
	}
	
	/**
	 * Method isForHatchling.
	 * @return boolean
	 */
	public boolean isForHatchling()
	{
		return _type2 == TYPE2_PET_HATCHLING;
	}
	
	/**
	 * Method isForStrider.
	 * @return boolean
	 */
	public boolean isForStrider()
	{
		return _type2 == TYPE2_PET_STRIDER;
	}
	
	/**
	 * Method isForWolf.
	 * @return boolean
	 */
	public boolean isForWolf()
	{
		return _type2 == TYPE2_PET_WOLF;
	}
	
	/**
	 * Method isForPetBaby.
	 * @return boolean
	 */
	public boolean isForPetBaby()
	{
		return _type2 == TYPE2_PET_BABY;
	}
	
	/**
	 * Method isForGWolf.
	 * @return boolean
	 */
	public boolean isForGWolf()
	{
		return _type2 == TYPE2_PET_GWOLF;
	}
	
	/**
	 * Method isPendant.
	 * @return boolean
	 */
	public boolean isPendant()
	{
		return _type2 == TYPE2_PENDANT;
	}
	
	/**
	 * Method isForPet.
	 * @return boolean
	 */
	public boolean isForPet()
	{
		return (_type2 == TYPE2_PENDANT) || (_type2 == TYPE2_PET_HATCHLING) || (_type2 == TYPE2_PET_WOLF) || (_type2 == TYPE2_PET_STRIDER) || (_type2 == TYPE2_PET_GWOLF) || (_type2 == TYPE2_PET_BABY);
	}
	
	/**
	 * Method attachSkill.
	 * @param skill Skill
	 */
	public void attachSkill(Skill skill)
	{
		_skills = ArrayUtils.add(_skills, skill);
	}
	
	/**
	 * Method getAttachedSkills.
	 * @return Skill[]
	 */
	public Skill[] getAttachedSkills()
	{
		return _skills;
	}
	
	/**
	 * Method getFirstSkill.
	 * @return Skill
	 */
	public Skill getFirstSkill()
	{
		if (_skills.length > 0)
		{
			return _skills[0];
		}
		return null;
	}
	
	/**
	 * Method getEnchant4Skill.
	 * @return Skill
	 */
	public Skill getEnchant4Skill()
	{
		return _enchant4Skill;
	}

	/**
	 * Method getEnchant4Skill.
	 * @return Skill
	 */
	public Skill getUnequipeSkill()
	{
		return _unequipeSkill;
	}

	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return _itemId + " " + _name;
	}
	
	/**
	 * Method isShadowItem.
	 * @return boolean
	 */
	public boolean isShadowItem()
	{
		return (_durability > 0) && !isTemporal();
	}
	
	/**
	 * Method isCommonItem.
	 * @return boolean
	 */
	public boolean isCommonItem()
	{
		return _name.startsWith("Common Item - ");
	}
	
	/**
	 * Method isSealedItem.
	 * @return boolean
	 */
	public boolean isSealedItem()
	{
		return _name.startsWith("Sealed");
	}
	
	/**
	 * Method isAltSeed.
	 * @return boolean
	 */
	public boolean isAltSeed()
	{
		return _name.contains("Alternative");
	}
	
	/**
	 * Method getItemClass.
	 * @return ItemClass
	 */
	public ItemClass getItemClass()
	{
		return _class;
	}
	
	/**
	 * Method isAdena.
	 * @return boolean
	 */
	public boolean isAdena()
	{
		return (_itemId == 57) || (_itemId == 6360) || (_itemId == 6361) || (_itemId == 6362);
	}
	
	/**
	 * Method isEquipment.
	 * @return boolean
	 */
	public boolean isEquipment()
	{
		return _type1 != TYPE1_ITEM_QUESTITEM_ADENA;
	}
	
	/**
	 * Method isKeyMatherial.
	 * @return boolean
	 */
	public boolean isKeyMatherial()
	{
		return _class == ItemClass.PIECES;
	}
	
	/**
	 * Method isRecipe.
	 * @return boolean
	 */
	public boolean isRecipe()
	{
		return _class == ItemClass.RECIPIES;
	}
	
	/**
	 * Method isTerritoryAccessory.
	 * @return boolean
	 */
	public boolean isTerritoryAccessory()
	{
		return ((_itemId >= 13740) && (_itemId <= 13748)) || ((_itemId >= 14592) && (_itemId <= 14600)) || ((_itemId >= 14664) && (_itemId <= 14672)) || ((_itemId >= 14801) && (_itemId <= 14809)) || ((_itemId >= 15282) && (_itemId <= 15299));
	}
	
	/**
	 * Method isArrow.
	 * @return boolean
	 */
	public boolean isArrow()
	{
		return (type == EtcItemType.ARROW) || (type == EtcItemType.UNLIMITED_ARROW);
	}
	
	/**
	 * Method isBelt.
	 * @return boolean
	 */
	public boolean isBelt()
	{
		return _bodyPart == SLOT_BELT;
	}
	
	/**
	 * Method isBracelet.
	 * @return boolean
	 */
	public boolean isBracelet()
	{
		return (_bodyPart == SLOT_R_BRACELET) || (_bodyPart == SLOT_L_BRACELET);
	}
	
	/**
	 * Method isUnderwear.
	 * @return boolean
	 */
	public boolean isUnderwear()
	{
		return _bodyPart == SLOT_UNDERWEAR;
	}
	
	/**
	 * Method isCloak.
	 * @return boolean
	 */
	public boolean isCloak()
	{
		return _bodyPart == SLOT_BACK;
	}
	
	/**
	 * Method isTalisman.
	 * @return boolean
	 */
	public boolean isTalisman()
	{
		return _bodyPart == SLOT_DECO;
	}
	
	/**
	 * Method isHerb.
	 * @return boolean
	 */
	public boolean isHerb()
	{
		return type == EtcItemType.HERB;
	}
	
	/**
	 * Method isHeroWeapon.
	 * @return boolean
	 */
	public boolean isHeroWeapon()
	{
		return ((_itemId >= 6611) && (_itemId <= 6621)) || ((_itemId >= 9388) && (_itemId <= 9390));
	}
	
	/**
	 * Method isCursed.
	 * @return boolean
	 */
	public boolean isCursed()
	{
		return CursedWeaponsManager.getInstance().isCursed(_itemId);
	}
	
	/**
	 * Method isMercenaryTicket.
	 * @return boolean
	 */
	public boolean isMercenaryTicket()
	{
		return type == EtcItemType.MERCENARY_TICKET;
	}
	
	/**
	 * Method isTerritoryFlag.
	 * @return boolean
	 */
	public boolean isTerritoryFlag()
	{
		return (_itemId == 13560) || (_itemId == 13561) || (_itemId == 13562) || (_itemId == 13563) || (_itemId == 13564) || (_itemId == 13565) || (_itemId == 13566) || (_itemId == 13567) || (_itemId == 13568);
	}
	
	/**
	 * Method isRod.
	 * @return boolean
	 */
	public boolean isRod()
	{
		return getItemType() == WeaponType.ROD;
	}
	
	/**
	 * Method isWeapon.
	 * @return boolean
	 */
	public boolean isWeapon()
	{
		return getType2() == ItemTemplate.TYPE2_WEAPON;
	}
	
	/**
	 * Method isArmor.
	 * @return boolean
	 */
	public boolean isArmor()
	{
		return getType2() == ItemTemplate.TYPE2_SHIELD_ARMOR;
	}
	
	/**
	 * Method isAccessory.
	 * @return boolean
	 */
	public boolean isAccessory()
	{
		return getType2() == ItemTemplate.TYPE2_ACCESSORY;
	}
	
	/**
	 * Method isQuest.
	 * @return boolean
	 */
	public boolean isQuest()
	{
		return getType2() == ItemTemplate.TYPE2_QUEST;
	}
	
	/**
	 * Method canBeEnchanted.
	 * @return boolean
	 */
	public boolean canBeEnchanted()
	{
		if (getItemGrade() == Grade.NONE)
		{
			return false;
		}
		if (isCursed())
		{
			return false;
		}
		if (isQuest())
		{
			return false;
		}
		return isEnchantable();
	}
	
	/**
	 * Method isEquipable.
	 * @return boolean
	 */
	public boolean isEquipable()
	{
		return (getItemType() == EtcItemType.BAIT) || (getItemType() == EtcItemType.ARROW) || (getItemType() == EtcItemType.UNLIMITED_ARROW) || (getItemType() == EtcItemType.BOLT) || !((getBodyPart() == 0) || (this instanceof EtcItemTemplate));
	}
	
	/**
	 * Method setEnchant4Skill.
	 * @param enchant4Skill Skill
	 */
	public void setEnchant4Skill(Skill enchant4Skill)
	{
		_enchant4Skill = enchant4Skill;
	}
	
	/**
	 * Method setEnchant4Skill.
	 * @param enchant4Skill Skill
	 */
	public void setUnequipeSkill(Skill unequipeSkill)
	{
		_unequipeSkill = unequipeSkill;
	}	
	
	/**
	 * Method testCondition.
	 * @param player Playable
	 * @param instance ItemInstance
	 * @return boolean
	 */
	public boolean testCondition(Playable player, ItemInstance instance)
	{
		if (_condition == null)
		{
			return true;
		}
		Env env = new Env();
		env.character = player;
		env.item = instance;
		boolean res = _condition.test(env);
		if (!res && (_condition.getSystemMsg() != null))
		{
			if (_condition.getSystemMsg().size() > 0)
			{
				player.sendPacket(new SystemMessage2(_condition.getSystemMsg()).addItemName(getItemId()));
			}
			else
			{
				player.sendPacket(_condition.getSystemMsg());
			}
		}
		return res;
	}
	
	/**
	 * Method setCondition.
	 * @param condition Condition
	 */
	public void setCondition(Condition condition)
	{
		_condition = condition;
	}
	
	/**
	 * Method isEnchantable.
	 * @return boolean
	 */
	public boolean isEnchantable()
	{
		return hasFlag(ItemFlags.ENCHANTABLE);
	}
	
	/**
	 * Method isTradeable.
	 * @return boolean
	 */
	public boolean isTradeable()
	{
		return hasFlag(ItemFlags.TRADEABLE);
	}
	
	/**
	 * Method isDestroyable.
	 * @return boolean
	 */
	public boolean isDestroyable()
	{
		return hasFlag(ItemFlags.DESTROYABLE);
	}
	
	/**
	 * Method isDropable.
	 * @return boolean
	 */
	public boolean isDropable()
	{
		return hasFlag(ItemFlags.DROPABLE);
	}
	
	/**
	 * Method isSellable.
	 * @return boolean
	 */
	public final boolean isSellable()
	{
		return hasFlag(ItemFlags.SELLABLE);
	}
	
	/**
	 * Method isAugmentable.
	 * @return boolean
	 */
	public final boolean isAugmentable()
	{
		return hasFlag(ItemFlags.AUGMENTABLE);
	}
	
	/**
	 * Method isAttributable.
	 * @return boolean
	 */
	public final boolean isAttributable()
	{
		return hasFlag(ItemFlags.ATTRIBUTABLE);
	}
	
	/**
	 * Method isStoreable.
	 * @return boolean
	 */
	public final boolean isStoreable()
	{
		return hasFlag(ItemFlags.STOREABLE);
	}
	
	/**
	 * Method isFreightable.
	 * @return boolean
	 */
	public final boolean isFreightable()
	{
		return hasFlag(ItemFlags.FREIGHTABLE);
	}
	
	/**
	 * Method hasFlag.
	 * @param f ItemFlags
	 * @return boolean
	 */
	public boolean hasFlag(ItemFlags f)
	{
		return (_flags & f.mask()) == f.mask();
	}
	
	/**
	 * Method activeFlag.
	 * @param f ItemFlags
	 */
	private void activeFlag(ItemFlags f)
	{
		_flags |= f.mask();
	}
	
	/**
	 * Method getHandler.
	 * @return IItemHandler
	 */
	public IItemHandler getHandler()
	{
		return _handler;
	}
	
	/**
	 * Method setHandler.
	 * @param handler IItemHandler
	 */
	public void setHandler(IItemHandler handler)
	{
		_handler = handler;
	}
	
	/**
	 * Method getReuseDelay.
	 * @return int
	 */
	public int getReuseDelay()
	{
		return _reuseDelay;
	}
	
	/**
	 * Method getReuseGroup.
	 * @return int
	 */
	public int getReuseGroup()
	{
		return _reuseGroup;
	}
	
	/**
	 * Method getDisplayReuseGroup.
	 * @return int
	 */
	public int getDisplayReuseGroup()
	{
		return _reuseGroup < 0 ? -1 : _reuseGroup;
	}
	
	/**
	 * Method getAgathionEnergy.
	 * @return int
	 */
	public int getAgathionEnergy()
	{
		return _agathionEnergy;
	}
	
	/**
	 * Method addEnchantOptions.
	 * @param level int
	 * @param options int[]
	 */
	public void addEnchantOptions(int level, int[] options)
	{
		if (_enchantOptions.isEmpty())
		{
			_enchantOptions = new HashIntObjectMap<int[]>();
		}
		_enchantOptions.put(level, options);
	}
	
	/**
	 * Method getEnchantOptions.
	 * @return IntObjectMap<int[]>
	 */
	public IntObjectMap<int[]> getEnchantOptions()
	{
		return _enchantOptions;
	}
	
	/**
	 * Method getReuseType.
	 * @return ReuseType
	 */
	public ReuseType getReuseType()
	{
		return _reuseType;
	}
	
	/**
	 * Method isBlessed.
	 * @return boolean
	 */
	public boolean isBlessed()
	{
		return _blessed;
	}
	
	/**
	 * Method isCapsuled.
	 * @return boolean
	 */
	public boolean isCapsuled()
	{
		return _capsuled;
	}
	
	/**
	 * Method isMagicWeapon.
	 * @return boolean
	 */
	public boolean isMagicWeapon()
	{
		return false;
	}
}
