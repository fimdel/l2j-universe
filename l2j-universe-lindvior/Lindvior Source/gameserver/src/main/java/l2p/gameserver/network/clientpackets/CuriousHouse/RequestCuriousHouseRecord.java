/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets.CuriousHouse;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;
import l2p.gameserver.network.serverpackets.CuriousHouse.ExCuriousHouseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestCuriousHouseRecord extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestCuriousHouseRecord.class);

    protected void readImpl() {

    }

    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;
        player.sendPacket(new ExCuriousHouseResult());
        _log.info("[IMPLEMENT ME!] RequestCuriousHouseRecord (maybe trigger)");
    }
}
/*
int __thiscall UNetworkHandler__RequestCuriousHouseRecord(int this)
{
  return (*(int (__cdecl **)(_DWORD, _DWORD, signed int, signed int))(**(_DWORD **)(this + 72) + 108))(
           *(_DWORD *)(this + 72),
           "ch",
           208,
           202);
}
 */
