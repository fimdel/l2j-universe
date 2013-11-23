package l2p.gameserver.network.clientpackets;

import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.residence.Fortress;
import l2p.gameserver.network.serverpackets.ExShowFortressMapInfo;

public class RequestFortressMapInfo extends L2GameClientPacket {
    private int _fortressId;

    @Override
    protected void readImpl() {
        _fortressId = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;
        Fortress fortress = ResidenceHolder.getInstance().getResidence(Fortress.class, _fortressId);
        if (fortress != null)
            sendPacket(new ExShowFortressMapInfo(fortress));
    }
}