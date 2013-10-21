package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;

/**
 */
public class ExMenteeSearch extends L2GameServerPacket {
	List<Player> mentees;
	int page, playersInPage;

	public ExMenteeSearch(Player activeChar, int _page, int minLevel, int maxLevel) {
		mentees = new ArrayList<>();
		page = _page;
		playersInPage = 64;
		for (Player player : World.getAroundPlayers(activeChar))
		{
			if (player.getLevel() >= minLevel && player.getLevel() <= maxLevel)
			{
				mentees.add(player);
			}
		}
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x122);
		writeD(page);
		if (!mentees.isEmpty())
		{
			writeD(mentees.size());
			writeD(mentees.size() % playersInPage);
			int i = 1;
			for (Player player : mentees)
			{
				if (i <= playersInPage * page && i > playersInPage * (page - 1))
				{
					writeS(player.getName());
					writeD(player.getClassId().getId());
					writeD(player.getLevel());
				}
			}
		}
		else
		{
			writeD(0x00);
			writeD(0x00);
		}
	}

}
