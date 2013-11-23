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

public class RequestCancelCuriousHouse extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestCancelCuriousHouse.class);

    protected void readImpl() {
    }

    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;
        _log.info("[IMPLEMENT ME!] RequestCancelCuriousHouse (maybe trigger)");
    }
}
/*
int __thiscall UNetworkHandler__RequestCancelCuriousHouse(int this)
{
  return (*(int (__cdecl **)(_DWORD, _DWORD, signed int, signed int))(**(_DWORD **)(this + 72) + 108))(
           *(_DWORD *)(this + 72),
           "ch",
           208,
           196);
}
*/