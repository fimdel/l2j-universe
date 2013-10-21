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

import java.util.Collection;

import lineage2.commons.collections.CollectionUtils;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.model.items.listeners.AccessoryListener;
import lineage2.gameserver.model.items.listeners.ArmorSetListener;
import lineage2.gameserver.model.items.listeners.BowListener;
import lineage2.gameserver.model.items.listeners.ItemAugmentationListener;
import lineage2.gameserver.model.items.listeners.ItemEnchantOptionsListener;
import lineage2.gameserver.model.items.listeners.ItemSkillsListener;
import lineage2.gameserver.network.serverpackets.ExBR_AgathionEnergyInfo;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.taskmanager.DelayedItemsManager;
import lineage2.gameserver.templates.item.EtcItemTemplate.EtcItemType;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.ItemFunctions;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PcInventory extends Inventory
{
	/**
	 * Field _owner.
	 */
	private final Player _owner;
	/**
	 * Field _lockType.
	 */
	private LockType _lockType = LockType.NONE;
	/**
	 * Field _lockItems.
	 */
	private int[] _lockItems = ArrayUtils.EMPTY_INT_ARRAY;
	
	/**
	 * Constructor for PcInventory.
	 * @param owner Player
	 */
	public PcInventory(Player owner)
	{
		super(owner.getObjectId());
		_owner = owner;
		addListener(ItemSkillsListener.getInstance());
		addListener(ItemAugmentationListener.getInstance());
		addListener(ItemEnchantOptionsListener.getInstance());
		addListener(ArmorSetListener.getInstance());
		addListener(BowListener.getInstance());
		addListener(AccessoryListener.getInstance());
	}
	
	/**
	 * Method getActor.
	 * @return Player
	 */
	@Override
	public Player getActor()
	{
		return _owner;
	}
	
	/**
	 * Method getBaseLocation.
	 * @return ItemLocation
	 */
	@Override
	protected ItemLocation getBaseLocation()
	{
		return ItemLocation.INVENTORY;
	}
	
	/**
	 * Method getEquipLocation.
	 * @return ItemLocation
	 */
	@Override
	protected ItemLocation getEquipLocation()
	{
		return ItemLocation.PAPERDOLL;
	}
	
	/**
	 * Method getAdena.
	 * @return long
	 */
	public long getAdena()
	{
		ItemInstance _adena = getItemByItemId(57);
		if (_adena == null)
		{
			return 0;
		}
		return _adena.getCount();
	}
	
	/**
	 * Method addAdena.
	 * @param amount long
	 * @return ItemInstance
	 */
	public ItemInstance addAdena(long amount)
	{
		return addItem(ItemTemplate.ITEM_ID_ADENA, amount);
	}
	
	/**
	 * Method reduceAdena.
	 * @param adena long
	 * @return boolean
	 */
	public boolean reduceAdena(long adena)
	{
		return destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, adena);
	}
	
	/**
	 * Method getPaperdollAugmentationId.
	 * @param slot int
	 * @return int
	 */
	public int getPaperdollAugmentationId(int slot)
	{
		ItemInstance item = _paperdoll[slot];
		if ((item != null) && item.isAugmented())
		{
			return item.getAugmentationId();
		}
		return 0;
	}
	
	/**
	 * Method getPaperdollItemId.
	 * @param slot int
	 * @return int
	 */
	@Override
	public int getPaperdollItemId(int slot)
	{
		Player player = getActor();
		int itemId = super.getPaperdollItemId(slot);
		if ((slot == PAPERDOLL_RHAND) && (itemId == 0) && player.isClanAirShipDriver())
		{
			itemId = 13556;
		}
		return itemId;
	}
	
	@Override
	public int getVisualItemId(int slot)
	{
		Player player = getActor();
		int itemId = super.getVisualItemId(slot);
		if(slot == PAPERDOLL_RHAND && itemId == 0 && player.isClanAirShipDriver())
		{
			itemId = 13556;
		}
		return itemId;
	}
	/**
	 * Method onRefreshWeight.
	 */
	@Override
	protected void onRefreshWeight()
	{
		getActor().refreshOverloaded();
	}
	
	/**
	 * Method validateItems.
	 */
	public void validateItems()
	{
		for (ItemInstance item : _paperdoll)
		{
			if ((item != null) && ((ItemFunctions.checkIfCanEquip(getActor(), item) != null) || !item.getTemplate().testCondition(getActor(), item)))
			{
				unEquipItem(item);
				getActor().sendDisarmMessage(item);
			}
		}
	}
	
	/**
	 * Method validateItemsSkills.
	 */
	public void validateItemsSkills()
	{
		for (ItemInstance item : _paperdoll)
		{
			if ((item == null) || (item.getTemplate().getType2() != ItemTemplate.TYPE2_WEAPON))
			{
				continue;
			}
			boolean needUnequipSkills = getActor().getWeaponsExpertisePenalty() > 0;
			if (item.getTemplate().getAttachedSkills().length > 0)
			{
				boolean has = getActor().getSkillLevel(item.getTemplate().getAttachedSkills()[0].getId()) > 0;
				if (needUnequipSkills && has)
				{
					ItemSkillsListener.getInstance().onUnequip(item.getEquipSlot(), item, getActor());
				}
				else if (!needUnequipSkills && !has)
				{
					ItemSkillsListener.getInstance().onEquip(item.getEquipSlot(), item, getActor());
				}
			}
			else if (item.getTemplate().getEnchant4Skill() != null)
			{
				boolean has = getActor().getSkillLevel(item.getTemplate().getEnchant4Skill().getId()) > 0;
				if (needUnequipSkills && has)
				{
					ItemSkillsListener.getInstance().onUnequip(item.getEquipSlot(), item, getActor());
				}
				else if (!needUnequipSkills && !has)
				{
					ItemSkillsListener.getInstance().onEquip(item.getEquipSlot(), item, getActor());
				}
			}
			else if (!item.getTemplate().getTriggerList().isEmpty())
			{
				if (needUnequipSkills)
				{
					ItemSkillsListener.getInstance().onUnequip(item.getEquipSlot(), item, getActor());
				}
				else
				{
					ItemSkillsListener.getInstance().onEquip(item.getEquipSlot(), item, getActor());
				}
			}
		}
	}
	
	/**
	 * Field isRefresh.
	 */
	public boolean isRefresh = false;
	
	/**
	 * Method refreshEquip.
	 */
	public void refreshEquip()
	{
		isRefresh = true;
		for (ItemInstance item : getItems())
		{
			if (item.isEquipped())
			{
				int slot = item.getEquipSlot();
				_listeners.onUnequip(slot, item);
				_listeners.onEquip(slot, item);
			}
			else if (item.getItemType() == EtcItemType.RUNE)
			{
				_listeners.onUnequip(-1, item);
				_listeners.onEquip(-1, item);
			}
		}
		isRefresh = false;
	}
	
	/**
	 * Method sort.
	 * @param order int[][]
	 */
	public void sort(int[][] order)
	{
		boolean needSort = false;
		for (int[] element : order)
		{
			ItemInstance item = getItemByObjectId(element[0]);
			if (item == null)
			{
				continue;
			}
			if (item.getLocation() != ItemLocation.INVENTORY)
			{
				continue;
			}
			if (item.getLocData() == element[1])
			{
				continue;
			}
			item.setLocData(element[1]);
			item.setJdbcState(JdbcEntityState.UPDATED);
			needSort = true;
		}
		if (needSort)
		{
			CollectionUtils.eqSort(_items, ItemOrderComparator.getInstance());
		}
	}
	
	/**
	 * Field arrows.
	 */
	private static final int[][] arrows =
	{
		{
			17,
			32249
		},
		{
			1341,
			22067,
			32250
		},
		{
			1342,
			22068,
			32251
		},
		{
			1343,
			22069,
			32252
		},
		{
			1344,
			22070,
			32253
		},
		{
			1345,
			22071,
			32254
		},
		{
			18550,
			32255
		},
	};
	
	/**
	 * Method findArrowForBow.
	 * @param bow ItemTemplate
	 * @return ItemInstance
	 */
	public ItemInstance findArrowForBow(ItemTemplate bow)
	{
		int[] arrowsId = arrows[bow.getCrystalType().externalOrdinal];
		ItemInstance ret = null;
		for (int id : arrowsId)
		{
			if ((ret = getItemByItemId(id)) != null)
			{
				return ret;
			}
		}
		return null;
	}
	
	/**
	 * Field bolts.
	 */
	private static final int[][] bolts =
	{
		{
			9632,
			32256
		},
		{
			9633,
			22144,
			32257
		},
		{
			9634,
			22145,
			32258
		},
		{
			9635,
			22146,
			32259
		},
		{
			9636,
			22147,
			32260
		},
		{
			9637,
			22148,
			32261
		},
		{
			19443,
			32262
		},
	};
	
	/**
	 * Method findArrowForCrossbow.
	 * @param xbow ItemTemplate
	 * @return ItemInstance
	 */
	public ItemInstance findArrowForCrossbow(ItemTemplate xbow)
	{
		int[] boltsId = bolts[xbow.getCrystalType().externalOrdinal];
		ItemInstance ret = null;
		for (int id : boltsId)
		{
			if ((ret = getItemByItemId(id)) != null)
			{
				return ret;
			}
		}
		return null;
	}
	
	/**
	 * Method findEquippedLure.
	 * @return ItemInstance
	 */
	public ItemInstance findEquippedLure()
	{
		ItemInstance res = null;
		int last_lure = 0;
		Player owner = getActor();
		String LastLure = owner.getVar("LastLure");
		if ((LastLure != null) && !LastLure.isEmpty())
		{
			last_lure = Integer.valueOf(LastLure);
		}
		for (ItemInstance temp : getItems())
		{
			if (temp.getItemType() == EtcItemType.BAIT)
			{
				if ((temp.getLocation() == ItemLocation.PAPERDOLL) && (temp.getEquipSlot() == PAPERDOLL_LHAND))
				{
					return temp;
				}
				else if ((last_lure > 0) && (res == null) && (temp.getObjectId() == last_lure))
				{
					res = temp;
				}
			}
		}
		return res;
	}
	
	/**
	 * Method lockItems.
	 * @param lock LockType
	 * @param items int[]
	 */
	public void lockItems(LockType lock, int[] items)
	{
		if (_lockType != LockType.NONE)
		{
			return;
		}
		_lockType = lock;
		_lockItems = items;
		getActor().sendItemList(false);
	}
	
	/**
	 * Method unlock.
	 */
	public void unlock()
	{
		if (_lockType == LockType.NONE)
		{
			return;
		}
		_lockType = LockType.NONE;
		_lockItems = ArrayUtils.EMPTY_INT_ARRAY;
		getActor().sendItemList(false);
	}
	
	/**
	 * Method isLockedItem.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public boolean isLockedItem(ItemInstance item)
	{
		switch (_lockType)
		{
			case INCLUDE:
				return ArrayUtils.contains(_lockItems, item.getItemId());
			case EXCLUDE:
				return !ArrayUtils.contains(_lockItems, item.getItemId());
			default:
				return false;
		}
	}
	
	/**
	 * Method getLockType.
	 * @return LockType
	 */
	public LockType getLockType()
	{
		return _lockType;
	}
	
	/**
	 * Method getLockItems.
	 * @return int[]
	 */
	public int[] getLockItems()
	{
		return _lockItems;
	}
	
	/**
	 * Method onRestoreItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onRestoreItem(ItemInstance item)
	{
		super.onRestoreItem(item);
		if (item.getItemType() == EtcItemType.RUNE)
		{
			_listeners.onEquip(-1, item);
		}
		if (item.isTemporalItem())
		{
			item.startTimer(new LifeTimeTask(item));
		}
		if (item.isCursed())
		{
			CursedWeaponsManager.getInstance().checkPlayer(getActor(), item);
		}
	}
	
	/**
	 * Method onAddItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onAddItem(ItemInstance item)
	{
		super.onAddItem(item);
		if (item.getItemType() == EtcItemType.RUNE)
		{
			_listeners.onEquip(-1, item);
		}
		if (item.isTemporalItem())
		{
			item.startTimer(new LifeTimeTask(item));
		}
		if (item.isCursed())
		{
			CursedWeaponsManager.getInstance().checkPlayer(getActor(), item);
		}
	}
	
	/**
	 * Method onRemoveItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void onRemoveItem(ItemInstance item)
	{
		super.onRemoveItem(item);
		getActor().removeItemFromShortCut(item.getObjectId());
		if (item.getItemType() == EtcItemType.RUNE)
		{
			_listeners.onUnequip(-1, item);
		}
		if (item.isTemporalItem())
		{
			item.stopTimer();
		}
	}
	
	/**
	 * Method onEquip.
	 * @param slot int
	 * @param item ItemInstance
	 */
	@Override
	protected void onEquip(int slot, ItemInstance item)
	{
		super.onEquip(slot, item);
		if (item.isShadowItem())
		{
			item.startTimer(new ShadowLifeTimeTask(item));
		}
	}
	
	/**
	 * Method onUnequip.
	 * @param slot int
	 * @param item ItemInstance
	 */
	@Override
	protected void onUnequip(int slot, ItemInstance item)
	{
		super.onUnequip(slot, item);
		if (item.isShadowItem())
		{
			item.stopTimer();
		}
	}
	
	/**
	 * Method restore.
	 */
	@Override
	public void restore()
	{
		final int ownerId = getOwnerId();
		writeLock();
		try
		{
			Collection<ItemInstance> items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getBaseLocation());
			for (ItemInstance item : items)
			{
				_items.add(item);
				onRestoreItem(item);
			}
			CollectionUtils.eqSort(_items, ItemOrderComparator.getInstance());
			items = _itemsDAO.getItemsByOwnerIdAndLoc(ownerId, getEquipLocation());
			for (ItemInstance item : items)
			{
				_items.add(item);
				onRestoreItem(item);
				if (item.getEquipSlot() >= PAPERDOLL_MAX)
				{
					item.setLocation(getBaseLocation());
					item.setLocData(0);
					item.setEquipped(false);
					continue;
				}
				setPaperdollItem(item.getEquipSlot(), item);
			}
		}
		finally
		{
			writeUnlock();
		}
		DelayedItemsManager.getInstance().loadDelayed(getActor(), false);
		refreshWeight();
	}
	
	/**
	 * Method store.
	 */
	@Override
	public void store()
	{
		writeLock();
		try
		{
			_itemsDAO.update(_items);
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method sendAddItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void sendAddItem(ItemInstance item)
	{
		Player actor = getActor();
		actor.sendPacket(new InventoryUpdate().addNewItem(item));
		if (item.getTemplate().getAgathionEnergy() > 0)
		{
			actor.sendPacket(new ExBR_AgathionEnergyInfo(1, item));
		}
	}
	
	/**
	 * Method sendModifyItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void sendModifyItem(ItemInstance item)
	{
		Player actor = getActor();
		actor.sendPacket(new InventoryUpdate().addModifiedItem(item));
		if (item.getTemplate().getAgathionEnergy() > 0)
		{
			actor.sendPacket(new ExBR_AgathionEnergyInfo(1, item));
		}
	}
	
	/**
	 * Method sendRemoveItem.
	 * @param item ItemInstance
	 */
	@Override
	protected void sendRemoveItem(ItemInstance item)
	{
		getActor().sendPacket(new InventoryUpdate().addRemovedItem(item));
	}
	
	/**
	 * Method startTimers.
	 */
	public void startTimers()
	{
	}
	
	/**
	 * Method stopAllTimers.
	 */
	public void stopAllTimers()
	{
		for (ItemInstance item : getItems())
		{
			if (item.isShadowItem() || item.isTemporalItem())
			{
				item.stopTimer();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class ShadowLifeTimeTask extends RunnableImpl
	{
		/**
		 * Field item.
		 */
		private final ItemInstance item;
		
		/**
		 * Constructor for ShadowLifeTimeTask.
		 * @param item ItemInstance
		 */
		ShadowLifeTimeTask(ItemInstance item)
		{
			this.item = item;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = getActor();
			if (!item.isEquipped())
			{
				return;
			}
			int mana;
			synchronized (item)
			{
				item.setLifeTime(item.getLifeTime() - 1);
				mana = item.getShadowLifeTime();
				if (mana <= 0)
				{
					destroyItem(item);
				}
			}
			SystemMessage sm = null;
			if (mana == 10)
			{
				sm = new SystemMessage(SystemMessage.S1S_REMAINING_MANA_IS_NOW_10);
			}
			else if (mana == 5)
			{
				sm = new SystemMessage(SystemMessage.S1S_REMAINING_MANA_IS_NOW_5);
			}
			else if (mana == 1)
			{
				sm = new SystemMessage(SystemMessage.S1S_REMAINING_MANA_IS_NOW_1_IT_WILL_DISAPPEAR_SOON);
			}
			else if (mana <= 0)
			{
				sm = new SystemMessage(SystemMessage.S1S_REMAINING_MANA_IS_NOW_0_AND_THE_ITEM_HAS_DISAPPEARED);
			}
			else
			{
				player.sendPacket(new InventoryUpdate().addModifiedItem(item));
			}
			if (sm != null)
			{
				sm.addItemName(item.getItemId());
				player.sendPacket(sm);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class LifeTimeTask extends RunnableImpl
	{
		/**
		 * Field item.
		 */
		private final ItemInstance item;
		
		/**
		 * Constructor for LifeTimeTask.
		 * @param item ItemInstance
		 */
		LifeTimeTask(ItemInstance item)
		{
			this.item = item;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			Player player = getActor();
			int left;
			synchronized (item)
			{
				left = item.getTemporalLifeTime();
				if (left <= 0)
				{
					destroyItem(item);
				}
			}
			if (left <= 0)
			{
				player.sendPacket(new SystemMessage(SystemMessage.THE_LIMITED_TIME_ITEM_HAS_BEEN_DELETED).addItemName(item.getItemId()));
			}
		}
	}
}
