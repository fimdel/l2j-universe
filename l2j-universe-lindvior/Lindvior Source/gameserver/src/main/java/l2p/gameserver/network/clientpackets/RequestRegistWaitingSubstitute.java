package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.PartySubstituteManager;
import l2p.gameserver.model.Player;

/**
 * @author ALF
 * @update Darvin
 * @data 20.02.2012
 */
public final class RequestRegistWaitingSubstitute extends L2GameClientPacket {
    private int _key;

    @Override
    protected void readImpl() {
        _key = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null)
            return;

        switch (_key) {
            case 0:
                PartySubstituteManager.getInstance().removePlayerFromParty(activeChar);
                break;
            case 1:
                PartySubstituteManager.getInstance().addPlayerToParty(activeChar);
                break;
            default:
                System.out.println("RequestRegistWaitingSubstitute: key is " + _key);
                break;
        }
    }
}
