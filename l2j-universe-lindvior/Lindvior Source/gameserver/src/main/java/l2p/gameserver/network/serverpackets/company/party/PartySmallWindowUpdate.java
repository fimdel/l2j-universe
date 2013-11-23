/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.instancemanager.PartySubstituteManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PartySmallWindowUpdate extends L2GameServerPacket {
    private final int obj_id, class_id, level;
    private final int curCp, maxCp, curHp, maxHp, curMp, maxMp, vitality;
    private final String obj_name;
    private final int replace;

    public PartySmallWindowUpdate(Player member) {
        obj_id = member.getObjectId();
        obj_name = member.getName();
        curCp = (int) member.getCurrentCp();
        maxCp = member.getMaxCp();
        curHp = (int) member.getCurrentHp();
        maxHp = member.getMaxHp();
        curMp = (int) member.getCurrentMp();
        maxMp = member.getMaxMp();
        level = member.getLevel();
        class_id = member.getClassId().getId();
        vitality = member.getVitality().getPoints();
        replace = PartySubstituteManager.getInstance().isPlayerToReplace(member) ? 1 : 0;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x52);
        // dSdddddddd
        writeD(obj_id);
        writeS(obj_name);
        writeD(curCp);
        writeD(maxCp);
        writeD(curHp);
        writeD(maxHp);
        writeD(curMp);
        writeD(maxMp);
        writeD(level);
        writeD(class_id);
        writeD(vitality);
        writeD(replace); // Идет ли поиск замены игроку
    }
}