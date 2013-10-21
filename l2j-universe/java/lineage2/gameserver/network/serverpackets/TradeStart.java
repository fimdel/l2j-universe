package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;

import java.util.ArrayList;
import java.util.List;

public class TradeStart extends L2GameServerPacket
{
	private List<ItemInfo> _tradelist = new ArrayList<ItemInfo>();
	private int targetId;

	public TradeStart(Player player, Player target)
	{
		targetId = target.getObjectId();

		ItemInstance[] items = player.getInventory().getItems();
		for (ItemInstance item : items)
			if (item.canBeTraded(player))
				_tradelist.add(new ItemInfo(item));
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x14);
		writeD(targetId);
		writeH(_tradelist.size());
		for (ItemInfo item : _tradelist)
			writeItemInfo(item);
	}
}