package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Effect;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill.SkillType;
import l2p.gameserver.model.Summon;
import l2p.gameserver.skills.EffectType;

public class RequestDispel extends L2GameClientPacket {
    private int _objectId, _id, _level;

    @Override
    protected void readImpl() throws Exception {
        _objectId = readD();
        _id = readD();
        _level = readD();
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();
        Creature target = activeChar;
        if (activeChar == null)
            return;
        if (activeChar.getObjectId() != _objectId) {
            for (Summon summon : activeChar.getSummonList()) {
                if (summon.getObjectId() == _objectId) {
                    target = summon;
                    break;
                }
            }
            if (target == null)
                return;
        }

        for (Effect e : target.getEffectList().getAllEffects())
            if (e.getDisplayId() == _id && e.getDisplayLevel() == _level)
                if (!e.isOffensive() && !e.getSkill().isMusic() && e.getSkill().isSelfDispellable() && e.getSkill().getSkillType() != SkillType.TRANSFORMATION && e.getTemplate().getEffectType() != EffectType.Hourglass)
                    e.exit();
                else
                    return;
    }
}