package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author : Darvin
 */
public class ExResponseCommissionInfo extends L2GameServerPacket
{
	private ItemInstance _item;

	public ExResponseCommissionInfo(ItemInstance item)
	{
		_item = item;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xF4);
		writeD(_item.getItemId()); // ItemId
		writeD(_item.getObjectId());
		writeQ(_item.getCount()); // TODO
		writeQ(0/* _item.getCount() */); // TODO
		writeD(0); // TODO
	}
}
