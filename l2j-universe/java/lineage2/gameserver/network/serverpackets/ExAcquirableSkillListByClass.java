package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.tables.SkillTable;

import java.util.Collection;

/**
 * @author ALF
 * @date 17.07.2012
 */
public class ExAcquirableSkillListByClass extends L2GameServerPacket {

    private final Collection<SkillLearn> allskills;

    public ExAcquirableSkillListByClass(Player player) {
        allskills = SkillAcquireHolder.getInstance().getAvailableAllSkills(player);
    }

    @Override
    protected final void writeImpl() {
        writeEx(0xFA);

        writeD(allskills.size());
        for (SkillLearn sk : allskills) {
            Skill skill = SkillTable.getInstance().getInfo(sk.getId(), sk.getLevel());

            if (skill == null) {
                continue;
            }

            writeD(sk.getId());
            writeD(sk.getLevel());
            writeD(sk.getCost());
            writeH(sk.getMinLevel());
            writeH(0x00); // Tauti
            boolean consumeItem = sk.getItemId() > 0;
            writeD(consumeItem ? 1 : 0);
            if (consumeItem) {
                writeD(sk.getItemId());
                writeQ(sk.getItemCount());
            }
            Skill relskill = SkillTable.getInstance().getInfo(sk.getId(), sk.getLevel());
            if (relskill != null && relskill.isRelationSkill()) {
                int[] _dels = relskill.getRelationSkills();
                writeD(_dels.length);// deletedSkillsSize
                for (int skillId : _dels) {
                    writeD(skillId);// skillId
                    writeD(SkillTable.getInstance().getBaseLevel(skillId));// skillLvl
                }
            } else {
                writeD(0x00);
            }
        }
    }
}
