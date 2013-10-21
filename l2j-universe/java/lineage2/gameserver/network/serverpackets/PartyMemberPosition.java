package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Location;

import java.util.HashMap;
import java.util.Map;

public class PartyMemberPosition extends L2GameServerPacket
{
	private final Map<Integer, Location> positions = new HashMap<Integer, Location>();

	public PartyMemberPosition add(Player actor)
	{
		positions.put(actor.getObjectId(), actor.getLoc());
		return this;
	}

	public int size()
	{
		return positions.size();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xba);
		writeD(positions.size());
		for (Map.Entry<Integer, Location> e : positions.entrySet())
		{
			writeD(e.getKey());
			writeD(e.getValue().x);
			writeD(e.getValue().y);
			writeD(e.getValue().z);
		}
	}
}