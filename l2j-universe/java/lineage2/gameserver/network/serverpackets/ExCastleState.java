package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ResidenceSide;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author Bonux
 */
public class ExCastleState extends L2GameServerPacket
{
	private final int _id;
	private final ResidenceSide _side;
	
	public ExCastleState(Castle castle)
	{
		_id = castle.getId();
		_side = castle.getResidenceSide();
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0x133);
		writeD(_id);
		writeD(_side.ordinal());
		
	}
}