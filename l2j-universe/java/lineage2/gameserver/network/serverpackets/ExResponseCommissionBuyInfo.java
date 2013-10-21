package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.instancemanager.commission.CommissionItemInfo;

/**
 * @author : Darvin
 */
public class ExResponseCommissionBuyInfo extends L2GameServerPacket
{
	private CommissionItemInfo _itemInfo;

	public ExResponseCommissionBuyInfo(CommissionItemInfo itemInfo)
	{
		_itemInfo = itemInfo;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xF8);
		writeD(1); // listSize
		writeQ(_itemInfo.getRegisteredPrice());
		writeQ(_itemInfo.getAuctionId());
		writeD(_itemInfo.getExItemType().ordinal());
		writeD(0x00); // unk maybe objId?
		writeD(_itemInfo.getItem().getItemId());
		writeD(_itemInfo.getItem().getEquipSlot());
		writeQ(_itemInfo.getItem().getCount());
		writeH(_itemInfo.getItem().getTemplate().getType2ForPackets());
		writeH(_itemInfo.getItem().getCustomType1());
		writeH(_itemInfo.getItem().isEquipped() ? 1 : 0);
		writeD(_itemInfo.getItem().getBodyPart());
		writeH(_itemInfo.getItem().getEnchantLevel());
		writeH(_itemInfo.getItem().getCustomType2());
		writeD(_itemInfo.getItem().getAugmentationId()); // L2WT TEST!!! D =
		                                                 // [HH] [00 00] [00 00]
		writeD(_itemInfo.getItem().getShadowLifeTime());
		writeD(_itemInfo.getItem().getTemporalLifeTime());
		writeH(0x01); // L2WT GOD
		writeH(_itemInfo.getItem().getAttackElement().getId());
		writeH(_itemInfo.getItem().getAttackElementValue());
		writeH(_itemInfo.getItem().getDefenceFire());
		writeH(_itemInfo.getItem().getDefenceWater());
		writeH(_itemInfo.getItem().getDefenceWind());
		writeH(_itemInfo.getItem().getDefenceEarth());
		writeH(_itemInfo.getItem().getDefenceHoly());
		writeH(_itemInfo.getItem().getDefenceUnholy());
		writeH(_itemInfo.getItem().getEnchantOptions()[0]);
		writeH(_itemInfo.getItem().getEnchantOptions()[1]);
		writeH(_itemInfo.getItem().getEnchantOptions()[2]);
	}
}
