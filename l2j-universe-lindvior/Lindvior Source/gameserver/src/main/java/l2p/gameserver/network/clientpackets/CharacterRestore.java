package l2p.gameserver.network.clientpackets;

import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.CharacterSelectionInfo;
import l2p.gameserver.network.serverpackets.ExLoginVitalityEffectInfo;

public class CharacterRestore extends L2GameClientPacket {
    // cd
    private int _charSlot;

    @Override
    protected void readImpl() {
        _charSlot = readD();
    }

    @Override
    protected void runImpl() {
        GameClient client = getClient();
        try {
            client.markRestoredChar(_charSlot);
        } catch (Exception e) {
        }
        CharacterSelectionInfo cl = new CharacterSelectionInfo(client.getLogin(), client.getSessionKey().playOkID1);
        ExLoginVitalityEffectInfo vl = new ExLoginVitalityEffectInfo(cl.getCharInfo());
        sendPacket(cl, vl);
        client.setCharSelection(cl.getCharInfo());
    }
}