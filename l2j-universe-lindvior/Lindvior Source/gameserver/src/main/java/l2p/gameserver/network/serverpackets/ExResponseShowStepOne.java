package l2p.gameserver.network.serverpackets;

import l2p.gameserver.data.xml.holder.PetitionGroupHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.petition.PetitionMainGroup;
import l2p.gameserver.utils.Language;

import java.util.Collection;

/**
 * @author VISTALL
 */
public class ExResponseShowStepOne extends L2GameServerPacket {
    private Language _language;

    public ExResponseShowStepOne(Player player) {
        _language = player.getLanguage();
    }

    @Override
    protected void writeImpl() {
        writeEx449(0xAE);
        Collection<PetitionMainGroup> petitionGroups = PetitionGroupHolder.getInstance().getPetitionGroups();
        writeD(petitionGroups.size());
        for (PetitionMainGroup group : petitionGroups) {
            writeC(group.getId());
            writeS(group.getName(_language));
        }
    }
}