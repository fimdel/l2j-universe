package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Config;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.petition.PetitionMainGroup;
import l2p.gameserver.model.petition.PetitionSubGroup;
import l2p.gameserver.network.serverpackets.ExResponseShowContents;

/**
 * @author VISTALL
 */
public class RequestExShowStepThree extends L2GameClientPacket {
    private int _subId;

    @Override
    protected void readImpl() {
        _subId = readC();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null || !Config.EX_NEW_PETITION_SYSTEM)
            return;

        PetitionMainGroup group = player.getPetitionGroup();
        if (group == null)
            return;

        PetitionSubGroup subGroup = group.getSubGroup(_subId);
        if (subGroup == null)
            return;

        player.sendPacket(new ExResponseShowContents(subGroup.getDescription(player.getLanguage())));
    }
}