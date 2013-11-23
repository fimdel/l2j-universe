package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.PartySubstituteManager;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExRegistPartySubstitute;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 01.07.12
 * Time: 14:41
 * запрос от лидера на замену чара
 */
public class RequestRegistPartySubstitute extends L2GameClientPacket {
    private int _changeCharId;

    @Override
    protected void readImpl() {
        _changeCharId = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();

        if (player == null)
            return;

        Party p = player.getParty();

        if (p == null)
            return;

        if (!p.isLeader(player))
            return;

        Player chp = GameObjectsStorage.getPlayer(_changeCharId);

        if (!p.containsMember(chp))
            return;

        PartySubstituteManager.getInstance().addPlayerToReplace(chp);

        player.sendPacket(new ExRegistPartySubstitute(_changeCharId, ExRegistPartySubstitute.REGISTER_OK));

    }
}
