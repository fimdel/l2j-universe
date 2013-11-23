package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

/**
 * @author ALF
 * @data 25.02.2012
 */
public class Subjob extends Skill {

    public Subjob(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (!activeChar.isPlayer())
            return;
        Player player = (Player) activeChar;
        switch (getId()) {
            case 1566: // Main класс
                player.changeClass(1);
                break;
            case 1567: // Dual Class
                player.changeClass(2);
                break;
            case 1568: // Sub Class
                player.changeClass(3);
                break;
            case 1569: // Unk
                player.changeClass(4);
                break;
        }
    }

}
