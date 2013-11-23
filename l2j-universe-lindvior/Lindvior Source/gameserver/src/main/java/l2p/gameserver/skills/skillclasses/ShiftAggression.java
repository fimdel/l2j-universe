package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.AggroList.AggroInfo;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class ShiftAggression extends Skill {
    public ShiftAggression(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (activeChar.getPlayer() == null)
            return;

        for (Creature target : targets)
            if (target != null) {
                if (!target.isPlayer())
                    continue;

                Player player = (Player) target;

                for (NpcInstance npc : World.getAroundNpc(activeChar, getSkillRadius(), getSkillRadius())) {
                    AggroInfo ai = npc.getAggroList().get(activeChar);
                    if (ai == null)
                        continue;
                    npc.getAggroList().addDamageHate(player, 0, ai.hate);
                    npc.getAggroList().remove(activeChar, true);
                }
            }

        if (isSSPossible())
            activeChar.unChargeShots(isMagic());
    }
}
