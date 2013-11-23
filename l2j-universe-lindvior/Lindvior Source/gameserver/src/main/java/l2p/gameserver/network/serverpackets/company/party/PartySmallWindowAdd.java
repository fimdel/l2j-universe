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

public class PartySmallWindowAdd extends L2GameServerPacket {
    private int leaderId, replace;
    private int distribution;
    private final PartySmallWindowAll.PartySmallWindowMemberInfo member;

    public PartySmallWindowAdd(Player player, Player member, int _distribution) {
        leaderId = player.getObjectId();
        distribution = _distribution;
        this.member = new PartySmallWindowAll.PartySmallWindowMemberInfo(member);
        replace = PartySubstituteManager.getInstance().isPlayerToReplace(member) ? 1 : 0;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x4F);
        writeD(leaderId);
        writeD(distribution);
        writeD(member._id);
        writeS(member._name);
        writeD(member.curCp);
        writeD(member.maxCp);
        writeD(member.vitality);
        writeD(member.curHp);
        writeD(member.maxHp);
        writeD(member.curMp);
        writeD(member.maxMp);
        writeD(member.level);
        writeD(member.class_id);
        writeD(0x00);// writeD(0x01); ??
        writeD(member.race_id);
        writeD(0x00);// Hide name
        writeD(0x00);// unknown
        writeD(replace);// Идет ли поиск замены игроку
    }
}