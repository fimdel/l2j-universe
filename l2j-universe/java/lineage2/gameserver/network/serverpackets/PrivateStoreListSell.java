package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.TradeItem;

import java.util.List;

public class PrivateStoreListSell extends L2GameServerPacket
{
	private int _sellerId;
	private long _adena;
	private final boolean _package;
	private List<TradeItem> _sellList;

	/**
	 * Список вещей в личном магазине продажи, показываемый покупателю
	 * 
	 * @param buyer
	 * @param seller
	 */
	public PrivateStoreListSell(Player buyer, Player seller)
	{
		_sellerId = seller.getObjectId();
		_adena = buyer.getAdena();
		_package = seller.getPrivateStoreType() == Player.STORE_PRIVATE_SELL_PACKAGE;
		_sellList = seller.getSellList();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xA1);
		writeD(_sellerId);
		writeD(_package ? 1 : 0);
		writeQ(_adena);
		writeD(0x00); // L2WT GOD
		writeD(_sellList.size());
		for (TradeItem si : _sellList)
		{
			writeItemInfo(si);
			writeQ(si.getOwnersPrice());
			writeQ(si.getStorePrice());
		}
	}
}