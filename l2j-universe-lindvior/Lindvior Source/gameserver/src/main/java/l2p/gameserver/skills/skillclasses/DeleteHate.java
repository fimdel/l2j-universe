package l2p.gameserver.skills.skillclasses;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class DeleteHate extends Skill {
    public DeleteHate(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null) {

                if (target.isRaid())
                    continue;

                if (getActivateRate() > 0) {
                    if (activeChar.isPlayer() && ((Player) activeChar).isGM())
                        activeChar.sendMessage(new CustomMessage("l2p.gameserver.skills.Formulas.Chance", (Player) activeChar).addString(getName()).addNumber(getActivateRate()));

                    if (!Rnd.chance(getActivateRate()))
                        return;
                }

                if (target.isNpc()) {
                    NpcInstance npc = (NpcInstance) target;
                    npc.getAggroList().clear(false);
                    npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                }

                getEffects(activeChar, target, false, false);
            }
    }
}
