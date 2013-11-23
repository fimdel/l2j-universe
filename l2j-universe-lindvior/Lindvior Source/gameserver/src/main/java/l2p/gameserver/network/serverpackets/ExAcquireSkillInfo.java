/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.SkillLearn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Darvin
 */
public class ExAcquireSkillInfo extends L2GameServerPacket {
    private Player player;
    private SkillLearn skillLearn;
    private List<Require> _reqs = Collections.emptyList();

    public ExAcquireSkillInfo(Player player, SkillLearn skillLearn) {
        this.player = player;
        this.skillLearn = skillLearn;
        if (skillLearn.getItemId() != 0) {
            _reqs = new ArrayList<Require>(1);
            _reqs.add(new Require(skillLearn.getItemId(), skillLearn.getItemCount()));
        }
    }

    @Override
    protected void writeImpl() {
        writeEx(0xFC);

        writeD(skillLearn.getId());//Skill ID
        writeD(skillLearn.getLevel());//Skill Level
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

    private static class Require {
        public int itemId;
        public long count;

        public Require(int pItemId, long pCount) {
            itemId = pItemId;
            count = pCount;
        }
    }
}
