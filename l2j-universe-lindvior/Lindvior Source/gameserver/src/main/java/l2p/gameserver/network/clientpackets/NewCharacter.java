package l2p.gameserver.network.clientpackets;

import l2p.gameserver.network.serverpackets.NewCharacterSuccess;

public class NewCharacter extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        sendPacket(new NewCharacterSuccess());
    }
}