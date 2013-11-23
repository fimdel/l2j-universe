/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.RankPrivs;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ManagePledgePower extends L2GameServerPacket {
    private int _action, _clanId, privs;

    public ManagePledgePower(Player player, int action, int rank) {
        _clanId = player.getClanId();
        _action = action;
        RankPrivs temp = player.getClan().getRankPrivs(rank);
        privs = temp == null ? 0 : temp.getPrivs();
        player.sendPacket(new PledgeReceiveUpdatePower(privs));
    }

    @Override
    protected final void writeImpl() {
        writeC(0x2a);
        writeD(_clanId);
        writeD(_action);
        writeD(privs);
    }
}