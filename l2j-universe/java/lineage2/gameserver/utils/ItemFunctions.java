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
package lineage2.gameserver.utils;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.model.items.attachment.PickableAttachment;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.templates.item.ArmorTemplate.ArmorType;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ItemFunctions
{
	/**
	 * Constructor for ItemFunctions.
	 */
	private ItemFunctions()
	{
	}
	
	/**
	 * Method createItem.
	 * @param itemId int
	 * @return ItemInstance
	 */
	public static ItemInstance createItem(int itemId)
	{
		ItemInstance item = new ItemInstance(IdFactory.getInstance().getNextId(), itemId);
		item.setLocation(ItemLocation.VOID);
		item.setCount(1L);
		return item;
	}
	
	/**
	 * Method addItem.
	 * @param playable Playable
	 * @param itemId int
	 * @param count long
	 * @param notify boolean
	 */
	public static void addItem(Playable playable, int itemId, long count, boolean notify)
	{
		if ((playable == null) || (count < 1))
		{
			return;
		}
		Playable player;
		if (playable.isServitor())
		{
			player = playable.getPlayer();
		}
		else
		{
			player = playable;
		}
		ItemTemplate t = ItemHolder.getInstance().getTemplate(itemId);
		if (t.isStackable())
		{
			player.getInventory().addItem(itemId, count);
		}
		else
		{
			for (long i = 0; i < count; i++)
			{
				player.getInventory().addItem(itemId, 1);
			}
		}
		if (notify)
		{
			player.sendPacket(SystemMessage2.obtainItems(itemId, count, 0));
		}
	}
	
	/**
	 * Method getItemCount.
	 * @param playable Playable
	 * @param itemId int
	 * @return long
	 */
	public static long getItemCount(Playable playable, int itemId)
	{
		if (playable == null)
		{
			return 0;
		}
		Playable player = playable.getPlayer();
		return player.getInventory().getCountOf(itemId);
	}
	
	/**
	 * Method removeItem.
	 * @param playable Playable
	 * @param itemId int
	 * @param count long
	 * @param notify boolean
	 * @return long
	 */
	public static long removeItem(Playable playable, int itemId, long count, boolean notify)
	{
		long removed = 0;
		if ((playable == null) || (count < 1))
		{
			return removed;
		}
		Playable player = playable.getPlayer();
		ItemTemplate t = ItemHolder.getInstance().getTemplate(itemId);
		if (t.isStackable())
		{
			if (player.getInventory().destroyItemByItemId(itemId, count))
			{
				removed = count;
			}
		}
		else
		{
			for (long i = 0; i < count; i++)
			{
				if (player.getInventory().destroyItemByItemId(itemId, 1))
				{
					removed++;
				}
			}
		}
		if ((removed > 0) && notify)
		{
			player.sendPacket(SystemMessage2.removeItems(itemId, removed));
		}
		return removed;
	}
	
	/**
	 * Method isClanApellaItem.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isClanApellaItem(int itemId)
	{
		return ((itemId >= 7860) && (itemId <= 7879)) || ((itemId >= 9830) && (itemId <= 9839));
	}
	
	/**
	 * Method checkIfCanEquip.
	 * @param pet PetInstance
	 * @param item ItemInstance
	 * @return SystemMessage
	 */
	public final static SystemMessage checkIfCanEquip(PetInstance pet, ItemInstance item)
	{
		if (!item.isEquipable())
		{
			return Msg.ITEM_NOT_AVAILABLE_FOR_PETS;
		}
		int petId = pet.getNpcId();
		if (item.getTemplate().isPendant() || (PetDataTable.isWolf(petId) && item.getTemplate().isForWolf()) || (PetDataTable.isHatchling(petId) && item.getTemplate().isForHatchling()) || (PetDataTable.isStrider(petId) && item.getTemplate().isForStrider()) || (PetDataTable.isGWolf(petId) && item.getTemplate().isForGWolf()) || (PetDataTable.isBabyPet(petId) && item.getTemplate().isForPetBaby()) || (PetDataTable.isImprovedBabyPet(petId) && item.getTemplate().isForPetBaby()))
		{
			return null;
		}
		return Msg.ITEM_NOT_AVAILABLE_FOR_PETS;
	}
	
	/**
	 * Method checkIfCanEquip.
	 * @param player Player
	 * @param item ItemInstance
	 * @return L2GameServerPacket
	 */
	public final static L2GameServerPacket checkIfCanEquip(Player player, ItemInstance item)
	{
		int itemId = item.getItemId();
		int targetSlot = item.getTemplate().getBodyPart();
		Clan clan = player.getClan();
		if ((item.isHeroWeapon() || (item.getItemId() == 6842)) && !player.isHero())
		{
			return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
		}
		if (!player.isAwaking())
		{
			if ((player.getRace() == Race.kamael) && ((item.getItemType() == ArmorType.HEAVY) || (item.getItemType() == ArmorType.MAGIC) || (item.getItemType() == ArmorType.SIGIL) || (item.getItemType() == WeaponType.NONE)))
			{
				return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
			}
			if ((player.getRace() != Race.kamael) && ((item.getItemType() == WeaponType.CROSSBOW) || (item.getItemType() == WeaponType.RAPIER) || (item.getItemType() == WeaponType.ANCIENTSWORD)))
			{
				return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
			}
		}
		if ((itemId >= 7850) && (itemId <= 7859) && (player.getLvlJoinedAcademy() == 0))
		{
			return Msg.THIS_ITEM_CAN_ONLY_BE_WORN_BY_A_MEMBER_OF_THE_CLAN_ACADEMY;
		}
		if (isClanApellaItem(itemId) && (player.getPledgeClass() < Player.RANK_WISEMAN))
		{
			return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
		}
		if ((item.getItemType() == WeaponType.DUALDAGGER) && ((player.getSkillLevel(923) < 1) && (player.getSkillLevel(10502) < 1)))
		{
			return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
		}
		if (ArrayUtils.contains(ItemTemplate.ITEM_ID_CASTLE_CIRCLET, itemId) && ((clan == null) || (itemId != ItemTemplate.ITEM_ID_CASTLE_CIRCLET[clan.getCastle()])))
		{
			return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
		}
		if ((itemId == 6841) && ((clan == null) || !player.isClanLeader() || (clan.getCastle() == 0)))
		{
			return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
		}
		if ((targetSlot == ItemTemplate.SLOT_LR_HAND) || (targetSlot == ItemTemplate.SLOT_L_HAND) || (targetSlot == ItemTemplate.SLOT_R_HAND))
		{
			if ((itemId != player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND)) && CursedWeaponsManager.getInstance().isCursed(player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND)))
			{
				return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
			}
			if (player.isCursedWeaponEquipped() && (itemId != player.getCursedWeaponEquippedId()))
			{
				return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
			}
		}
		if (item.getTemplate().isCloak())
		{
			if (item.getName().contains("Knight") && ((player.getPledgeClass() < Player.RANK_KNIGHT) || (player.getCastle() == null)))
			{
				return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
			}
			if (item.getName().contains("Kamael") && (player.getRace() != Race.kamael))
			{
				return Msg.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM;
			}
			if (!player.getOpenCloak())
			{
				return Msg.THE_CLOAK_CANNOT_BE_EQUIPPED_BECAUSE_A_NECESSARY_ITEM_IS_NOT_EQUIPPED;
			}
		}
		if (targetSlot == ItemTemplate.SLOT_DECO)
		{
			int count = player.getTalismanCount();
			if (count <= 0)
			{
				return new SystemMessage2(SystemMsg.YOU_CANNOT_WEAR_S1_BECAUSE_YOU_ARE_NOT_WEARING_A_BRACELET).addItemName(itemId);
			}
			ItemInstance deco;
			for (int slot = Inventory.PAPERDOLL_DECO1; slot <= Inventory.PAPERDOLL_DECO6; slot++)
			{
				deco = player.getInventory().getPaperdollItem(slot);
				if (deco != null)
				{
					if (deco == item)
					{
						return null;
					}
					if ((--count <= 0) || (deco.getItemId() == itemId))
					{
						return new SystemMessage2(SystemMsg.YOU_CANNOT_EQUIP_S1_BECAUSE_YOU_DO_NOT_HAVE_ANY_AVAILABLE_SLOTS).addItemName(itemId);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Method checkIfCanPickup.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @return boolean
	 */
	public static boolean checkIfCanPickup(Playable playable, ItemInstance item)
	{
		Player player = playable.getPlayer();
		return (item.getDropTimeOwner() <= System.currentTimeMillis()) || item.getDropPlayers().contains(player.getObjectId());
	}
	
	/**
	 * Method canAddItem.
	 * @param player Player
	 * @param item ItemInstance
	 * @return boolean
	 */
	public static boolean canAddItem(Player player, ItemInstance item)
	{
		if (!player.getInventory().validateWeight(item))
		{
			player.sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
			return false;
		}
		if (!player.getInventory().validateCapacity(item))
		{
			player.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
			return false;
		}
		if (!item.getTemplate().getHandler().pickupItem(player, item))
		{
			return false;
		}
		PickableAttachment attachment = item.getAttachment() instanceof PickableAttachment ? (PickableAttachment) item.getAttachment() : null;
		if ((attachment != null) && !attachment.canPickUp(player))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method checkIfCanDiscard.
	 * @param player Player
	 * @param item ItemInstance
	 * @return boolean
	 */
	public final static boolean checkIfCanDiscard(Player player, ItemInstance item)
	{
		if (item.isHeroWeapon())
		{
			return false;
		}
		if (PetDataTable.isPetControlItem(item) && player.isMounted())
		{
			return false;
		}
		if (player.getPetControlItem() == item)
		{
			return false;
		}
		if (player.getEnchantScroll() == item)
		{
			return false;
		}
		if (item.isCursed())
		{
			return false;
		}
		if (item.getTemplate().isQuest())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method isBlessedEnchantScroll.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isBlessedEnchantScroll(int itemId)
	{
		switch (itemId)
		{
			case 6575:
			case 6576:
			case 6573:
			case 6574:
			case 6571:
			case 6572:
			case 6569:
			case 6570:
			case 6577:
			case 6578:
			case 19447:
			case 19448:
			case 21582:
				return true;
		}
		return false;
	}
	
	/**
	 * Method isAncientEnchantScroll.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isAncientEnchantScroll(int itemId)
	{
		switch (itemId)
		{
			case 22014:
			case 22016:
			case 22015:
			case 22017:
			case 20519:
			case 20520:
				return true;
		}
		return false;
	}
	
	/**
	 * Method isDestructionWpnEnchantScroll.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isDestructionWpnEnchantScroll(int itemId)
	{
		switch (itemId)
		{
			case 22221:
			case 22223:
			case 22225:
			case 22227:
			case 22229:
				return true;
		}
		return false;
	}
	
	/**
	 * Method isDestructionArmEnchantScroll.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isDestructionArmEnchantScroll(int itemId)
	{
		switch (itemId)
		{
			case 22222:
			case 22224:
			case 22226:
			case 22228:
			case 22230:
				return true;
		}
		return false;
	}
	
	/**
	 * Method isItemMallEnchantScroll.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isItemMallEnchantScroll(int itemId)
	{
		switch (itemId)
		{
			case 22006:
			case 22010:
			case 22007:
			case 22011:
			case 22008:
			case 22012:
			case 22009:
			case 22013:
			case 20517:
			case 20518:
				return true;
			default:
				return isAncientEnchantScroll(itemId);
		}
	}
	
	/**
	 * Method isDivineEnchantScroll.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isDivineEnchantScroll(int itemId)
	{
		switch (itemId)
		{
			case 22018:
			case 22020:
			case 22019:
			case 22021:
			case 20521:
			case 20522:
				return true;
		}
		return false;
	}
	
	/**
	 * Method isCrystallEnchantScroll.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isCrystallEnchantScroll(int itemId)
	{
		switch (itemId)
		{
			case 957:
			case 958:
			case 953:
			case 954:
			case 949:
			case 950:
			case 731:
			case 732:
			case 961:
			case 962:
				return true;
		}
		return false;
	}
	
	/**
	 * Method isGemstones.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isGemstones(int itemId)
	{
		switch (itemId)
		{
			case 2130:
			case 2131:
			case 2132:
			case 2133:
			case 2134:
			case 19440:
				return true;
		}
		return false;
	}
	
	/**
	 * Method getEnchantCrystalId.
	 * @param item ItemInstance
	 * @param scroll ItemInstance
	 * @param catalyst ItemInstance
	 * @return int
	 */
	public final static int getEnchantCrystalId(ItemInstance item, ItemInstance scroll, ItemInstance catalyst)
	{
		boolean scrollValid = false, catalystValid = false;
		for (int scrollId : getEnchantScrollId(item))
		{
			if (scroll.getItemId() == scrollId)
			{
				scrollValid = true;
				break;
			}
		}
		if (catalyst == null)
		{
			catalystValid = true;
		}
		else
		{
			for (int catalystId : getEnchantCatalystId(item))
			{
				if (catalystId == catalyst.getItemId())
				{
					catalystValid = true;
					break;
				}
			}
		}
		if (scrollValid && catalystValid)
		{
			switch (item.getCrystalType().cry)
			{
				case ItemTemplate.CRYSTAL_NONE:
					return 0;
				case ItemTemplate.CRYSTAL_D:
					return 1458;
				case ItemTemplate.CRYSTAL_C:
					return 1459;
				case ItemTemplate.CRYSTAL_B:
					return 1460;
				case ItemTemplate.CRYSTAL_A:
					return 1461;
				case ItemTemplate.CRYSTAL_S:
					return 1462;
				case ItemTemplate.CRYSTAL_R:
					return 17371;
			}
		}
		return -1;
	}
	
	/**
	 * Method getEnchantScrollId.
	 * @param item ItemInstance
	 * @return int[]
	 */
	public final static int[] getEnchantScrollId(ItemInstance item)
	{
		if (item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON)
		{
			switch (item.getCrystalType().cry)
			{
				case ItemTemplate.CRYSTAL_NONE:
					return new int[]
					{
						13540
					};
				case ItemTemplate.CRYSTAL_D:
					return new int[]
					{
						955,
						6575,
						957,
						22006,
						22229
					};
				case ItemTemplate.CRYSTAL_C:
					return new int[]
					{
						951,
						6573,
						953,
						22007,
						22227
					};
				case ItemTemplate.CRYSTAL_B:
					return new int[]
					{
						947,
						6571,
						949,
						22008,
						22014,
						22018,
						22225
					};
				case ItemTemplate.CRYSTAL_A:
					return new int[]
					{
						729,
						6569,
						731,
						22009,
						22015,
						22019,
						22223
					};
				case ItemTemplate.CRYSTAL_S:
					return new int[]
					{
						959,
						6577,
						961,
						20517,
						20519,
						20521,
						22221
					};
				case ItemTemplate.CRYSTAL_R:
					return new int[]
					{
						17526,
						19447
					};
			}
		}
		else if ((item.getTemplate().getType2() == ItemTemplate.TYPE2_SHIELD_ARMOR) || (item.getTemplate().getType2() == ItemTemplate.TYPE2_ACCESSORY))
		{
			switch (item.getCrystalType().cry)
			{
				case ItemTemplate.CRYSTAL_NONE:
					return new int[]
					{
						21581,
						21582
					};
				case ItemTemplate.CRYSTAL_D:
					return new int[]
					{
						956,
						6576,
						958,
						22010,
						22230
					};
				case ItemTemplate.CRYSTAL_C:
					return new int[]
					{
						952,
						6574,
						954,
						22011,
						22228
					};
				case ItemTemplate.CRYSTAL_B:
					return new int[]
					{
						948,
						6572,
						950,
						22012,
						22016,
						22020,
						22226
					};
				case ItemTemplate.CRYSTAL_A:
					return new int[]
					{
						730,
						6570,
						732,
						22013,
						22017,
						22021,
						22224
					};
				case ItemTemplate.CRYSTAL_S:
					return new int[]
					{
						960,
						6578,
						962,
						20518,
						20520,
						20522,
						22222
					};
				case ItemTemplate.CRYSTAL_R:
					return new int[]
					{
						17527,
						19448
					};
			}
		}
		return new int[0];
	}
	
	/**
	 * Field catalyst.
	 */
	public static final int[][] catalyst =
	{
		//WP D
		{
			12362,
			14078,
			14702
		},
		//WP C
		{
			12363,
			14079,
			14703
		},
		//WP B
		{
			12364,
			14080,
			14704
		},
		//WP A
		{
			12365,
			14081,
			14705
		},
		//WP S
		{
			12366,
			14082,
			14706
		},
		//AM D
		{
			12367,
			14083,
			14707
		},
		//AM C
		{
			12368,
			14084,
			14708
		},
		//AM B
		{
			12369,
			14085,
			14709
		},
		//AM A
		{
			12370,
			14086,
			14710
		},
		//AM S
		{
			12371,
			14087,
			14711
		},
		
		//WP R
		{
			23347,
			23599,
			30381,
			30855
		},
		//AM R
		{
			23348,
			23600,
			30382,
			33861
		}
	};
	
	/**
	 * Method getEnchantCatalystId.
	 * @param item ItemInstance
	 * @return int[]
	 */
	public final static int[] getEnchantCatalystId(ItemInstance item)
	{
		if (item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON)
		{
			switch (item.getCrystalType().cry)
			{
				case ItemTemplate.CRYSTAL_R:
					return catalyst[10];
				case ItemTemplate.CRYSTAL_S:
					return catalyst[4];
				case ItemTemplate.CRYSTAL_A:
					return catalyst[3];
				case ItemTemplate.CRYSTAL_B:
					return catalyst[2];
				case ItemTemplate.CRYSTAL_C:
					return catalyst[1];
				case ItemTemplate.CRYSTAL_D:
					return catalyst[0];
			}
		}
		else if ((item.getTemplate().getType2() == ItemTemplate.TYPE2_SHIELD_ARMOR) || (item.getTemplate().getType2() == ItemTemplate.TYPE2_ACCESSORY))
		{
			switch (item.getCrystalType().cry)
			{
				case ItemTemplate.CRYSTAL_R:
					return catalyst[11];
				case ItemTemplate.CRYSTAL_S:
					return catalyst[9];
				case ItemTemplate.CRYSTAL_A:
					return catalyst[8];
				case ItemTemplate.CRYSTAL_B:
					return catalyst[7];
				case ItemTemplate.CRYSTAL_C:
					return catalyst[6];
				case ItemTemplate.CRYSTAL_D:
					return catalyst[5];
			}
		}
		return new int[]
		{
			0,
			0,
			0
		};
	}
	
	/**
	 * Method getCatalystPower.
	 * @param itemId int
	 * @return int
	 */
	public final static int getCatalystPower(int itemId)
	{
		for (int i = 0; i < catalyst.length; i++)
		{
			for (int id : catalyst[i])
			{
				if (id == itemId)
				{
					switch (i)
					{
						case 0:
							return 20;
						case 1:
							return 18;
						case 2:
							return 15;
						case 3:
							return 12;
						case 4:
							return 10;
						case 10:
							return 10;
						case 5:
							return 35;
						case 6:
							return 27;
						case 7:
							return 23;
						case 8:
							return 18;
						case 9:
							return 15;
						case 11:
							return 15;
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * Method checkCatalyst.
	 * @param item ItemInstance
	 * @param catalyst ItemInstance
	 * @return boolean
	 */
	public static final boolean checkCatalyst(ItemInstance item, ItemInstance catalyst)
	{
		if ((item == null) || (catalyst == null))
		{
			return false;
		}
		int current = item.getEnchantLevel();
		if ((current < (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR ? 4 : 3)) || (current > 8))
		{
			return false;
		}
		for (int catalystRequired : getEnchantCatalystId(item))
		{
			if (catalystRequired == catalyst.getItemId())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method isLifeStone.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isLifeStone(int itemId)
	{
		return ((itemId >= 8723) && (itemId <= 8762)) || ((itemId >= 9573) && (itemId <= 9576)) || ((itemId >= 10483) && (itemId <= 10486)) || ((itemId >= 14166) && (itemId <= 14169)) || ((itemId >= 16160) && (itemId <= 16167));
	}
	
	/**
	 * Method isAccessoryLifeStone.
	 * @param itemId int
	 * @return boolean
	 */
	public final static boolean isAccessoryLifeStone(int itemId)
	{
		return ((itemId >= 12754) && (itemId <= 12763)) || ((itemId >= 12840) && (itemId <= 12851)) || (itemId == 12821) || (itemId == 12822) || (itemId == 14008) || (itemId == 16177) || (itemId == 16178);
	}
	
	/**
	 * Method getLifeStoneGrade.
	 * @param itemId int
	 * @return int
	 */
	public final static int getLifeStoneGrade(int itemId)
	{
		switch (itemId)
		{
			case 8723:
			case 8724:
			case 8725:
			case 8726:
			case 8727:
			case 8728:
			case 8729:
			case 8730:
			case 8731:
			case 8732:
			case 9573:
			case 10483:
			case 14166:
			case 16160:
			case 16164:
				return 0;
			case 8733:
			case 8734:
			case 8735:
			case 8736:
			case 8737:
			case 8738:
			case 8739:
			case 8740:
			case 8741:
			case 8742:
			case 9574:
			case 10484:
			case 14167:
			case 16161:
			case 16165:
				return 1;
			case 8743:
			case 8744:
			case 8745:
			case 8746:
			case 8747:
			case 8748:
			case 8749:
			case 8750:
			case 8751:
			case 8752:
			case 9575:
			case 10485:
			case 14168:
			case 16162:
			case 16166:
				return 2;
			case 8753:
			case 8754:
			case 8755:
			case 8756:
			case 8757:
			case 8758:
			case 8759:
			case 8760:
			case 8761:
			case 8762:
			case 9576:
			case 10486:
			case 14169:
			case 16163:
			case 16167:
				return 3;
			default:
				return 0;
		}
	}
	
	/**
	 * Method getLifeStoneLevel.
	 * @param itemId int
	 * @return int
	 */
	public final static int getLifeStoneLevel(int itemId)
	{
		switch (itemId)
		{
			case 8723:
			case 8733:
			case 8743:
			case 8753:
			case 12754:
			case 12840:
				return 1;
			case 8724:
			case 8734:
			case 8744:
			case 8754:
			case 12755:
			case 12841:
				return 2;
			case 8725:
			case 8735:
			case 8745:
			case 8755:
			case 12756:
			case 12842:
				return 3;
			case 8726:
			case 8736:
			case 8746:
			case 8756:
			case 12757:
			case 12843:
				return 4;
			case 8727:
			case 8737:
			case 8747:
			case 8757:
			case 12758:
			case 12844:
				return 5;
			case 8728:
			case 8738:
			case 8748:
			case 8758:
			case 12759:
			case 12845:
				return 6;
			case 8729:
			case 8739:
			case 8749:
			case 8759:
			case 12760:
			case 12846:
				return 7;
			case 8730:
			case 8740:
			case 8750:
			case 8760:
			case 12761:
			case 12847:
				return 8;
			case 8731:
			case 8741:
			case 8751:
			case 8761:
			case 12762:
			case 12848:
				return 9;
			case 8732:
			case 8742:
			case 8752:
			case 8762:
			case 12763:
			case 12849:
				return 10;
			case 9573:
			case 9574:
			case 9575:
			case 9576:
			case 12821:
			case 12850:
				return 11;
			case 10483:
			case 10484:
			case 10485:
			case 10486:
			case 12822:
			case 12851:
				return 12;
			case 14008:
			case 14166:
			case 14167:
			case 14168:
			case 14169:
				return 13;
			case 16160:
			case 16161:
			case 16162:
			case 16163:
			case 16177:
				return 14;
			case 16164:
			case 16165:
			case 16166:
			case 16167:
			case 16178:
				return 15;
			default:
				return 1;
		}
	}
	
	/**
	 * Method getEnchantAttributeStoneElement.
	 * @param itemId int
	 * @param isArmor boolean
	 * @return Element
	 */
	public static Element getEnchantAttributeStoneElement(int itemId, boolean isArmor)
	{
		Element element = Element.NONE;
		switch (itemId)
		{
			case 9546:
			case 9552:
			case 10521:
				element = Element.FIRE;
				break;
			case 9547:
			case 9553:
			case 10522:
				element = Element.WATER;
				break;
			case 9548:
			case 9554:
			case 10523:
				element = Element.EARTH;
				break;
			case 9549:
			case 9555:
			case 10524:
				element = Element.WIND;
				break;
			case 9550:
			case 9556:
			case 10525:
				element = Element.UNHOLY;
				break;
			case 9551:
			case 9557:
			case 10526:
				element = Element.HOLY;
				break;
		}
		if (isArmor)
		{
			return Element.getReverseElement(element);
		}
		return element;
	}
}
