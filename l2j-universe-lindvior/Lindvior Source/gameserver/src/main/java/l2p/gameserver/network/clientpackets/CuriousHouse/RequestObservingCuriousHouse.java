/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets.CuriousHouse;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestObservingCuriousHouse extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestObservingCuriousHouse.class);

    protected void readImpl() {
        readD();
    }

    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;
        _log.info("[IMPLEMENT ME!] RequestObservingCuriousHouse (maybe trigger)");
    }
}
/*
int __thiscall UNetworkHandler__RequestObservingCuriousHouse(int this, int a2)
{
  return (*(int (__cdecl **)(_DWORD, char *, signed int, signed int, int))(**(_DWORD **)(this + 72) + 108))(
           *(_DWORD *)(this + 72),
           "chd",
           208,
           199,
           a2);
}
 */
