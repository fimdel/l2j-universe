/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.instancemanager.CursedWeaponsManager;

public class ExCursedWeaponList extends L2GameServerPacket {
    private int[] cursedWeapon_ids;

    public ExCursedWeaponList() {
        cursedWeapon_ids = CursedWeaponsManager.getInstance().getCursedWeaponsIds();
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x47);
        writeDD(cursedWeapon_ids, true);
    }
}