package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Summon;

public class ExPartyPetWindowDelete extends L2GameServerPacket
{
	private int _summonObjectId;
	private int _ownerObjectId;
	private String _summonName;

	public ExPartyPetWindowDelete(Summon summon)
	{
		_summonObjectId = summon.getObjectId();
		_summonName = summon.getName();
		_ownerObjectId = summon.getPlayer().getObjectId();
	}

	@Override
	protected final void writeImpl()
	{
		writeEx(0x6B);
		writeD(_summonObjectId);
		writeD(_ownerObjectId);
		writeS(_summonName);
	}
}