/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.data.xml.holder.SkillAcquireHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.SkillLearn;
import l2p.gameserver.model.base.AcquireType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExAcquirableSkillListByClass extends L2GameServerPacket {
    private Player player;
    private Collection<SkillLearn> skills;
    private List<Require> _reqs = Collections.emptyList();

    public ExAcquirableSkillListByClass(Player player) {
        this.player = player;
        skills = new ArrayList<SkillLearn>();
        for (SkillLearn skill : SkillAcquireHolder.getInstance().getAvailableSkills(player, AcquireType.NORMAL, true)) {
            if (skill.getCost() != 0) {
                skills.add(skill);
            }
        }
    }

    @Override
    protected final void writeImpl() {
        writeEx(0xFA);

        writeD(skills.size());
        for (SkillLearn skillLearn : skills) {
            writeD(skillLearn.getId());// skill id
            writeD(skillLearn.getLevel());// skill level
            writeD(skillLearn.getCost());// sp_cost
            writeH(skillLearn.getMinLevel());// Required Level
            writeH(0);//Glory Days      //479 протокол
            writeD(_reqs.size());
            for (Require temp : _reqs) {
                writeD(temp.itemId);
                writeQ(temp.count);
            }

            writeD(skillLearn.getRemovedSkillsForPlayer(player).size());// deletedSkillsSize
            for (Skill skill : skillLearn.getRemovedSkillsForPlayer(player)) {
                writeD(skill.getId());// skillId
                writeD(skill.getLevel());// skillLvl
            }
        }
    }

    private static class Require {
        public int itemId;
        public long count;

        @SuppressWarnings("unused")
        public Require(int pItemId, long pCount) {
            itemId = pItemId;
            count = pCount;
        }
    }
}
