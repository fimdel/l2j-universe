package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.TrapInstance;
import l2p.gameserver.network.serverpackets.NpcInfo;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class DetectTrap extends Skill {
    public DetectTrap(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null && target.isTrap()) {
                TrapInstance trap = (TrapInstance) target;
                if (trap.getLevel() <= getPower()) {
                    trap.setDetected(true);
                    for (Player player : World.getAroundPlayers(trap))
                        player.sendPacket(new NpcInfo(trap, player));
                }
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
