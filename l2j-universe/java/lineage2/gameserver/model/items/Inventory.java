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
package lineage2.gameserver.model.items;

import java.util.Comparator;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.listener.inventory.OnEquipListener;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.model.items.listeners.StatsListener;
import lineage2.gameserver.templates.item.EtcItemTemplate.EtcItemType;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Inventory extends ItemContainer
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Inventory.class);
	/**
	 * Field PAPERDOLL_UNDER. (value is 0)
	 */
	public static final int PAPERDOLL_UNDER = 0;
	/**
	 * Field PAPERDOLL_REAR. (value is 1)
	 */
	public static final int PAPERDOLL_REAR = 1;
	/**
	 * Field PAPERDOLL_LEAR. (value is 2)
	 */
	public static final int PAPERDOLL_LEAR = 2;
	/**
	 * Field PAPERDOLL_NECK. (value is 3)
	 */
	public static final int PAPERDOLL_NECK = 3;
	/**
	 * Field PAPERDOLL_RFINGER. (value is 4)
	 */
	public static final int PAPERDOLL_RFINGER = 4;
	/**
	 * Field PAPERDOLL_LFINGER. (value is 5)
	 */
	public static final int PAPERDOLL_LFINGER = 5;
	/**
	 * Field PAPERDOLL_HEAD. (value is 6)
	 */
	public static final int PAPERDOLL_HEAD = 6;
	/**
	 * Field PAPERDOLL_RHAND. (value is 7)
	 */
	public static final int PAPERDOLL_RHAND = 7;
	/**
	 * Field PAPERDOLL_LHAND. (value is 8)
	 */
	public static final int PAPERDOLL_LHAND = 8;
	/**
	 * Field PAPERDOLL_GLOVES. (value is 9)
	 */
	public static final int PAPERDOLL_GLOVES = 9;
	/**
	 * Field PAPERDOLL_CHEST. (value is 10)
	 */
	public static final int PAPERDOLL_CHEST = 10;
	/**
	 * Field PAPERDOLL_LEGS. (value is 11)
	 */
	public static final int PAPERDOLL_LEGS = 11;
	/**
	 * Field PAPERDOLL_FEET. (value is 12)
	 */
	public static final int PAPERDOLL_FEET = 12;
	/**
	 * Field PAPERDOLL_BACK. (value is 13)
	 */
	public static final int PAPERDOLL_BACK = 13;
	/**
	 * Field PAPERDOLL_LRHAND. (value is 14)
	 */
	public static final int PAPERDOLL_LRHAND = 14;
	/**
	 * Field PAPERDOLL_HAIR. (value is 15)
	 */
	public static final int PAPERDOLL_HAIR = 15;
	/**
	 * Field PAPERDOLL_DHAIR. (value is 16)
	 */
	public static final int PAPERDOLL_DHAIR = 16;
	/**
	 * Field PAPERDOLL_RBRACELET. (value is 17)
	 */
	public static final int PAPERDOLL_RBRACELET = 17;
	/**
	 * Field PAPERDOLL_LBRACELET. (value is 18)
	 */
	public static final int PAPERDOLL_LBRACELET = 18;
	/**
	 * Field PAPERDOLL_DECO1. (value is 19)
	 */
	public static final int PAPERDOLL_DECO1 = 19;
	/**
	 * Field PAPERDOLL_DECO2. (value is 20)
	 */
	public static final int PAPERDOLL_DECO2 = 20;
	/**
	 * Field PAPERDOLL_DECO3. (value is 21)
	 */
	public static final int PAPERDOLL_DECO3 = 21;
	/**
	 * Field PAPERDOLL_DECO4. (value is 22)
	 */
	public static final int PAPERDOLL_DECO4 = 22;
	/**
	 * Field PAPERDOLL_DECO5. (value is 23)
	 */
	public static final int PAPERDOLL_DECO5 = 23;
	/**
	 * Field PAPERDOLL_DECO6. (value is 24)
	 */
	public static final int PAPERDOLL_DECO6 = 24;
	/**
	 * Field PAPERDOLL_BELT. (value is 25)
	 */
	public static final int PAPERDOLL_BELT = 25;
	/**
	 * Field PAPERDOLL_MAX. (value is 26)
	 */
	public static final int PAPERDOLL_MAX = 26;
	/**
	 * Field PAPERDOLL_ORDER.
	 */
	public static final int[] PAPERDOLL_ORDER =
	{
		Inventory.PAPERDOLL_UNDER,
		Inventory.PAPERDOLL_REAR,
		Inventory.PAPERDOLL_LEAR,
		Inventory.PAPERDOLL_NECK,
		Inventory.PAPERDOLL_RFINGER,
		Inventory.PAPERDOLL_LFINGER,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_BACK,
		Inventory.PAPERDOLL_LRHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_DHAIR,
		Inventory.PAPERDOLL_RBRACELET,
		Inventory.PAPERDOLL_LBRACELET,
		Inventory.PAPERDOLL_DECO1,
		Inventory.PAPERDOLL_DECO2,
		Inventory.PAPERDOLL_DECO3,
		Inventory.PAPERDOLL_DECO4,
		Inventory.PAPERDOLL_DECO5,
		Inventory.PAPERDOLL_DECO6,
		Inventory.PAPERDOLL_BELT
	};
	
	/**
	 * @author Mobius
	 */
	public class InventoryListenerList extends ListenerList<Playable>
	{
		/**
		 * Method onEquip.
		 * @param slot int
		 * @param item ItemInstance
		 */
		public void onEquip(int slot, ItemInstance item)
		{
			for (Listener<Playable> listener : getListeners())
			{
				((OnEquipListener) listener).onEquip(slot, item, getActor());
			}
		}
		
		/**
		 * Method onUnequip.
		 * @param slot int
		 * @param item ItemInstance
		 */
		public void onUnequip(int slot, ItemInstance item)
		{
			for (Listener<Playable> listener : getListeners())
			{
				((OnEquipListener) listener).onUnequip(slot, item, getActor());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class ItemOrderComparator implements Comparator<ItemInstance>
	{
		/**
		 * Field instance.
		 */
		private static final Comparator<ItemInstance> instance = new ItemOrderComparator();
		
		/**
		 * Method getInstance.
		 * @return Comparator<ItemInstance>
		 */
		public static final Comparator<ItemInstance> getInstance()
		{
			return instance;
		}
		
		/**
		 * Method compare.
		 * @param o1 ItemInstance
		 * @param o2 ItemInstance
		 * @return int
		 */
		@Override
		public int compare(ItemInstance o1, ItemInstance o2)
		{
			if ((o1 == null) || (o2 == null))
			{
				return 0;
			}
			return o1.getLocData() - o2.getLocData();
		}
	}
	
	/**
	 * Field _ownerId.
	 */
	protected final int _ownerId;
	/**
	 * Field _paperdoll.
	 */
	protected final ItemInstance[] _paperdoll = new ItemInstance[PAPERDOLL_MAX];
	/**
	 * Field _listeners.
	 */
	protected final InventoryListenerList _listeners = new InventoryListenerList();
	/**
	 * Field _totalWeight.
	 */
	protected int _totalWeight;
	/**
	 * Field _wearedMask.
	 */
	protected long _wearedMask;
	
	/**
	 * Constructor for Inventory.
	 * @param ownerId int
	 */
	protected Inventory(int ownerId)
	{
		_ownerId = ownerId;
		addListener(StatsListener.getInstance());
	}
	
	/**
	 * Method getActor.
	 * @return Playable
	 */
	public abstract Playable getActor();
	
	/**
	 * Method getBaseLocation.
	 * @return ItemLocation
	 */
	protected abstract ItemLocation getBaseLocation();
	
	/**
	 * Method getEquipLocation.
	 * @return ItemLocation
	 */
	protected abstract ItemLocation getEquipLocation();
	
	/**
	 * Method getOwnerId.
	 * @return int
	 */
	public int getOwnerId()
	{
		return _ownerId;
	}
	
	/**
	 * Method onRestoreItem.
	 * @param item ItemInstance
	 */
	protected void onRestoreItem(ItemInstance item)
	{
		_totalWeight += item.getTemplate().getWeight() * item.getCount();
	}
	
	/**
	 * Method onAddItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onAddItem(ItemInstance item)
	{
		item.setOwnerId(getOwnerId());
		item.setLocation(getBaseLocation());
		item.setLocData(findSlot());
		if (item.getJdbcState().isSavable())
		{
			item.save();
		}
		else
		{
			item.setJdbcState(JdbcEntityState.UPDATED);
			item.update();
		}
		sendAddItem(item);
		refreshWeight();
	}
	
	/**
	 * Method onModifyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onModifyItem(ItemInstance item)
	{
		item.setJdbcState(JdbcEntityState.UPDATED);
		item.update();
		sendModifyItem(item);
		refreshWeight();
	}
	
	/**
	 * Method onRemoveItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onRemoveItem(ItemInstance item)
	{
		if (item.isEquipped())
		{
			unEquipItem(item);
		}
		sendRemoveItem(item);
		item.setLocData(-1);
		refreshWeight();
	}
	
	/**
	 * Method onDestroyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onDestroyItem(ItemInstance item)
	{
		item.setCount(0L);
		item.delete();
	}
	
	/**
	 * Method onEquip.
	 * @param slot int
	 * @param item ItemInstance
	 */
	protected void onEquip(int slot, ItemInstance item)
	{
		_listeners.onEquip(slot, item);
		item.setLocation(getEquipLocation());
		item.setLocData(slot);
		item.setEquipped(true);
		item.setJdbcState(JdbcEntityState.UPDATED);
		sendModifyItem(item);
		_wearedMask |= item.getTemplate().getItemMask();
	}
	
	/**
	 * Method onUnequip.
	 * @param slot int
	 * @param item ItemInstance
	 */
	protected void onUnequip(int slot, ItemInstance item)
	{
		item.setLocation(getBaseLocation());
		item.setLocData(findSlot());
		item.setEquipped(false);
		item.setJdbcState(JdbcEntityState.UPDATED);
		item.setChargedSpiritshot(ItemInstance.CHARGED_NONE);
		item.setChargedSoulshot(ItemInstance.CHARGED_NONE);
		sendModifyItem(item);
		_wearedMask &= ~item.getTemplate().getItemMask();
		_listeners.onUnequip(slot, item);
	}
	
	/**
	 * Method findSlot.
	 * @return int
	 */
	private int findSlot()
	{
		ItemInstance item;
		int slot = 0;
		loop:
		for (slot = 0; slot < _items.size(); slot++)
		{
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				if (item.isEquipped() || item.getTemplate().isQuest())
				{
					continue;
				}
				if (item.getEquipSlot() == slot)
				{
					continue loop;
				}
			}
			break;
		}
		return slot;
	}
	
	/**
	 * Method getPaperdollItem.
	 * @param slot int
	 * @return ItemInstance
	 */
	public ItemInstance getPaperdollItem(int slot)
	{
		return _paperdoll[slot];
	}
	
	/**
	 * Method getPaperdollItems.
	 * @return ItemInstance[]
	 */
	public ItemInstance[] getPaperdollItems()
	{
		return _paperdoll;
	}
	
	/**
	 * Method getPaperdollItemId.
	 * @param slot int
	 * @return int
	 */
	public int getPaperdollItemId(int slot)
	{
		ItemInstance item = getPaperdollItem(slot);
		if (item != null)
		{
			return item.getItemId();
		}
		else if (slot == PAPERDOLL_HAIR)
		{
			item = _paperdoll[PAPERDOLL_DHAIR];
			if (item != null)
			{
				return item.getItemId();
			}
		}
		return 0;
	}
	
	public int getVisualItemId(int slot)
	{
		ItemInstance item = getPaperdollItem(slot);
		if(item != null)
		{
			return item.getVisualId();
		}
		else if(slot == PAPERDOLL_HAIR)
		{
			item = _paperdoll[PAPERDOLL_DHAIR];
			if(item != null)
			{
				return item.getVisualId();
			}
		}

		return 0;
	}
	/**
	 * Method getPaperdollObjectId.
	 * @param slot int
	 * @return int
	 */
	public int getPaperdollObjectId(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		if (item != null)
		{
			return item.getObjectId();
		}
		else if (slot == PAPERDOLL_HAIR)
		{
			item = _paperdoll[PAPERDOLL_DHAIR];
			if (item != null)
			{
				return item.getObjectId();
			}
		}
		return 0;
	}
	
	/**
	 * Method addListener.
	 * @param listener OnEquipListener
	 */
	public void addListener(OnEquipListener listener)
	{
		_listeners.add(listener);
	}
	
	/**
	 * Method removeListener.
	 * @param listener OnEquipListener
	 */
	public void removeListener(OnEquipListener listener)
	{
		_listeners.remove(listener);
	}
	
	/**
	 * Method setPaperdollItem.
	 * @param slot int
	 * @param item ItemInstance
	 * @return ItemInstance
	 */
	public ItemInstance setPaperdollItem(int slot, ItemInstance item)
	{
		ItemInstance old;
		writeLock();
		try
		{
			old = _paperdoll[slot];
			if (old != item)
			{
				if (old != null)
				{
					_paperdoll[slot] = null;
					onUnequip(slot, old);
				}
				if (item != null)
				{
					_paperdoll[slot] = item;
					onEquip(slot, item);
				}
			}
		}
		finally
		{
			writeUnlock();
		}
		return old;
	}
	
	/**
	 * Method getWearedMask.
	 * @return long
	 */
	public long getWearedMask()
	{
		return _wearedMask;
	}
	
	/**
	 * Method unEquipItem.
	 * @param item ItemInstance
	 */
	public void unEquipItem(ItemInstance item)
	{
		if (item.isEquipped())
		{
			unEquipItemInBodySlot(item.getBodyPart(), item);
		}
	}
	
	/**
	 * Method unEquipItemInBodySlot.
	 * @param bodySlot int
	 */
	public void unEquipItemInBodySlot(int bodySlot)
	{
		unEquipItemInBodySlot(bodySlot, null);
	}
	
	/**
	 * Method unEquipItemInBodySlot.
	 * @param bodySlot int
	 * @param item ItemInstance
	 */
	private void unEquipItemInBodySlot(int bodySlot, ItemInstance item)
	{
		int pdollSlot = -1;
		switch (bodySlot)
		{
			case ItemTemplate.SLOT_NECK:
				pdollSlot = PAPERDOLL_NECK;
				break;
			case ItemTemplate.SLOT_L_EAR:
				pdollSlot = PAPERDOLL_LEAR;
				break;
			case ItemTemplate.SLOT_R_EAR:
				pdollSlot = PAPERDOLL_REAR;
				break;
			case ItemTemplate.SLOT_L_EAR | ItemTemplate.SLOT_R_EAR:
				if (item == null)
				{
					return;
				}
				if (getPaperdollItem(PAPERDOLL_LEAR) == item)
				{
					pdollSlot = PAPERDOLL_LEAR;
				}
				if (getPaperdollItem(PAPERDOLL_REAR) == item)
				{
					pdollSlot = PAPERDOLL_REAR;
				}
				break;
			case ItemTemplate.SLOT_L_FINGER:
				pdollSlot = PAPERDOLL_LFINGER;
				break;
			case ItemTemplate.SLOT_R_FINGER:
				pdollSlot = PAPERDOLL_RFINGER;
				break;
			case ItemTemplate.SLOT_L_FINGER | ItemTemplate.SLOT_R_FINGER:
				if (item == null)
				{
					return;
				}
				if (getPaperdollItem(PAPERDOLL_LFINGER) == item)
				{
					pdollSlot = PAPERDOLL_LFINGER;
				}
				if (getPaperdollItem(PAPERDOLL_RFINGER) == item)
				{
					pdollSlot = PAPERDOLL_RFINGER;
				}
				break;
			case ItemTemplate.SLOT_HAIR:
				pdollSlot = PAPERDOLL_HAIR;
				break;
			case ItemTemplate.SLOT_DHAIR:
				pdollSlot = PAPERDOLL_DHAIR;
				break;
			case ItemTemplate.SLOT_HAIRALL:
				setPaperdollItem(PAPERDOLL_DHAIR, null);
				pdollSlot = PAPERDOLL_HAIR;
				break;
			case ItemTemplate.SLOT_HEAD:
				pdollSlot = PAPERDOLL_HEAD;
				break;
			case ItemTemplate.SLOT_R_HAND:
				pdollSlot = PAPERDOLL_RHAND;
				break;
			case ItemTemplate.SLOT_L_HAND:
				pdollSlot = PAPERDOLL_LHAND;
				break;
			case ItemTemplate.SLOT_GLOVES:
				pdollSlot = PAPERDOLL_GLOVES;
				break;
			case ItemTemplate.SLOT_LEGS:
				pdollSlot = PAPERDOLL_LEGS;
				break;
			case ItemTemplate.SLOT_CHEST:
			case ItemTemplate.SLOT_FULL_ARMOR:
			case ItemTemplate.SLOT_FORMAL_WEAR:
				pdollSlot = PAPERDOLL_CHEST;
				break;
			case ItemTemplate.SLOT_BACK:
				pdollSlot = PAPERDOLL_BACK;
				break;
			case ItemTemplate.SLOT_FEET:
				pdollSlot = PAPERDOLL_FEET;
				break;
			case ItemTemplate.SLOT_UNDERWEAR:
				pdollSlot = PAPERDOLL_UNDER;
				break;
			case ItemTemplate.SLOT_BELT:
				pdollSlot = PAPERDOLL_BELT;
				break;
			case ItemTemplate.SLOT_LR_HAND:
				setPaperdollItem(PAPERDOLL_LHAND, null);
				pdollSlot = PAPERDOLL_RHAND;
				break;
			case ItemTemplate.SLOT_L_BRACELET:
				pdollSlot = PAPERDOLL_LBRACELET;
				break;
			case ItemTemplate.SLOT_R_BRACELET:
				pdollSlot = PAPERDOLL_RBRACELET;
				setPaperdollItem(Inventory.PAPERDOLL_DECO1, null);
				setPaperdollItem(Inventory.PAPERDOLL_DECO2, null);
				setPaperdollItem(Inventory.PAPERDOLL_DECO3, null);
				setPaperdollItem(Inventory.PAPERDOLL_DECO4, null);
				setPaperdollItem(Inventory.PAPERDOLL_DECO5, null);
				setPaperdollItem(Inventory.PAPERDOLL_DECO6, null);
				break;
			case ItemTemplate.SLOT_DECO:
				if (item == null)
				{
					return;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO1) == item)
				{
					pdollSlot = PAPERDOLL_DECO1;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO2) == item)
				{
					pdollSlot = PAPERDOLL_DECO2;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO3) == item)
				{
					pdollSlot = PAPERDOLL_DECO3;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO4) == item)
				{
					pdollSlot = PAPERDOLL_DECO4;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO5) == item)
				{
					pdollSlot = PAPERDOLL_DECO5;
				}
				else if (getPaperdollItem(PAPERDOLL_DECO6) == item)
				{
					pdollSlot = PAPERDOLL_DECO6;
				}
				break;
			default:
				_log.warn("Requested invalid body slot: " + bodySlot + ", Item: " + item + ", ownerId: '" + getOwnerId() + "'");
				return;
		}
		if (pdollSlot >= 0)
		{
			setPaperdollItem(pdollSlot, null);
		}
	}
	
	/**
	 * Method equipItem.
	 * @param item ItemInstance
	 */
	public void equipItem(ItemInstance item)
	{
		int bodySlot = item.getBodyPart();
		double hp = getActor().getCurrentHp();
		double mp = getActor().getCurrentMp();
		double cp = getActor().getCurrentCp();
		switch (bodySlot)
		{
			case ItemTemplate.SLOT_LR_HAND:
			{
				setPaperdollItem(PAPERDOLL_LHAND, null);
				setPaperdollItem(PAPERDOLL_RHAND, item);
				break;
			}
			case ItemTemplate.SLOT_L_HAND:
			{
				final ItemInstance rHandItem = getPaperdollItem(PAPERDOLL_RHAND);
				final ItemTemplate rHandItemTemplate = rHandItem == null ? null : rHandItem.getTemplate();
				final ItemTemplate newItem = item.getTemplate();
				if (newItem.getItemType() == EtcItemType.ARROW)
				{
					if (rHandItemTemplate == null)
					{
						return;
					}
					if (rHandItemTemplate.getItemType() != WeaponType.BOW)
					{
						return;
					}
					if (rHandItemTemplate.getCrystalType() != newItem.getCrystalType())
					{
						return;
					}
				}
				else if (newItem.getItemType() == EtcItemType.BOLT)
				{
					if (rHandItemTemplate == null)
					{
						return;
					}
					if (rHandItemTemplate.getItemType() != WeaponType.CROSSBOW)
					{
						return;
					}
					if (rHandItemTemplate.getCrystalType() != newItem.getCrystalType())
					{
						return;
					}
				}
				else if (newItem.getItemType() == EtcItemType.BAIT)
				{
					if (rHandItemTemplate == null)
					{
						return;
					}
					if (rHandItemTemplate.getItemType() != WeaponType.ROD)
					{
						return;
					}
					if (!getActor().isPlayer())
					{
						return;
					}
					Player owner = (Player) getActor();
					owner.setVar("LastLure", String.valueOf(item.getObjectId()), -1);
				}
				else
				{
					if ((rHandItemTemplate != null) && (rHandItemTemplate.getBodyPart() == ItemTemplate.SLOT_LR_HAND))
					{
						setPaperdollItem(PAPERDOLL_RHAND, null);
					}
				}
				setPaperdollItem(PAPERDOLL_LHAND, item);
				break;
			}
			case ItemTemplate.SLOT_R_HAND:
			{
				setPaperdollItem(PAPERDOLL_RHAND, item);
				break;
			}
			case ItemTemplate.SLOT_L_EAR:
			case ItemTemplate.SLOT_R_EAR:
			case ItemTemplate.SLOT_L_EAR | ItemTemplate.SLOT_R_EAR:
			{
				if (_paperdoll[PAPERDOLL_LEAR] == null)
				{
					setPaperdollItem(PAPERDOLL_LEAR, item);
				}
				else if (_paperdoll[PAPERDOLL_REAR] == null)
				{
					setPaperdollItem(PAPERDOLL_REAR, item);
				}
				else
				{
					setPaperdollItem(PAPERDOLL_LEAR, item);
				}
				break;
			}
			case ItemTemplate.SLOT_L_FINGER:
			case ItemTemplate.SLOT_R_FINGER:
			case ItemTemplate.SLOT_L_FINGER | ItemTemplate.SLOT_R_FINGER:
			{
				if (_paperdoll[PAPERDOLL_LFINGER] == null)
				{
					setPaperdollItem(PAPERDOLL_LFINGER, item);
				}
				else if (_paperdoll[PAPERDOLL_RFINGER] == null)
				{
					setPaperdollItem(PAPERDOLL_RFINGER, item);
				}
				else
				{
					setPaperdollItem(PAPERDOLL_LFINGER, item);
				}
				break;
			}
			case ItemTemplate.SLOT_NECK:
				setPaperdollItem(PAPERDOLL_NECK, item);
				break;
			case ItemTemplate.SLOT_FULL_ARMOR:
				setPaperdollItem(PAPERDOLL_LEGS, null);
				setPaperdollItem(PAPERDOLL_CHEST, item);
				break;
			case ItemTemplate.SLOT_CHEST:
				setPaperdollItem(PAPERDOLL_CHEST, item);
				break;
			case ItemTemplate.SLOT_LEGS:
			{
				ItemInstance chest = getPaperdollItem(PAPERDOLL_CHEST);
				if ((chest != null) && (chest.getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR))
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				else if (getPaperdollItemId(PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				setPaperdollItem(PAPERDOLL_LEGS, item);
				break;
			}
			case ItemTemplate.SLOT_FEET:
				if (getPaperdollItemId(PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				setPaperdollItem(PAPERDOLL_FEET, item);
				break;
			case ItemTemplate.SLOT_GLOVES:
				if (getPaperdollItemId(PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				setPaperdollItem(PAPERDOLL_GLOVES, item);
				break;
			case ItemTemplate.SLOT_HEAD:
				if (getPaperdollItemId(PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR)
				{
					setPaperdollItem(PAPERDOLL_CHEST, null);
				}
				setPaperdollItem(PAPERDOLL_HEAD, item);
				break;
			case ItemTemplate.SLOT_HAIR:
				ItemInstance old = getPaperdollItem(PAPERDOLL_DHAIR);
				if ((old != null) && (old.getBodyPart() == ItemTemplate.SLOT_HAIRALL))
				{
					setPaperdollItem(PAPERDOLL_DHAIR, null);
				}
				setPaperdollItem(PAPERDOLL_HAIR, item);
				break;
			case ItemTemplate.SLOT_DHAIR:
				ItemInstance slot2 = getPaperdollItem(PAPERDOLL_DHAIR);
				if ((slot2 != null) && (slot2.getBodyPart() == ItemTemplate.SLOT_HAIRALL))
				{
					setPaperdollItem(PAPERDOLL_HAIR, null);
				}
				setPaperdollItem(PAPERDOLL_DHAIR, item);
				break;
			case ItemTemplate.SLOT_HAIRALL:
				setPaperdollItem(PAPERDOLL_HAIR, null);
				setPaperdollItem(PAPERDOLL_DHAIR, item);
				break;
			case ItemTemplate.SLOT_R_BRACELET:
				setPaperdollItem(PAPERDOLL_RBRACELET, item);
				break;
			case ItemTemplate.SLOT_L_BRACELET:
				setPaperdollItem(PAPERDOLL_LBRACELET, item);
				break;
			case ItemTemplate.SLOT_UNDERWEAR:
				setPaperdollItem(PAPERDOLL_UNDER, item);
				break;
			case ItemTemplate.SLOT_BACK:
				setPaperdollItem(PAPERDOLL_BACK, item);
				break;
			case ItemTemplate.SLOT_BELT:
				setPaperdollItem(PAPERDOLL_BELT, item);
				break;
			case ItemTemplate.SLOT_DECO:
				if (_paperdoll[PAPERDOLL_DECO1] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO1, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO2] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO2, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO3] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO3, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO4] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO4, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO5] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO5, item);
				}
				else if (_paperdoll[PAPERDOLL_DECO6] == null)
				{
					setPaperdollItem(PAPERDOLL_DECO6, item);
				}
				else
				{
					setPaperdollItem(PAPERDOLL_DECO1, item);
				}
				break;
			case ItemTemplate.SLOT_FORMAL_WEAR:
				setPaperdollItem(PAPERDOLL_LEGS, null);
				setPaperdollItem(PAPERDOLL_HEAD, null);
				setPaperdollItem(PAPERDOLL_FEET, null);
				setPaperdollItem(PAPERDOLL_GLOVES, null);
				setPaperdollItem(PAPERDOLL_CHEST, item);
				break;
			default:
				_log.warn("unknown body slot:" + bodySlot + " for item id: " + item.getItemId());
				return;
		}
		getActor().setCurrentHp(hp, false);
		getActor().setCurrentMp(mp);
		getActor().setCurrentCp(cp);
		if (getActor().isPlayer())
		{
			((Player) getActor()).autoShot();
		}
	}
	
	/**
	 * Method sendAddItem.
	 * @param item ItemInstance
	 */
	protected abstract void sendAddItem(ItemInstance item);
	
	/**
	 * Method sendModifyItem.
	 * @param item ItemInstance
	 */
	protected abstract void sendModifyItem(ItemInstance item);
	
	/**
	 * Method sendRemoveItem.
	 * @param item ItemInstance
	 */
	protected abstract void sendRemoveItem(ItemInstance item);
	
	/**
	 * Method refreshWeight.
	 */
	protected void refreshWeight()
	{
		int weight = 0;
		readLock();
		try
		{
			ItemInstance item;
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				weight += item.getTemplate().getWeight() * item.getCount();
			}
		}
		finally
		{
			readUnlock();
		}
		if (_totalWeight == weight)
		{
			return;
		}
		_totalWeight = weight;
		onRefreshWeight();
	}
	
	/**
	 * Method onRefreshWeight.
	 */
	protected abstract void onRefreshWeight();
	
	/**
	 * Method getTotalWeight.
	 * @return int
	 */
	public int getTotalWeight()
	{
		return _totalWeight;
	}
	
	/**
	 * Method validateCapacity.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public boolean validateCapacity(ItemInstance item)
	{
		long slots = 0;
		if (!item.isStackable() || (getItemByItemId(item.getItemId()) == null))
		{
			slots++;
		}
		return validateCapacity(slots);
	}
	
	/**
	 * Method validateCapacity.
	 * @param itemId int
	 * @param count long
	 * @return boolean
	 */
	public boolean validateCapacity(int itemId, long count)
	{
		ItemTemplate item = ItemHolder.getInstance().getTemplate(itemId);
		return validateCapacity(item, count);
	}
	
	/**
	 * Method validateCapacity.
	 * @param item ItemTemplate
	 * @param count long
	 * @return boolean
	 */
	public boolean validateCapacity(ItemTemplate item, long count)
	{
		long slots = 0;
		if (!item.isStackable() || (getItemByItemId(item.getItemId()) == null))
		{
			slots = count;
		}
		return validateCapacity(slots);
	}
	
	/**
	 * Method validateCapacity.
	 * @param slots long
	 * @return boolean
	 */
	public boolean validateCapacity(long slots)
	{
		if (slots == 0)
		{
			return true;
		}
		if ((slots < Integer.MIN_VALUE) || (slots > Integer.MAX_VALUE))
		{
			return false;
		}
		if ((getSize() + (int) slots) < 0)
		{
			return false;
		}
		return (getSize() + slots) <= getActor().getInventoryLimit();
	}
	
	/**
	 * Method validateWeight.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public boolean validateWeight(ItemInstance item)
	{
		long weight = item.getTemplate().getWeight() * item.getCount();
		return validateWeight(weight);
	}
	
	/**
	 * Method validateWeight.
	 * @param itemId int
	 * @param count long
	 * @return boolean
	 */
	public boolean validateWeight(int itemId, long count)
	{
		ItemTemplate item = ItemHolder.getInstance().getTemplate(itemId);
		return validateWeight(item, count);
	}
	
	/**
	 * Method validateWeight.
	 * @param item ItemTemplate
	 * @param count long
	 * @return boolean
	 */
	public boolean validateWeight(ItemTemplate item, long count)
	{
		long weight = item.getWeight() * count;
		return validateWeight(weight);
	}
	
	/**
	 * Method validateWeight.
	 * @param weight long
	 * @return boolean
	 */
	public boolean validateWeight(long weight)
	{
		if (weight == 0L)
		{
			return true;
		}
		if ((weight < Integer.MIN_VALUE) || (weight > Integer.MAX_VALUE))
		{
			return false;
		}
		if ((getTotalWeight() + (int) weight) < 0)
		{
			return false;
		}
		return (getTotalWeight() + weight) <= getActor().getMaxLoad();
	}
	
	/**
	 * Method restore.
	 */
	public abstract void restore();
	
	/**
	 * Method store.
	 */
	public abstract void store();
	
	/**
	 * Method getPaperdollIndex.
	 * @param slot int
	 * @return int
	 */
	public static int getPaperdollIndex(int slot)
	{
		switch (slot)
		{
			case ItemTemplate.SLOT_UNDERWEAR:
				return PAPERDOLL_UNDER;
			case ItemTemplate.SLOT_R_EAR:
				return PAPERDOLL_REAR;
			case ItemTemplate.SLOT_L_EAR:
				return PAPERDOLL_LEAR;
			case ItemTemplate.SLOT_NECK:
				return PAPERDOLL_NECK;
			case ItemTemplate.SLOT_R_FINGER:
				return PAPERDOLL_RFINGER;
			case ItemTemplate.SLOT_L_FINGER:
				return PAPERDOLL_LFINGER;
			case ItemTemplate.SLOT_HEAD:
				return PAPERDOLL_HEAD;
			case ItemTemplate.SLOT_R_HAND:
				return PAPERDOLL_RHAND;
			case ItemTemplate.SLOT_L_HAND:
				return PAPERDOLL_LHAND;
			case ItemTemplate.SLOT_LR_HAND:
				return PAPERDOLL_LRHAND;
			case ItemTemplate.SLOT_GLOVES:
				return PAPERDOLL_GLOVES;
			case ItemTemplate.SLOT_CHEST:
			case ItemTemplate.SLOT_FULL_ARMOR:
			case ItemTemplate.SLOT_FORMAL_WEAR:
				return PAPERDOLL_CHEST;
			case ItemTemplate.SLOT_LEGS:
				return PAPERDOLL_LEGS;
			case ItemTemplate.SLOT_FEET:
				return PAPERDOLL_FEET;
			case ItemTemplate.SLOT_BACK:
				return PAPERDOLL_BACK;
			case ItemTemplate.SLOT_HAIR:
			case ItemTemplate.SLOT_HAIRALL:
				return PAPERDOLL_HAIR;
			case ItemTemplate.SLOT_DHAIR:
				return PAPERDOLL_DHAIR;
			case ItemTemplate.SLOT_R_BRACELET:
				return PAPERDOLL_RBRACELET;
			case ItemTemplate.SLOT_L_BRACELET:
				return PAPERDOLL_LBRACELET;
			case ItemTemplate.SLOT_DECO:
				return PAPERDOLL_DECO1;
			case ItemTemplate.SLOT_BELT:
				return PAPERDOLL_BELT;
		}
		return -1;
	}
	
	/**
	 * Method getSize.
	 * @return int
	 */
	@Override
	public int getSize()
	{
		return super.getSize() - getQuestSize();
	}
	
	/**
	 * Method getAllSize.
	 * @return int
	 */
	public int getAllSize()
	{
		return super.getSize();
	}
	
	/**
	 * Method getQuestSize.
	 * @return int
	 */
	public int getQuestSize()
	{
		int size = 0;
		for (ItemInstance item : getItems())
		{
			if (item.getTemplate().isQuest())
			{
				size++;
			}
		}
		return size;
	}
}
