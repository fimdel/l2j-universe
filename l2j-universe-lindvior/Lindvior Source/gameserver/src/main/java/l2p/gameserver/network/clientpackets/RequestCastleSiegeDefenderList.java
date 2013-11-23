package l2p.gameserver.network.clientpackets;

import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.network.serverpackets.CastleSiegeDefenderList;

public class RequestCastleSiegeDefenderList extends L2GameClientPacket {
    private int _unitId;

    @Override
    protected void readImpl() {
        _unitId = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, _unitId);
        if (castle == null || castle.getOwner() == null)
            return;

        player.sendPacket(new CastleSiegeDefenderList(castle));
    }
}