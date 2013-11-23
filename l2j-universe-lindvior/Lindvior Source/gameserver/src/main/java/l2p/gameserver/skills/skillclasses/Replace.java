package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.*;
import l2p.gameserver.templates.StatsSet;
import l2p.gameserver.utils.Location;

import java.util.List;

public class Replace extends Skill {

    public Replace(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (!(activeChar instanceof Player))
            return;
        GameObject target = activeChar.getTarget();

        if (target == null) {
            activeChar.sendMessage("Не возможно использовать скил(Вы не выделили слугу в таргет.");
            return;
        }
        final Location loc_pet = target.getLoc();
        final Location loc_cha = activeChar.getLoc();

        activeChar.teleToLocation(loc_pet);
        ((Summon) target).teleToLocation(loc_cha);

    }
}
