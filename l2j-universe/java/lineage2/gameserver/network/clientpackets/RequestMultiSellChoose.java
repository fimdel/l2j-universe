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
package lineage2.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.math.SafeMath;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.MultiSellEntry;
import lineage2.gameserver.model.base.MultiSellIngredient;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemAttributes;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestMultiSellChoose extends L2GameClientPacket
{
	/**
	 * Field _listId.
	 */
	private int _listId;
	/**
	 * Field _entryId.
	 */
	private int _entryId;
	/**
	 * Field _amount.
	 */
	private long _amount;
	
	/**
	 * @author Mobius
	 */
	private class ItemData
	{
		/**
		 * Field _id.
		 */
		private final int _id;
		/**
		 * Field _count.
		 */
		private final long _count;
		/**
		 * Field _item.
		 */
		private final ItemInstance _item;
		
		/**
		 * Constructor for ItemData.
		 * @param id int
		 * @param count long
		 * @param item ItemInstance
		 */
		public ItemData(int id, long count, ItemInstance item)
		{
			_id = id;
			_count = count;
			_item = item;
		}
		
		/**
		 * Method getId.
		 * @return int
		 */
		public int getId()
		{
			return _id;
		}
		
		/**
		 * Method getCount.
		 * @return long
		 */
		public long getCount()
		{
			return _count;
		}
		
		/**
		 * Method getItem.
		 * @return ItemInstance
		 */
		public ItemInstance getItem()
		{
			return _item;
		}
		
		/**
		 * Method equals.
		 * @param obj Object
		 * @return boolean
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof ItemData))
			{
				return false;
			}
			
			ItemData i = (ItemData) obj;
			
			return (_id == i._id) && (_count == i._count) && (_item == i._item);
		}
	}
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_listId = readD();
		_entryId = readD();
		_amount = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_amount < 1))
		{
			return;
		}
		
		MultiSellListContainer list1 = activeChar.getMultisell();
		if (list1 == null)
		{
			activeChar.sendActionFailed();
			activeChar.setMultisell(null);
			return;
		}
		
		if (list1.getListId() != _listId)
		{
			activeChar.sendActionFailed();
			activeChar.setMultisell(null);
			return;
		}
		
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		
		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (activeChar.getKarma() < 0) && !activeChar.isGM())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		MultiSellEntry entry = null;
		for (MultiSellEntry $entry : list1.getEntries())
		{
			if ($entry.getEntryId() == _entryId)
			{
				entry = $entry;
				break;
			}
		}
		
		if (entry == null)
		{
			return;
		}
		
		final boolean keepenchant = list1.isKeepEnchant();
		final boolean notax = list1.isNoTax();
		final List<ItemData> items = new ArrayList<>();
		
		PcInventory inventory = activeChar.getInventory();
		
		long totalPrice = 0;
		
		NpcInstance merchant = activeChar.getLastNpc();
		Castle castle = merchant != null ? merchant.getCastle(activeChar) : null;
		
		inventory.writeLock();
		try
		{
			long tax = SafeMath.mulAndCheck(entry.getTax(), _amount);
			
			long slots = 0;
			long weight = 0;
			for (MultiSellIngredient i : entry.getProduction())
			{
				if (i.getItemId() <= 0)
				{
					continue;
				}
				ItemTemplate item = ItemHolder.getInstance().getTemplate(i.getItemId());
				
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(SafeMath.mulAndCheck(i.getItemCount(), _amount), item.getWeight()));
				if (item.isStackable())
				{
					if (inventory.getItemByItemId(i.getItemId()) == null)
					{
						slots++;
					}
				}
				else
				{
					slots = SafeMath.addAndCheck(slots, _amount);
				}
			}
			
			if (!inventory.validateWeight(weight))
			{
				activeChar.sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
				activeChar.sendActionFailed();
				return;
			}
			
			if (!inventory.validateCapacity(slots))
			{
				activeChar.sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
				activeChar.sendActionFailed();
				return;
			}
			
			if (entry.getIngredients().size() == 0)
			{
				activeChar.sendActionFailed();
				activeChar.setMultisell(null);
				return;
			}
			
			for (MultiSellIngredient ingridient : entry.getIngredients())
			{
				int ingridientItemId = ingridient.getItemId();
				long ingridientItemCount = ingridient.getItemCount();
				int ingridientEnchant = ingridient.getItemEnchant();
				long totalAmount = !ingridient.getMantainIngredient() ? SafeMath.mulAndCheck(ingridientItemCount, _amount) : ingridientItemCount;
				
				if (ingridientItemId == ItemTemplate.ITEM_ID_CLAN_REPUTATION_SCORE)
				{
					if (activeChar.getClan() == null)
					{
						activeChar.sendPacket(Msg.YOU_ARE_NOT_A_CLAN_MEMBER);
						return;
					}
					
					if (activeChar.getClan().getReputationScore() < totalAmount)
					{
						activeChar.sendPacket(Msg.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
						return;
					}
					
					if (activeChar.getClan().getLeaderId() != activeChar.getObjectId())
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_A_CLAN_LEADER).addString(activeChar.getName()));
						return;
					}
					if (!ingridient.getMantainIngredient())
					{
						items.add(new ItemData(ingridientItemId, totalAmount, null));
					}
				}
				else if (ingridientItemId == ItemTemplate.ITEM_ID_PC_BANG_POINTS)
				{
					if (activeChar.getPcBangPoints() < totalAmount)
					{
						activeChar.sendPacket(Msg.YOU_ARE_SHORT_OF_ACCUMULATED_POINTS);
						return;
					}
					if (!ingridient.getMantainIngredient())
					{
						items.add(new ItemData(ingridientItemId, totalAmount, null));
					}
				}
				else if (ingridientItemId == ItemTemplate.ITEM_ID_FAME)
				{
					if (activeChar.getFame() < totalAmount)
					{
						activeChar.sendPacket(Msg.NOT_ENOUGH_FAME_POINTS);
						return;
					}
					if (!ingridient.getMantainIngredient())
					{
						items.add(new ItemData(ingridientItemId, totalAmount, null));
					}
				}
				else
				{
					ItemTemplate template = ItemHolder.getInstance().getTemplate(ingridientItemId);
					
					if (!template.isStackable())
					{
						for (int i = 0; i < (ingridientItemCount * _amount); i++)
						{
							List<ItemInstance> list = inventory.getItemsByItemId(ingridientItemId);
							if (keepenchant)
							{
								ItemInstance itemToTake = null;
								for (ItemInstance item : list)
								{
									ItemData itmd = new ItemData(item.getItemId(), item.getCount(), item);
									if (((item.getEnchantLevel() == ingridientEnchant) || !item.getTemplate().isEquipment()) && !items.contains(itmd) && item.canBeExchanged(activeChar))
									{
										itemToTake = item;
										break;
									}
								}
								
								if (itemToTake == null)
								{
									activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
									return;
								}
								
								if (!ingridient.getMantainIngredient())
								{
									items.add(new ItemData(itemToTake.getItemId(), 1, itemToTake));
								}
							}
							else
							{
								ItemInstance itemToTake = null;
								for (ItemInstance item : list)
								{
									if (!items.contains(new ItemData(item.getItemId(), item.getCount(), item)) && ((itemToTake == null) || (item.getEnchantLevel() < itemToTake.getEnchantLevel())) && !item.isShadowItem() && !item.isTemporalItem() && (!item.isAugmented() || Config.ALT_ALLOW_DROP_AUGMENTED) && ItemFunctions.checkIfCanDiscard(activeChar, item))
									{
										itemToTake = item;
										if (itemToTake.getEnchantLevel() == 0)
										{
											break;
										}
									}
								}
								
								if (itemToTake == null)
								{
									activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
									return;
								}
								
								if (!ingridient.getMantainIngredient())
								{
									items.add(new ItemData(itemToTake.getItemId(), 1, itemToTake));
								}
							}
						}
					}
					else
					{
						if (ingridientItemId == 57)
						{
							totalPrice = SafeMath.addAndCheck(totalPrice, SafeMath.mulAndCheck(ingridientItemCount, _amount));
						}
						ItemInstance item = inventory.getItemByItemId(ingridientItemId);
						
						if ((item == null) || (item.getCount() < totalAmount))
						{
							activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
							return;
						}
						
						if (!ingridient.getMantainIngredient())
						{
							items.add(new ItemData(item.getItemId(), totalAmount, item));
						}
					}
				}
				
				if (activeChar.getAdena() < totalPrice)
				{
					activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					return;
				}
			}
			
			int enchantLevel = 0;
			ItemAttributes attributes = null;
			int augmentationId = 0;
			for (ItemData id : items)
			{
				long count = id.getCount();
				if (count > 0)
				{
					if (id.getId() == ItemTemplate.ITEM_ID_CLAN_REPUTATION_SCORE)
					{
						activeChar.getClan().incReputation((int) -count, false, "MultiSell");
						activeChar.sendPacket(new SystemMessage(SystemMessage.S1_POINTS_HAVE_BEEN_DEDUCTED_FROM_THE_CLAN_REPUTATION_SCORE).addNumber(count));
					}
					else if (id.getId() == ItemTemplate.ITEM_ID_PC_BANG_POINTS)
					{
						activeChar.reducePcBangPoints((int) count);
					}
					else if (id.getId() == ItemTemplate.ITEM_ID_FAME)
					{
						activeChar.setFame(activeChar.getFame() - (int) count, "MultiSell");
						activeChar.sendPacket(new SystemMessage(SystemMessage.S2_S1_HAS_DISAPPEARED).addNumber(count).addString("Fame"));
					}
					else
					{
						if (inventory.destroyItem(id.getItem(), count))
						{
							if (keepenchant && id.getItem().canBeEnchanted())
							{
								enchantLevel = id.getItem().getEnchantLevel();
								attributes = id.getItem().getAttributes();
								augmentationId = id.getItem().getAugmentationId();
							}
							
							activeChar.sendPacket(SystemMessage2.removeItems(id.getId(), count));
							continue;
						}
						return;
					}
				}
			}
			
			if ((tax > 0) && !notax)
			{
				if (castle != null)
				{
					activeChar.sendMessage(new CustomMessage("trade.HavePaidTax", activeChar).addNumber(tax));
					if ((merchant != null) && (merchant.getReflection() == ReflectionManager.DEFAULT))
					{
						castle.addToTreasury(tax, true, false);
					}
				}
			}
			double rndNum = 100.0D * Rnd.nextDouble();
			double chance = 0.0D;
			double chanceFrom = 0.0D;
			cycle1 :
			for (MultiSellIngredient in : entry.getProduction())
			{
				if (in.getItemId() <= 0)
				{
					if (in.getItemId() == ItemTemplate.ITEM_ID_CLAN_REPUTATION_SCORE)
					{
						activeChar.getClan().incReputation((int) (in.getItemCount() * _amount), false, "MultiSell");
						activeChar.sendPacket(new SystemMessage(SystemMessage.YOUR_CLAN_HAS_ADDED_1S_POINTS_TO_ITS_CLAN_REPUTATION_SCORE).addNumber(in.getItemCount() * _amount));
					}
					else if (in.getItemId() == ItemTemplate.ITEM_ID_PC_BANG_POINTS)
					{
						activeChar.addPcBangPoints((int) (in.getItemCount() * _amount), false);
					}
					else if (in.getItemId() == ItemTemplate.ITEM_ID_FAME)
					{
						activeChar.setFame(activeChar.getFame() + (int) (in.getItemCount() * _amount), "MultiSell");
					}
				}
				else if (ItemHolder.getInstance().getTemplate(in.getItemId()).isStackable())
				{
					
					long total = SafeMath.mulAndLimit(in.getItemCount(), _amount);
					if (in.getChance() >= 0)
					{
						chance = in.getChance();
						
						if ((rndNum >= chanceFrom) && (rndNum <= (chance + chanceFrom)))
						{
							ItemFunctions.addItem(activeChar, in.getItemId(), total, true);
							break;
						}
						chanceFrom += chance;
					}
					else
					{
						ItemFunctions.addItem(activeChar, in.getItemId(), total, true);
					}
					
				}
				else
				{
					for (int i = 0; i < _amount; i++)
					{
						ItemInstance product = ItemFunctions.createItem(in.getItemId());
						
						if (keepenchant && product.canBeEnchanted())
						{
							if (in.getChance() >= 0)
							{
								chance = in.getChance();							
								
								if ((rndNum >= chanceFrom) && (rndNum <= (chance + chanceFrom)))
								{
									product.setEnchantLevel(enchantLevel);
									if (attributes != null)
									{
										product.setAttributes(attributes.clone());
									}
									if (augmentationId != 0)
									{
										product.setAugmentationId(augmentationId);
									}
									inventory.addItem(product);
									activeChar.sendPacket(SystemMessage2.obtainItems(product));
									break cycle1;
									
								}
								chanceFrom += chance;
							}
							else
							{
								product.setEnchantLevel(enchantLevel);
								if (attributes != null)
								{
									product.setAttributes(attributes.clone());
								}
								if (augmentationId != 0)
								{
									product.setAugmentationId(augmentationId);
								}
								inventory.addItem(product);
								activeChar.sendPacket(SystemMessage2.obtainItems(product));
							}
						}
						else
						{
							product.setEnchantLevel(in.getItemEnchant());
							product.setAttributes(in.getItemAttributes().clone());
							if (in.getChance() >= 0)
							{
								chance = in.getChance();
								
								if ((rndNum >= chanceFrom) && (rndNum <= (chance + chanceFrom)))
								{
									ItemFunctions.addItem(activeChar, in.getItemId(), in.getItemCount(), true);
									break;
								}
								chanceFrom += chance;
							}
							else
							{
								inventory.addItem(product);
								activeChar.sendPacket(SystemMessage2.obtainItems(product));
							}
						}
					}
				}
			}
		}
		catch (ArithmeticException ae)
		{
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		finally
		{
			inventory.writeUnlock();
		}
		
		activeChar.sendChanges();
		
		if (!list1.isShowAll())
		{
			MultiSellHolder.getInstance().SeparateAndSend(list1, activeChar, castle == null ? 0 : castle.getTaxRate());
		}
	}
}