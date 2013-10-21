package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.instancemanager.itemauction.ItemAuction;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionBid;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionState;

/**
 * @author n0nam3
 */
public class ExItemAuctionInfo extends L2GameServerPacket
{
	private boolean _refresh;
	private int _timeRemaining;
	private ItemAuction _currentAuction;
	private ItemAuction _nextAuction;

	public ExItemAuctionInfo(boolean refresh, ItemAuction currentAuction, ItemAuction nextAuction)
	{
		if (currentAuction == null)
			throw new NullPointerException();

		if (currentAuction.getAuctionState() != ItemAuctionState.STARTED)
			_timeRemaining = 0;
		else
			_timeRemaining = (int) (currentAuction.getFinishingTimeRemaining() / 1000); // in
			                                                                            // seconds

		_refresh = refresh;
		_currentAuction = currentAuction;
		_nextAuction = nextAuction;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x69);
		writeC(_refresh ? 0x00 : 0x01);
		writeD(_currentAuction.getInstanceId());

		ItemAuctionBid highestBid = _currentAuction.getHighestBid();
		writeQ(highestBid != null ? highestBid.getLastBid() : _currentAuction.getAuctionInitBid());

		writeD(_timeRemaining);
		writeItemInfo(_currentAuction.getAuctionItem());

		if (_nextAuction != null)
		{
			writeQ(_nextAuction.getAuctionInitBid());
			writeD((int) (_nextAuction.getStartingTime() / 1000L)); // unix time
			                                                        // in
			                                                        // seconds
			writeItemInfo(_nextAuction.getAuctionItem());
		}
	}
}