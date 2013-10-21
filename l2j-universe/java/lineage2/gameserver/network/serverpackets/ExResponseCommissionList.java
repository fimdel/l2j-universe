package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.instancemanager.commission.CommissionItemInfo;

import java.util.List;

/**
 * @author : Darvin
 */
public class ExResponseCommissionList extends L2GameServerPacket
{
	public static final int EMPTY_LIST = -2;
	public static final int PLAYER_REGISTERED_ITEMS = 2;
	public static final int ALL_ITEMS = 3;

	private int type;
	private int currentTime;
	private int part;
	private List<CommissionItemInfo> items;

	public ExResponseCommissionList(int type)
	{
		this.type = type;
	}

	public ExResponseCommissionList(int type, int part, List<CommissionItemInfo> items)
	{
		this.type = type;
		this.part = part;
		this.items = items;

		currentTime = (int) (System.currentTimeMillis() / 1000);
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xF7);

		writeD(type); // List type. -2 при пустов листе, 02 - итемы,
		              // выставленные персонажем, 03 - все итемы
		if (type == EMPTY_LIST)
			return;
		writeD(currentTime); // current time
		writeD(part); // part
		writeD(items.size()); // items count
		for (CommissionItemInfo itemInfo : items)
		{
			writeQ(itemInfo.getAuctionId()); // auctionId
			writeQ(itemInfo.getRegisteredPrice()); // item price
			writeD(itemInfo.getExItemType().ordinal()); // Тип продаваемой вещи
			writeD(itemInfo.getSaleDays()); // sale days, 0 - 1 день, 1 - 3 дня,
			                                // 2 - 5 дней, 3 - 7 дней.
			writeD((int) (itemInfo.getSaleEndTime() / 1000)); // Sale end time
			writeS(itemInfo.getSellerName()); // seller name
			writeD(0); // unknown (вероятно objectId итема), на евро всегда 0
			writeD(itemInfo.getItem().getItemId()); // item_id
			writeQ(itemInfo.getItem().getCount()); // count
			writeH(itemInfo.getItem().getTemplate().getType2ForPackets()); // itemType2
			                                                               // or
			                                                               // equipSlot
			writeD(itemInfo.getItem().getBodyPart()); // bodypart
			writeH(itemInfo.getItem().getEnchantLevel()); // enchant_lvl
			writeH(itemInfo.getItem().getCustomType2()); // custom_type2
			writeD(0x00); // unk
			writeH(itemInfo.getItem().getAttackElement().getId()); // atk_element_id
			writeH(itemInfo.getItem().getAttackElementValue()); // atk_element_val
			writeH(itemInfo.getItem().getDefenceFire()); // fire_defence
			writeH(itemInfo.getItem().getDefenceWater()); // water_defence
			writeH(itemInfo.getItem().getDefenceWind()); // wind_defence
			writeH(itemInfo.getItem().getDefenceEarth()); // earth_defence
			writeH(itemInfo.getItem().getDefenceHoly()); // holy_defence
			writeH(itemInfo.getItem().getDefenceUnholy()); // unholy_defence
			writeH(itemInfo.getItem().getEnchantOptions()[0]); // enchant_opt1
			writeH(itemInfo.getItem().getEnchantOptions()[1]); // enchant_opt2
			writeH(itemInfo.getItem().getEnchantOptions()[2]); // enchant_opt3
		}
	}
}
