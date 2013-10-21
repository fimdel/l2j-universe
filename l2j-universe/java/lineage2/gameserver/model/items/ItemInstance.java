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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.collections.GArray;
import lineage2.commons.dao.JdbcEntity;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.dao.ItemsDAO;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.attachment.ItemAttachment;
import lineage2.gameserver.model.items.etcitems.LifeStoneGrade;
import lineage2.gameserver.model.items.listeners.ItemEnchantOptionsListener;
import lineage2.gameserver.network.serverpackets.DropItem;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SpawnItem;
import lineage2.gameserver.scripts.Events;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.taskmanager.ItemsAutoDestroy;
import lineage2.gameserver.taskmanager.LazyPrecisionTaskManager;
import lineage2.gameserver.templates.item.ExItemType;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;
import lineage2.gameserver.templates.item.ItemTemplate.ItemClass;
import lineage2.gameserver.templates.item.ItemType;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

import org.napile.primitive.Containers;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

public final class ItemInstance extends GameObject implements JdbcEntity
{
	/**
	 * Field EMPTY_ENCHANT_OPTIONS.
	 */
	public static final int[] EMPTY_ENCHANT_OPTIONS = new int[3];
	
	/**
	 * Field serialVersionUID. (value is 3162753878915133228)
	 */
	private static final long serialVersionUID = 3162753878915133228L;
	
	/**
	 * Field _itemsDAO.
	 */
	private static final ItemsDAO _itemsDAO = ItemsDAO.getInstance();
	
	/**
	 * @author Mobius
	 */
	public static enum ItemLocation
	{
		/**
		 * Field VOID.
		 */
		VOID,
		/**
		 * Field INVENTORY.
		 */
		INVENTORY,
		/**
		 * Field PAPERDOLL.
		 */
		PAPERDOLL,
		/**
		 * Field PET_INVENTORY.
		 */
		PET_INVENTORY,
		/**
		 * Field PET_PAPERDOLL.
		 */
		PET_PAPERDOLL,
		/**
		 * Field WAREHOUSE.
		 */
		WAREHOUSE,
		/**
		 * Field CLANWH.
		 */
		CLANWH,
		/**
		 * Field FREIGHT.
		 */
		FREIGHT,
		/**
		 * Field LEASE.
		 */
		@Deprecated
		LEASE,
		/**
		 * Field MAIL.
		 */
		MAIL,
		/**
		 * Field COMMISSION.
		 */
		COMMISSION
	}
	
	/**
	 * Field CHARGED_NONE. (value is 0)
	 */
	public static final int CHARGED_NONE = 0;
	/**
	 * Field CHARGED_SOULSHOT. (value is 1)
	 */
	public static final int CHARGED_SOULSHOT = 1;
	/**
	 * Field CHARGED_SPIRITSHOT. (value is 1)
	 */
	public static final int CHARGED_SPIRITSHOT = 1;
	/**
	 * Field CHARGED_BLESSED_SPIRITSHOT. (value is 2)
	 */
	public static final int CHARGED_BLESSED_SPIRITSHOT = 2;
	
	/**
	 * Field FLAG_NO_DROP.
	 */
	public static final int FLAG_NO_DROP = 1 << 0;
	/**
	 * Field FLAG_NO_TRADE.
	 */
	public static final int FLAG_NO_TRADE = 1 << 1;
	/**
	 * Field FLAG_NO_TRANSFER.
	 */
	public static final int FLAG_NO_TRANSFER = 1 << 2;
	/**
	 * Field FLAG_NO_CRYSTALLIZE.
	 */
	public static final int FLAG_NO_CRYSTALLIZE = 1 << 3;
	/**
	 * Field FLAG_NO_ENCHANT.
	 */
	public static final int FLAG_NO_ENCHANT = 1 << 4;
	/**
	 * Field FLAG_NO_DESTROY.
	 */
	public static final int FLAG_NO_DESTROY = 1 << 5;
	
	/**
	 * Field ownerId.
	 */
	private int ownerId;
	/**
	 * Field itemId.
	 */
	private int itemId;
	/**
	 * Field count.
	 */
	private long count;
	/**
	 * Field enchantLevel.
	 */
	private int enchantLevel = -1;
	/**
	 * Field loc.
	 */
	private ItemLocation loc;
	/**
	 * Field locData.
	 */
	private int locData;
	/**
	 * Field customType1.
	 */
	private int customType1;
	/**
	 * Field customType2.
	 */
	private int customType2;
	/**
	 * Field lifeTime.
	 */
	private int lifeTime;
	/**
	 * Field customFlags.
	 */
	private int customFlags;
	/**
	 * Field attrs.
	 */
	private ItemAttributes attrs = new ItemAttributes();
	/**
	 * Field _enchantOptions.
	 */
	private int[] _enchantOptions = EMPTY_ENCHANT_OPTIONS;
	/**
	 * Field template.
	 */
	private ItemTemplate template;
	/**
	 * Field isEquipped.
	 */
	private boolean isEquipped;
	/**
	 * Field _dropTime.
	 */
	private long _dropTime;
	
	/**
	 * Field _dropPlayers.
	 */
	private IntSet _dropPlayers = Containers.EMPTY_INT_SET;
	/**
	 * Field _dropTimeOwner.
	 */
	private long _dropTimeOwner;
	
	/**
	 * Field _chargedSoulshot.
	 */
	private int _chargedSoulshot = CHARGED_NONE;
	/**
	 * Field _chargedSpiritshot.
	 */
	private int _chargedSpiritshot = CHARGED_NONE;
	
	/**
	 * Field _chargedFishtshot.
	 */
	private boolean _chargedFishtshot = false;
	private int _visualId;
	/**
	 * Field _augmentationId.
	 */
	private int _augmentationId;
	/**
	 * Field _agathionEnergy.
	 */
	private int _agathionEnergy;
	
	/**
	 * Field _attachment.
	 */
	private ItemAttachment _attachment;
	/**
	 * Field _state.
	 */
	private JdbcEntityState _state = JdbcEntityState.CREATED;
	
	/**
	 * Constructor for ItemInstance.
	 * @param objectId int
	 */
	public ItemInstance(int objectId)
	{
		super(objectId);
	}
	
	/**
	 * Constructor for ItemInstance.
	 * @param objectId int
	 * @param itemId int
	 */
	public ItemInstance(int objectId, int itemId)
	{
		super(objectId);
		setItemId(itemId);
		setLifeTime(getTemplate().isTemporal() ? (int) (System.currentTimeMillis() / 1000L) + (getTemplate().getDurability() * 60) : getTemplate().getDurability());
		setAgathionEnergy(getTemplate().getAgathionEnergy());
		setLocData(-1);
		setEnchantLevel(0);
	}
	
	/**
	 * Method getOwnerId.
	 * @return int
	 */
	public int getOwnerId()
	{
		return ownerId;
	}
	
	/**
	 * Method setOwnerId.
	 * @param ownerId int
	 */
	public void setOwnerId(int ownerId)
	{
		this.ownerId = ownerId;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return itemId;
	}
	
	/**
	 * Method setItemId.
	 * @param id int
	 */
	public void setItemId(int id)
	{
		itemId = id;
		template = ItemHolder.getInstance().getTemplate(id);
		setCustomFlags(getCustomFlags());
	}
	
	/**
	 * Method getCount.
	 * @return long
	 */
	public long getCount()
	{
		return count;
	}
	
	/**
	 * Method setCount.
	 * @param count long
	 */
	public void setCount(long count)
	{
		if (count < 0)
		{
			count = 0;
		}
		
		if (!isStackable() && (count > 1L))
		{
			this.count = 1L;
			return;
		}
		
		this.count = count;
	}
	
	/**
	 * Method getEnchantLevel.
	 * @return int
	 */
	public int getEnchantLevel()
	{
		return enchantLevel;
	}
	
	/**
	 * Method setEnchantLevel.
	 * @param enchantLevel int
	 */
	public void setEnchantLevel(int enchantLevel)
	{
		final int old = this.enchantLevel;
		
		this.enchantLevel = enchantLevel;
		
		if ((old != this.enchantLevel) && (getTemplate().getEnchantOptions().size() > 0))
		{
			Player player = GameObjectsStorage.getPlayer(ownerId);
			
			if (isEquipped() && (player != null))
			{
				ItemEnchantOptionsListener.getInstance().onUnequip(getEquipSlot(), this, player);
			}
			
			int[] enchantOptions = getTemplate().getEnchantOptions().get(this.enchantLevel);
			
			_enchantOptions = enchantOptions == null ? EMPTY_ENCHANT_OPTIONS : enchantOptions;
			
			if (isEquipped() && (player != null))
			{
				ItemEnchantOptionsListener.getInstance().onEquip(getEquipSlot(), this, player);
			}
		}
	}
	
	/**
	 * Method setLocName.
	 * @param loc String
	 */
	public void setLocName(String loc)
	{
		this.loc = ItemLocation.valueOf(loc);
	}
	
	/**
	 * Method getLocName.
	 * @return String
	 */
	public String getLocName()
	{
		return loc.name();
	}
	
	/**
	 * Method setLocation.
	 * @param loc ItemLocation
	 */
	public void setLocation(ItemLocation loc)
	{
		this.loc = loc;
	}
	
	/**
	 * Method getLocation.
	 * @return ItemLocation
	 */
	public ItemLocation getLocation()
	{
		return loc;
	}
	
	/**
	 * Method setLocData.
	 * @param locData int
	 */
	public void setLocData(int locData)
	{
		this.locData = locData;
	}
	
	/**
	 * Method getLocData.
	 * @return int
	 */
	public int getLocData()
	{
		return locData;
	}
	
	/**
	 * Method getCustomType1.
	 * @return int
	 */
	public int getCustomType1()
	{
		return customType1;
	}
	
	/**
	 * Method setCustomType1.
	 * @param newtype int
	 */
	public void setCustomType1(int newtype)
	{
		customType1 = newtype;
	}
	
	/**
	 * Method getCustomType2.
	 * @return int
	 */
	public int getCustomType2()
	{
		return customType2;
	}
	
	/**
	 * Method setCustomType2.
	 * @param newtype int
	 */
	public void setCustomType2(int newtype)
	{
		customType2 = newtype;
	}
	
	/**
	 * Method getLifeTime.
	 * @return int
	 */
	public int getLifeTime()
	{
		return lifeTime;
	}
	
	/**
	 * Method setLifeTime.
	 * @param lifeTime int
	 */
	public void setLifeTime(int lifeTime)
	{
		this.lifeTime = Math.max(0, lifeTime);
	}
	
	/**
	 * Method getCustomFlags.
	 * @return int
	 */
	public int getCustomFlags()
	{
		return customFlags;
	}
	
	/**
	 * Method setCustomFlags.
	 * @param flags int
	 */
	public void setCustomFlags(int flags)
	{
		customFlags = flags;
	}
	
	/**
	 * Method getAttributes.
	 * @return ItemAttributes
	 */
	public ItemAttributes getAttributes()
	{
		return attrs;
	}
	
	/**
	 * Method setAttributes.
	 * @param attrs ItemAttributes
	 */
	public void setAttributes(ItemAttributes attrs)
	{
		this.attrs = attrs;
	}
	
	/**
	 * Method getShadowLifeTime.
	 * @return int
	 */
	public int getShadowLifeTime()
	{
		if (!isShadowItem())
		{
			return 0;
		}
		return getLifeTime();
	}
	
	/**
	 * Method getTemporalLifeTime.
	 * @return int
	 */
	public int getTemporalLifeTime()
	{
		if (!isTemporalItem())
		{
			return 0;
		}
		return getLifeTime() - (int) (System.currentTimeMillis() / 1000L);
	}
	
	/**
	 * Field _timerTask.
	 */
	private ScheduledFuture<?> _timerTask;
	
	/**
	 * Method startTimer.
	 * @param r Runnable
	 */
	public void startTimer(Runnable r)
	{
		_timerTask = LazyPrecisionTaskManager.getInstance().scheduleAtFixedRate(r, 0, 60000L);
	}
	
	/**
	 * Method stopTimer.
	 */
	public void stopTimer()
	{
		if (_timerTask != null)
		{
			_timerTask.cancel(false);
			_timerTask = null;
		}
	}
	
	/**
	 * Method isEquipable.
	 * @return boolean
	 */
	public boolean isEquipable()
	{
		return template.isEquipable();
	}
	
	/**
	 * Method isEquipped.
	 * @return boolean
	 */
	public boolean isEquipped()
	{
		return isEquipped;
	}
	
	/**
	 * Method setEquipped.
	 * @param isEquipped boolean
	 */
	public void setEquipped(boolean isEquipped)
	{
		this.isEquipped = isEquipped;
	}
	
	/**
	 * Method getBodyPart.
	 * @return int
	 */
	public int getBodyPart()
	{
		return template.getBodyPart();
	}
	
	/**
	 * Method getEquipSlot.
	 * @return int
	 */
	public int getEquipSlot()
	{
		return getLocData();
	}
	
	/**
	 * Method getTemplate.
	 * @return ItemTemplate
	 */
	public ItemTemplate getTemplate()
	{
		return template;
	}
	
	/**
	 * Method setDropTime.
	 * @param time long
	 */
	public void setDropTime(long time)
	{
		_dropTime = time;
	}
	
	/**
	 * Method getLastDropTime.
	 * @return long
	 */
	public long getLastDropTime()
	{
		return _dropTime;
	}
	
	/**
	 * Method getDropTimeOwner.
	 * @return long
	 */
	public long getDropTimeOwner()
	{
		return _dropTimeOwner;
	}
	
	/**
	 * Method getItemType.
	 * @return ItemType
	 */
	public ItemType getItemType()
	{
		return template.getItemType();
	}
	
	public ExItemType getExItemType()
	{
		return template.getExItemType();
	}

	/**
	 * Method isArmor.
	 * @return boolean
	 */
	public boolean isArmor()
	{
		return template.isArmor();
	}
	
	/**
	 * Method isAccessory.
	 * @return boolean
	 */
	public boolean isAccessory()
	{
		return template.isAccessory();
	}
	
	/**
	 * Method isWeapon.
	 * @return boolean
	 */
	public boolean isWeapon()
	{
		return template.isWeapon();
	}
	
	/**
	 * Method getReferencePrice.
	 * @return int
	 */
	public int getReferencePrice()
	{
		return template.getReferencePrice();
	}
	
	/**
	 * Method isStackable.
	 * @return boolean
	 */
	public boolean isStackable()
	{
		return template.isStackable();
	}
	
	@Override
	public void onActionSelect(final Player player, final boolean forced)
	{
		if(Events.onAction(player, this, forced))
		{
			return;
		}

		if(player.isAlikeDead())
		{
			return;
		}

		if(player.isCursedWeaponEquipped() && CursedWeaponsManager.getInstance().isCursed(itemId))
		{
			return;
		}

		player.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, this, null);
	}
	
	/**
	 * Method isAugmented.
	 * @return boolean
	 */
	public boolean isAugmented()
	{
		return getAugmentationId() != 0;
	}
	
	/**
	 * Method getAugmentationId.
	 * @return int
	 */
	public int getAugmentationId()
	{
		return _augmentationId;
	}
	
	/**
	 * Method setAugmentationId.
	 * @param val int
	 */
	public void setAugmentationId(int val)
	{
		_augmentationId = val;
	}
	
	/**
	 * Returns the type of charge with SoulShot of the item.
	 * @return int (CHARGED_NONE, CHARGED_SOULSHOT)
	 */
	public int getChargedSoulshot()
	{
		return _chargedSoulshot;
	}
	
	/**
	 * Method getChargedSpiritshot.
	 * @return int
	 */
	public int getChargedSpiritshot()
	{
		return _chargedSpiritshot;
	}
	
	/**
	 * Method getChargedFishshot.
	 * @return boolean
	 */
	public boolean getChargedFishshot()
	{
		return _chargedFishtshot;
	}
	
	/**
	 * Method setChargedSoulshot.
	 * @param type int
	 */
	public void setChargedSoulshot(int type)
	{
		_chargedSoulshot = type;
	}
	
	/**
	 * Method setChargedSpiritshot.
	 * @param type int
	 */
	public void setChargedSpiritshot(int type)
	{
		_chargedSpiritshot = type;
	}
	
	/**
	 * Method setChargedFishshot.
	 * @param type boolean
	 */
	public void setChargedFishshot(boolean type)
	{
		_chargedFishtshot = type;
	}
	
	/**
	 * @author Mobius
	 */
	public class FuncAttack extends Func
	{
		/**
		 * Field element.
		 */
		private final Element element;
		
		/**
		 * Constructor for FuncAttack.
		 * @param element Element
		 * @param order int
		 * @param owner Object
		 */
		public FuncAttack(Element element, int order, Object owner)
		{
			super(element.getAttack(), order, owner);
			this.element = element;
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value += getAttributeElementValue(element, true);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class FuncDefence extends Func
	{
		/**
		 * Field element.
		 */
		private final Element element;
		
		/**
		 * Constructor for FuncDefence.
		 * @param element Element
		 * @param order int
		 * @param owner Object
		 */
		public FuncDefence(Element element, int order, Object owner)
		{
			super(element.getDefence(), order, owner);
			this.element = element;
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value += getAttributeElementValue(element, true);
		}
	}
	
	/**
	 * Method getStatFuncs.
	 * @return Func[]
	 */
	public Func[] getStatFuncs()
	{
		Func[] result = Func.EMPTY_FUNC_ARRAY;
		
		GArray<Func> funcs = new GArray<Func>();
		
		if (template.getAttachedFuncs().length > 0)
		{
			for (FuncTemplate t : template.getAttachedFuncs())
			{
				Func f = t.getFunc(this);
				if (f != null)
				{
					funcs.add(f);
				}
			}
		}
		
		for (Element e : Element.VALUES)
		{
			if (isWeapon())
			{
				funcs.add(new FuncAttack(e, 0x40, this));
			}
			if (isArmor())
			{
				funcs.add(new FuncDefence(e, 0x40, this));
			}
		}
		
		if (!funcs.isEmpty())
		{
			result = funcs.toArray(new Func[funcs.size()]);
		}
		
		funcs.clear();
		
		return result;
	}
	
	/**
	 * Method isHeroWeapon.
	 * @return boolean
	 */
	public boolean isHeroWeapon()
	{
		return template.isHeroWeapon();
	}
	
	/**
	 * Method canBeDestroyed.
	 * @param player Player
	 * @return boolean
	 */
	public boolean canBeDestroyed(Player player)
	{
		if ((customFlags & FLAG_NO_DESTROY) == FLAG_NO_DESTROY)
		{
			return false;
		}
		
		if (isHeroWeapon())
		{
			return false;
		}
		
		if (PetDataTable.isPetControlItem(this) && player.isMounted())
		{
			return false;
		}
		
		if (player.getPetControlItem() == this)
		{
			return false;
		}
		
		if (player.getEnchantScroll() == this)
		{
			return false;
		}
		
		if (isCursed())
		{
			return false;
		}
		
		return template.isDestroyable();
	}
	
	/**
	 * Method canBeDropped.
	 * @param player Player
	 * @param pk boolean
	 * @return boolean
	 */
	public boolean canBeDropped(Player player, boolean pk)
	{
		if (player.isGM())
		{
			return true;
		}
		
		if ((customFlags & FLAG_NO_DROP) == FLAG_NO_DROP)
		{
			return false;
		}
		
		if (isShadowItem())
		{
			return false;
		}
		
		if (isTemporalItem())
		{
			return false;
		}
		
		if (isAugmented() && (!pk || !Config.DROP_ITEMS_AUGMENTED) && !Config.ALT_ALLOW_DROP_AUGMENTED)
		{
			return false;
		}
		
		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}
		
		return template.isDropable();
	}
	
	/**
	 * Method canBeTraded.
	 * @param player Player
	 * @return boolean
	 */
	public boolean canBeTraded(Player player)
	{
		if (isEquipped())
		{
			return false;
		}
		
		if (player.isGM())
		{
			return true;
		}
		
		if ((customFlags & FLAG_NO_TRADE) == FLAG_NO_TRADE)
		{
			return false;
		}
		
		if (isShadowItem())
		{
			return false;
		}
		
		if (isTemporalItem())
		{
			return false;
		}
		
		if (isAugmented() && !Config.ALT_ALLOW_DROP_AUGMENTED)
		{
			return false;
		}
		
		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}
		
		return template.isTradeable();
	}
	
	/**
	 * Method canBeSold.
	 * @param player Player
	 * @return boolean
	 */
	public boolean canBeSold(Player player)
	{
		if ((customFlags & FLAG_NO_DESTROY) == FLAG_NO_DESTROY)
		{
			return false;
		}
		
		if (getItemId() == ItemTemplate.ITEM_ID_ADENA)
		{
			return false;
		}
		
		if (template.getReferencePrice() == 0)
		{
			return false;
		}
		
		if (isShadowItem())
		{
			return false;
		}
		
		if (isTemporalItem())
		{
			return false;
		}
		
		if (isAugmented() && !Config.ALT_ALLOW_DROP_AUGMENTED)
		{
			return false;
		}
		
		if (isEquipped())
		{
			return false;
		}
		
		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}
		
		return template.isSellable();
	}
	
	/**
	 * Method canBeStored.
	 * @param player Player
	 * @param privatewh boolean
	 * @return boolean
	 */
	public boolean canBeStored(Player player, boolean privatewh)
	{
		if ((customFlags & FLAG_NO_TRANSFER) == FLAG_NO_TRANSFER)
		{
			return false;
		}
		
		if (!getTemplate().isStoreable())
		{
			return false;
		}
		
		if (!privatewh && (isShadowItem() || isTemporalItem()))
		{
			return false;
		}
		
		if (!privatewh && isAugmented() && !Config.ALT_ALLOW_DROP_AUGMENTED)
		{
			return false;
		}
		
		if (isEquipped())
		{
			return false;
		}
		
		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}
		
		return privatewh || template.isTradeable();
	}
	
	/**
	 * Method canBeCrystallized.
	 * @param player Player
	 * @return boolean
	 */
	public boolean canBeCrystallized(Player player)
	{
		if ((customFlags & FLAG_NO_CRYSTALLIZE) == FLAG_NO_CRYSTALLIZE)
		{
			return false;
		}
		
		if (isShadowItem())
		{
			return false;
		}
		
		if (isTemporalItem())
		{
			return false;
		}
		
		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}
		
		return template.isCrystallizable();
	}
	
	/**
	 * Method canBeEnchanted.
	 * @return boolean
	 */
	public boolean canBeEnchanted()
	{
		if ((customFlags & FLAG_NO_ENCHANT) == FLAG_NO_ENCHANT)
		{
			return false;
		}
		
		return template.canBeEnchanted();
	}
	
	/**
	 * Method canBeAugmented.
	 * @param player Player
	 * @param lsg LifeStoneGrade
	 * @return boolean
	 */
	public boolean canBeAugmented(Player player, LifeStoneGrade lsg)
	{
		if (!canBeEnchanted())
		{
			return false;
		}
		
		if (isAugmented())
		{
			return false;
		}
		
		if (isCommonItem())
		{
			return false;
		}
		
		if (isTerritoryAccessory())
		{
			return false;
		}
		
		if (getTemplate().getItemGrade().ordinal() < Grade.C.ordinal())
		{
			return false;
		}
		
		if (!getTemplate().isAugmentable())
		{
			return false;
		}
		
		if (isAccessory())
		{
			return lsg == LifeStoneGrade.ACCESSORY;
		}
		
		if (isArmor())
		{
			return Config.ALT_ALLOW_AUGMENT_ALL;
		}
		
		if (isWeapon())
		{
			return ((lsg != LifeStoneGrade.ACCESSORY) && (lsg != LifeStoneGrade.UNDERWEAR));
		}
		
		return true;
	}
	
	/**
	 * Method canBeExchanged.
	 * @param player Player
	 * @return boolean
	 */
	public boolean canBeExchanged(Player player)
	{
		if ((customFlags & FLAG_NO_DESTROY) == FLAG_NO_DESTROY)
		{
			return false;
		}
		
		if (isShadowItem())
		{
			return false;
		}
		
		if (isTemporalItem())
		{
			return false;
		}
		
		if (!ItemFunctions.checkIfCanDiscard(player, this))
		{
			return false;
		}
		
		return template.isDestroyable();
	}
	
	/**
	 * Method isTerritoryAccessory.
	 * @return boolean
	 */
	public boolean isTerritoryAccessory()
	{
		return template.isTerritoryAccessory();
	}
	
	/**
	 * Method isShadowItem.
	 * @return boolean
	 */
	public boolean isShadowItem()
	{
		return template.isShadowItem();
	}
	
	/**
	 * Method isTemporalItem.
	 * @return boolean
	 */
	public boolean isTemporalItem()
	{
		return template.isTemporal();
	}
	
	/**
	 * Method isCommonItem.
	 * @return boolean
	 */
	public boolean isCommonItem()
	{
		return template.isCommonItem();
	}
	
	/**
	 * Method isAltSeed.
	 * @return boolean
	 */
	public boolean isAltSeed()
	{
		return template.isAltSeed();
	}
	
	/**
	 * Method isCursed.
	 * @return boolean
	 */
	public boolean isCursed()
	{
		return template.isCursed();
	}
	
	/**
	 * Method dropToTheGround.
	 * @param lastAttacker Player
	 * @param fromNpc NpcInstance
	 */
	public void dropToTheGround(Player lastAttacker, NpcInstance fromNpc)
	{
		Creature dropper = fromNpc;
		if (dropper == null)
		{
			dropper = lastAttacker;
		}
		
		Location pos = Location.findAroundPosition(dropper, 100);
		
		if (lastAttacker != null)
		{
			_dropPlayers = new HashIntSet(1, 2);
			for (Player $member : lastAttacker.getPlayerGroup())
			{
				_dropPlayers.add($member.getObjectId());
			}
			
			_dropTimeOwner = System.currentTimeMillis() + Config.NONOWNER_ITEM_PICKUP_DELAY + ((fromNpc != null) && fromNpc.isRaid() ? 285000 : 0);
		}
		
		dropMe(dropper, pos);
		
		if (isHerb())
		{
			ItemsAutoDestroy.getInstance().addHerb(this);
		}
		else if ((Config.AUTODESTROY_ITEM_AFTER > 0) && !isCursed())
		{
			ItemsAutoDestroy.getInstance().addItem(this);
		}
	}
	
	/**
	 * Method dropToTheGround.
	 * @param dropper Creature
	 * @param dropPos Location
	 */
	public void dropToTheGround(Creature dropper, Location dropPos)
	{
		if (GeoEngine.canMoveToCoord(dropper.getX(), dropper.getY(), dropper.getZ(), dropPos.x, dropPos.y, dropPos.z, dropper.getGeoIndex()))
		{
			dropMe(dropper, dropPos);
		}
		else
		{
			dropMe(dropper, dropper.getLoc());
		}
	}
	
	/**
	 * Method dropToTheGround.
	 * @param dropper Playable
	 * @param dropPos Location
	 */
	public void dropToTheGround(Playable dropper, Location dropPos)
	{
		setLocation(ItemLocation.VOID);
		if (getJdbcState().isPersisted())
		{
			setJdbcState(JdbcEntityState.UPDATED);
			update();
		}
		
		if (GeoEngine.canMoveToCoord(dropper.getX(), dropper.getY(), dropper.getZ(), dropPos.x, dropPos.y, dropPos.z, dropper.getGeoIndex()))
		{
			dropMe(dropper, dropPos);
		}
		else
		{
			dropMe(dropper, dropper.getLoc());
		}
	}
	
	/**
	 * Method dropMe.
	 * @param dropper Creature
	 * @param loc Location
	 */
	public void dropMe(Creature dropper, Location loc)
	{
		if (dropper != null)
		{
			setReflection(dropper.getReflection());
		}
		
		spawnMe0(loc, dropper);
	}
	
	/**
	 * Method pickupMe.
	 */
	public final void pickupMe()
	{
		decayMe();
		setReflection(ReflectionManager.DEFAULT);
	}
	
	/**
	 * Method getItemClass.
	 * @return ItemClass
	 */
	public ItemClass getItemClass()
	{
		return template.getItemClass();
	}
	
	/**
	 * Method getDefence.
	 * @param element Element
	 * @return int
	 */
	private int getDefence(Element element)
	{
		return isArmor() ? getAttributeElementValue(element, true) : 0;
	}
	
	/**
	 * Method getDefenceFire.
	 * @return int
	 */
	public int getDefenceFire()
	{
		return getDefence(Element.FIRE);
	}
	
	/**
	 * Method getDefenceWater.
	 * @return int
	 */
	public int getDefenceWater()
	{
		return getDefence(Element.WATER);
	}
	
	/**
	 * Method getDefenceWind.
	 * @return int
	 */
	public int getDefenceWind()
	{
		return getDefence(Element.WIND);
	}
	
	/**
	 * Method getDefenceEarth.
	 * @return int
	 */
	public int getDefenceEarth()
	{
		return getDefence(Element.EARTH);
	}
	
	/**
	 * Method getDefenceHoly.
	 * @return int
	 */
	public int getDefenceHoly()
	{
		return getDefence(Element.HOLY);
	}
	
	/**
	 * Method getDefenceUnholy.
	 * @return int
	 */
	public int getDefenceUnholy()
	{
		return getDefence(Element.UNHOLY);
	}
	
	/**
	 * Method getAttributeElementValue.
	 * @param element Element
	 * @param withBase boolean
	 * @return int
	 */
	public int getAttributeElementValue(Element element, boolean withBase)
	{
		return attrs.getValue(element) + (withBase ? template.getBaseAttributeValue(element) : 0);
	}
	
	/**
	 * Method getAttributeElement.
	 * @return Element
	 */
	public Element getAttributeElement()
	{
		return attrs.getElement();
	}
	
	/**
	 * Method getAttributeElementValue.
	 * @return int
	 */
	public int getAttributeElementValue()
	{
		return attrs.getValue();
	}
	
	/**
	 * Method getAttackElement.
	 * @return Element
	 */
	public Element getAttackElement()
	{
		Element element = isWeapon() ? getAttributeElement() : Element.NONE;
		if (element == Element.NONE)
		{
			for (Element e : Element.VALUES)
			{
				if (template.getBaseAttributeValue(e) > 0)
				{					
					return e;
				}
			}
		}
		return element;
	}
	
	/**
	 * Method getAttackElementValue.
	 * @return int
	 */
	public int getAttackElementValue()
	{
		return isWeapon() ? getAttributeElementValue(getAttackElement(), true) : 0;
	}
	
	/**
	 * Method setAttributeElement.
	 * @param element Element
	 * @param value int
	 */
	public void setAttributeElement(Element element, int value)
	{
		attrs.setValue(element, value);
	}
	
	/**
	 * Method isHerb.
	 * @return boolean
	 */
	public boolean isHerb()
	{
		return getTemplate().isHerb();
	}
	
	/**
	 * Method getCrystalType.
	 * @return Grade
	 */
	public Grade getCrystalType()
	{
		return template.getCrystalType();
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	@Override
	public String getName()
	{
		return getTemplate().getName();
	}
	
	/**
	 * Method save.
	 * @see lineage2.commons.dao.JdbcEntity#save()
	 */
	@Override
	public void save()
	{
		_itemsDAO.save(this);
	}
	
	/**
	 * Method update.
	 * @see lineage2.commons.dao.JdbcEntity#update()
	 */
	@Override
	public void update()
	{
		_itemsDAO.update(this);
	}
	
	/**
	 * Method delete.
	 * @see lineage2.commons.dao.JdbcEntity#delete()
	 */
	@Override
	public void delete()
	{
		_itemsDAO.delete(this);
	}
	
	/**
	 * Method addPacketList.
	 * @param forPlayer Player
	 * @param dropper Creature
	 * @return List<L2GameServerPacket>
	 */
	@Override
	public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		L2GameServerPacket packet = null;
		if (dropper != null)
		{
			packet = new DropItem(this, dropper.getObjectId());
		}
		else
		{
			packet = new SpawnItem(this);
		}
		
		return Collections.singletonList(packet);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(getTemplate().getItemId());
		sb.append(' ');
		if (getEnchantLevel() > 0)
		{
			sb.append('+');
			sb.append(getEnchantLevel());
			sb.append(' ');
		}
		sb.append(getTemplate().getName());
		if (!getTemplate().getAdditionalName().isEmpty())
		{
			sb.append(' ');
			sb.append('\\').append(getTemplate().getAdditionalName()).append('\\');
		}
		sb.append(' ');
		sb.append('(');
		sb.append(getCount());
		sb.append(')');
		sb.append('[');
		sb.append(getObjectId());
		sb.append(']');
		
		return sb.toString();
		
	}
	
	/**
	 * Method setJdbcState.
	 * @param state JdbcEntityState
	 * @see lineage2.commons.dao.JdbcEntity#setJdbcState(JdbcEntityState)
	 */
	@Override
	public void setJdbcState(JdbcEntityState state)
	{
		_state = state;
	}
	
	/**
	 * Method getJdbcState.
	 * @return JdbcEntityState * @see lineage2.commons.dao.JdbcEntity#getJdbcState()
	 */
	@Override
	public JdbcEntityState getJdbcState()
	{
		return _state;
	}
	
	/**
	 * Method isItem.
	 * @return boolean
	 */
	@Override
	public boolean isItem()
	{
		return true;
	}
	
	/**
	 * Method getAttachment.
	 * @return ItemAttachment
	 */
	public ItemAttachment getAttachment()
	{
		return _attachment;
	}
	
	/**
	 * Method setAttachment.
	 * @param attachment ItemAttachment
	 */
	public void setAttachment(ItemAttachment attachment)
	{
		ItemAttachment old = _attachment;
		_attachment = attachment;
		if (_attachment != null)
		{
			_attachment.setItem(this);
		}
		if (old != null)
		{
			old.setItem(null);
		}
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
	 * Method setAgathionEnergy.
	 * @param agathionEnergy int
	 */
	public void setAgathionEnergy(int agathionEnergy)
	{
		_agathionEnergy = agathionEnergy;
	}
	
	/**
	 * Method getEnchantOptions.
	 * @return int[]
	 */
	public int[] getEnchantOptions()
	{
		return _enchantOptions;
	}
	
	public int getVisualId()
	{
		return _visualId;
	}

	public void setVisualId(int val)
	{
		_visualId = val;
	}

	public IntSet getDropPlayers()
	{
		return _dropPlayers;
	}

	public boolean isOther()
	{
		if(!isAccessory() || !isWeapon() || !isArmor())
		{
			return true;
		}
		return false;
	}

	public boolean canBeAppearance()
	{
		if(isAccessory() || isWeapon() || isArmor())
		{
			return true;
		}
		return false;
	}
}