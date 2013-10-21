package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.data.xml.holder.PetitionGroupHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.petition.PetitionMainGroup;
import lineage2.gameserver.utils.Language;

import java.util.Collection;

/**
 * @author VISTALL
 */
public class ExResponseShowStepOne extends L2GameServerPacket
{
	private Language _language;

	public ExResponseShowStepOne(Player player)
	{
		_language = player.getLanguage();
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xAF);
		Collection<PetitionMainGroup> petitionGroups = PetitionGroupHolder.getInstance().getPetitionGroups();
		writeD(petitionGroups.size());
		for (PetitionMainGroup group : petitionGroups)
		{
			writeC(group.getId());
			writeS(group.getName(_language));
		}
	}
}