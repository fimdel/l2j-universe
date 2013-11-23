/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.company.MPCC.ExListMpccWaiting;

/**
 * @author VISTALL
 */
public class RequestExListMpccWaiting extends L2GameClientPacket {
    private int _listId;
    private int _locationId;
    private boolean _allLevels;

    @Override
    protected void readImpl() throws Exception {
        _listId = readD();
        _locationId = readD();
        _allLevels = readD() == 1;
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        player.sendPacket(new ExListMpccWaiting(player, _listId, _locationId, _allLevels));
    }
}