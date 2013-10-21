package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.items.Inventory;

import java.util.Map;

public class ShopPreviewInfo extends L2GameServerPacket
{
	private Map<Integer, Integer> _itemlist;

	public ShopPreviewInfo(Map<Integer, Integer> itemlist)
	{
		_itemlist = itemlist;
	}

	@Override
	protected void writeImpl()
	{
		writeC(0xF6);
		writeD(Inventory.PAPERDOLL_MAX);

		// Slots
		for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
			writeD(getFromList(PAPERDOLL_ID));
	}

	private int getFromList(int key)
	{
		return _itemlist.get(key) != null ? _itemlist.get(key) : 0;
	}
}