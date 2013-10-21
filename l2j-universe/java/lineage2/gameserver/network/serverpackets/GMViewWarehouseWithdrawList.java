package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;

public class GMViewWarehouseWithdrawList extends L2GameServerPacket
{
	private final ItemInstance[] _items;
	private String _charName;
	private long _charAdena;

	public GMViewWarehouseWithdrawList(Player cha)
	{
		_charName = cha.getName();
		_charAdena = cha.getAdena();
		_items = cha.getWarehouse().getItems();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x9b);
		writeS(_charName);
		writeQ(_charAdena);
		writeH(_items.length);
		for (ItemInstance temp : _items)
		{
			writeItemInfo(temp);
			writeD(temp.getObjectId());
		}
	}
}