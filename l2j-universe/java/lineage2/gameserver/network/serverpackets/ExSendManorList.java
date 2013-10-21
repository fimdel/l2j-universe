package lineage2.gameserver.network.serverpackets;

import java.util.Collection;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Residence;

public class ExSendManorList extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeEx(0x22);
		Collection<Castle> residences = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		writeD(residences.size());
		for (Residence castle : residences)
		{
			writeD(castle.getId());
			writeS(castle.getName().toLowerCase());
		}
	}
}