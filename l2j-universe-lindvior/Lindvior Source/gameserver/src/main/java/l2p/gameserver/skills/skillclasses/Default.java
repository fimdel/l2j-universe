package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.templates.StatsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Default extends Skill {
    private static final Logger _log = LoggerFactory.getLogger(Default.class);

    public Default(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (activeChar.isPlayer())
            activeChar.sendMessage(new CustomMessage("l2p.gameserver.skills.skillclasses.Default.NotImplemented", (Player) activeChar).addNumber(getId()).addString("" + getSkillType()));
        _log.warn("NOTDONE skill: " + getId() + ", used by" + activeChar);
        activeChar.sendActionFailed();
    }
}
