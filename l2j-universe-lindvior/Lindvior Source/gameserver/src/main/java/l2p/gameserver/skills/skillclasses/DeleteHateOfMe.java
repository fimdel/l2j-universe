package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.stats.Formulas;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class DeleteHateOfMe extends Skill {
    public DeleteHateOfMe(StatsSet set) {
        super(set);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null) {
                if (activeChar.isPlayer() && ((Player) activeChar).isGM())
                    activeChar.sendMessage(new CustomMessage("l2p.gameserver.skills.Formulas.Chance", (Player) activeChar).addString(getName()).addNumber(getActivateRate()));

                if (target.isNpc() && Formulas.calcSkillSuccess(activeChar, target, this, getActivateRate())) {
                    NpcInstance npc = (NpcInstance) target;
                    npc.getAggroList().remove(activeChar, true);
                    npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                }
                getEffects(activeChar, target, true, false);
            }
    }
}
