package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

/**
 * Seven Signs Record Update
 * <p/>
 * packet type id 0xf5 format:
 * <p/>
 * c cc (Page Num = 1 -> 4, period)
 * <p/>
 * 1: [ddd cc dd ddd c ddd c] 2: [hc [cd (dc (S))] 3: [ccc (cccc)] 4: [(cchh)]
 */
public class SSQStatus extends L2GameServerPacket
{
	private int _page, period;

	public SSQStatus(Player player, int recordPage)
	{
		_page = recordPage;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xfb);

		writeC(_page);
		writeC(period); // current period?
	}
}