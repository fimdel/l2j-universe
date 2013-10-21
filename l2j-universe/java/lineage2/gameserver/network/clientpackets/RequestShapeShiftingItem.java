package lineage2.gameserver.network.clientpackets;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.data.xml.holder.EnchantItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.etcitems.AppearanceStone;
import lineage2.gameserver.model.items.etcitems.AppearanceStone.ShapeTargetType;
import lineage2.gameserver.model.items.etcitems.AppearanceStone.ShapeType;
import lineage2.gameserver.network.serverpackets.ExShapeShiftingResult;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ExItemType;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author kick
 **/
public class RequestShapeShiftingItem extends L2GameClientPacket
{
	private int _targetItemObjId;

	@Override
	protected void readImpl()
	{
		_targetItemObjId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if(player == null)
		{
			return;
		}

		if(player.isActionsDisabled() || player.isInStoreMode() || player.isInTrade())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		PcInventory inventory = player.getInventory();
		ItemInstance targetItem = inventory.getItemByObjectId(_targetItemObjId);
		ItemInstance stone = player.getAppearanceStone();
		if(targetItem == null || stone == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		if(stone.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		if(!targetItem.canBeAppearance())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		if(targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		if((stone = inventory.getItemByObjectId(stone.getObjectId())) == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		AppearanceStone appearanceStone = EnchantItemHolder.getInstance().getAppearanceStone(stone.getItemId());
		if(appearanceStone == null)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		if(appearanceStone.getType() != ShapeType.RESTORE && targetItem.getVisualId() > 0 || appearanceStone.getType() == ShapeType.RESTORE && targetItem.getVisualId() == 0)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		if(!targetItem.isOther() && targetItem.getTemplate().getItemGrade() == Grade.NONE)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		Grade[] stoneGrades = appearanceStone.getGrades();
		if(stoneGrades != null && stoneGrades.length > 0)
		{
			if(!ArrayUtils.contains(stoneGrades, targetItem.getTemplate().getItemGrade()))
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
		}

		ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
		if(targetTypes == null || targetTypes.length == 0)
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ALL))
		{
			if(targetItem.isWeapon())
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.WEAPON))
				{
					player.sendPacket(ExShapeShiftingResult.FAIL);
					player.setAppearanceStone(null);
					player.setAppearanceExtractItem(null);
					return;
				}
			}
			else if(targetItem.isArmor())
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ARMOR))
				{
					player.sendPacket(ExShapeShiftingResult.FAIL);
					player.setAppearanceStone(null);
					player.setAppearanceExtractItem(null);
					return;
				}
			}
			else
			{
				if(!ArrayUtils.contains(targetTypes, ShapeTargetType.ACCESSORY))
				{
					player.sendPacket(ExShapeShiftingResult.FAIL);
					player.setAppearanceStone(null);
					player.setAppearanceExtractItem(null);
					return;
				}
			}
		}

		ExItemType[] itemTypes = appearanceStone.getItemTypes();
		if(itemTypes != null && itemTypes.length > 0)
		{
			if(!ArrayUtils.contains(itemTypes, targetItem.getExItemType()))
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
		}

		ItemInstance extracItem = player.getAppearanceExtractItem();
		int extracItemId = 0;
		if(appearanceStone.getType() != ShapeType.RESTORE && appearanceStone.getType() != ShapeType.FIXED)
		{
			if(extracItem == null)
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}

			if(!extracItem.canBeAppearance())
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}

			if(extracItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && extracItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}

			/*if(!extracItem.isOther() && extracItem.getGrade() == ItemGrade.NONE)
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}*/

			if(!extracItem.isOther() && targetItem.getTemplate().getItemGrade().ordinal() <= extracItem.getTemplate().getItemGrade().ordinal())
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}

			if(extracItem.getVisualId() > 0)
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}

			if(targetItem.getExItemType() != extracItem.getExItemType())
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}

			if(extracItem.getOwnerId() != player.getObjectId())
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}
			extracItemId = extracItem.getItemId();
		}

		if(targetItem.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExShapeShiftingResult.FAIL);
			player.setAppearanceStone(null);
			player.setAppearanceExtractItem(null);
			return;
		}

		inventory.writeLock();
		try
		{
			long cost = appearanceStone.getCost();
			if(cost > player.getAdena())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_MODIFY_AS_YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}

			if(stone.getCount() < 1L)
			{
				player.sendPacket(ExShapeShiftingResult.FAIL);
				player.setAppearanceStone(null);
				player.setAppearanceExtractItem(null);
				return;
			}

			if(appearanceStone.getType() == ShapeType.NORMAL)
			{
				if(!inventory.destroyItem(extracItem, 1L))
				{
					player.sendPacket(ExShapeShiftingResult.FAIL);
					player.setAppearanceStone(null);
					player.setAppearanceExtractItem(null);
					return;
				}
			}

			inventory.destroyItem(stone, 1L);
			player.reduceAdena(cost);

			boolean equipped = false;
			if(equipped = targetItem.isEquipped())
			{
				inventory.isRefresh = true;
				inventory.unEquipItem(targetItem);
			}

			switch(appearanceStone.getType())
			{
				case RESTORE:
					targetItem.setVisualId(0);
					break;
				case NORMAL:
					targetItem.setVisualId(extracItem.getItemId());
					break;
				case BLESSED:
					targetItem.setVisualId(extracItem.getItemId());
					break;
				case FIXED:
					targetItem.setVisualId(appearanceStone.getExtractItemId());
					break;
				default:
					break;
			}

			targetItem.setJdbcState(JdbcEntityState.UPDATED);
			targetItem.update();

			if(equipped)
			{
				inventory.equipItem(targetItem);
				inventory.isRefresh = false;
			}
			player.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_SPENT_S1_ON_A_SUCCESSFUL_APPEARANCE_MODIFICATION).addLong(cost));
		}
		finally
		{
			inventory.writeUnlock();
		}

		//player.sendPacket(new InventoryUpdate().addModifiedItem(player, targetItem));
		player.sendPacket(new InventoryUpdate().addModifiedItem(targetItem));
		//player.sendPacket(new IStaticPacket[] { new ExShapeShiftingResult(targetItem.getItemId(), extracItem.getVisualId()), new UserInfo(player), new ExBR_ExtraUserInfo(player) });

		player.setAppearanceStone(null);
		player.setAppearanceExtractItem(null);
		player.sendPacket(new ExShapeShiftingResult(ExShapeShiftingResult.SUCCESS_RESULT, targetItem.getItemId(), extracItemId));
	}
}