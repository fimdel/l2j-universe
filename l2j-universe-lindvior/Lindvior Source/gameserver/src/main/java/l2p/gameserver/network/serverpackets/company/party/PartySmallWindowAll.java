/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.instancemanager.PartySubstituteManager;
import l2p.gameserver.model.Party;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Summon;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * format ddd+[dSddddddddddddd{ddSddddd}]
 */
public class PartySmallWindowAll extends L2GameServerPacket {
    private final int leaderId, loot;
    private final List<PartySmallWindowMemberInfo> members = new ArrayList<PartySmallWindowMemberInfo>();

    public PartySmallWindowAll(Party party, Player exclude) {
        leaderId = party.getPartyLeader().getObjectId();
        loot = party.getLootDistribution();

        for (Player member : party.getPartyMembers()) {
            if (member != exclude) {
                members.add(new PartySmallWindowMemberInfo(member));
            }
        }
    }

    @Override
    protected final void writeImpl() {
        writeC(0x4E);
        writeD(leaderId); // c3 party leader id
        writeD(loot); // c3 party loot type (0,1,2,....)
        writeD(members.size());
        for (PartySmallWindowMemberInfo member : members) {
            writeD(member._id);
            writeS(member._name);
            writeD(member.curCp);
            writeD(member.maxCp);
            writeD(member.curHp);
            writeD(member.maxHp);
            writeD(member.curMp);
            writeD(member.maxMp);
            writeD(member.vitality);
            writeD(member.level);
            writeD(member.class_id);
            writeD(0);// writeD(0x01); ??
            writeD(member.race_id);
            writeD(0x00); // Hide Name
            writeD(0x00); //
            writeD(member.replace);// Ищется ли замена данному игроку.

            writeD(member.summonInfos.size());
            for (PartySmallWindowMemberInfo.PartySmallWindowSummonInfo summon : member.summonInfos) {
                writeD(summon.summon_id);
                writeD(summon.summon_NpcId);
                writeD(summon.summon_type);
                writeS(summon.summon_Name);
                writeD(summon.summon_curHp);
                writeD(summon.summon_maxHp);
                writeD(summon.summon_curMp);
                writeD(summon.summon_maxMp);
                writeD(summon.summon_level);
            }
        }
    }

    public static class PartySmallWindowMemberInfo {
        public String _name;
        public int _id, curCp, maxCp, curHp, maxHp, curMp, maxMp, level, class_id, race_id, vitality, replace;
        public List<PartySmallWindowSummonInfo> summonInfos;

        public PartySmallWindowMemberInfo(Player member) {
            _name = member.getName();
            _id = member.getObjectId();
            curCp = (int) member.getCurrentCp();
            maxCp = member.getMaxCp();
            vitality = member.getVitality().getPoints();
            curHp = (int) member.getCurrentHp();
            maxHp = member.getMaxHp();
            curMp = (int) member.getCurrentMp();
            maxMp = member.getMaxMp();
            level = member.getLevel();
            class_id = member.getClassId().getId();
            race_id = member.getRace().ordinal();
            summonInfos = new ArrayList<PartySmallWindowSummonInfo>(member.getSummonList().size());
            for (Summon summon : member.getSummonList()) {
                summonInfos.add(new PartySmallWindowSummonInfo(summon));
            }
            replace = PartySubstituteManager.getInstance().isPlayerToReplace(member) ? 1 : 0;
        }

        public static class PartySmallWindowSummonInfo {

            public int summon_type, summon_id, summon_NpcId, summon_level;
            public int summon_curHp, summon_maxHp, summon_curMp, summon_maxMp;
            public String summon_Name;

            public PartySmallWindowSummonInfo(Summon summon) {
                summon_type = summon.getSummonType();
                summon_id = summon.getObjectId();
                summon_NpcId = summon.getNpcId() + 1000000;
                summon_Name = summon.getName();
                summon_curHp = (int) summon.getCurrentHp();
                summon_maxHp = summon.getMaxHp();
                summon_curMp = (int) summon.getCurrentMp();
                summon_maxMp = summon.getMaxMp();
                summon_level = summon.getLevel();
            }
        }
    }
}