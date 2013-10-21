package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.petition.PetitionMainGroup;
import lineage2.gameserver.model.petition.PetitionSubGroup;
import lineage2.gameserver.utils.Language;

import java.util.Collection;

/**
 * @author VISTALL
 */
public class ExResponseShowStepTwo extends L2GameServerPacket
{
	private Language _language;
	private PetitionMainGroup _petitionMainGroup;

	public ExResponseShowStepTwo(Player player, PetitionMainGroup gr)
	{
		_language = player.getLanguage();
		_petitionMainGroup = gr;
	}

	@Override
	protected void writeImpl()
	{
		writeEx(0xB0);
		Collection<PetitionSubGroup> subGroups = _petitionMainGroup.getSubGroups();
		writeD(subGroups.size());
		writeS(_petitionMainGroup.getDescription(_language));
		for (PetitionSubGroup g : subGroups)
		{
			writeC(g.getId());
			writeS(g.getName(_language));
		}
	}
}