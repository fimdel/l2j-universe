package ai;


import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.tables.SkillTable;


/**
 * @author ALF
 */
public class Wispes extends DefaultAI {
	public Wispes (NpcInstance actor) {
		super(actor);
		AI_TASK_ACTIVE_DELAY = 2000;
		AI_TASK_ATTACK_DELAY = 2000;
	}
    @Override
	protected boolean thinkActive() {
		if (!_def_think) {
			NpcInstance npc = getActor();
			if (npc == null)
				return true;
            List<Creature> target = new ArrayList<Creature>();
                for (Player player : World.getAroundPlayers(npc, 300, 300)) {
                    if (player.getEffectList().getEffectsBySkillId(12001) == null) {
                        target.add(player);
                        if (npc.getNpcId() == 32915) {
                            npc.broadcastPacket(new MagicSkillUse(npc, player, 14064, 1, 0, 0));
                            npc.callSkill(SkillTable.getInstance().getInfo(14064, 1), target, true);
                        }
						else if (npc.getNpcId() == 32916) {
                            npc.broadcastPacket(new MagicSkillUse(npc, player, 14065, 1, 0, 0));
                            npc.callSkill(SkillTable.getInstance().getInfo(14065, 1), target, true);
                        }
                    }
				if (target.size() > 0) {
					target.clear();
					npc.deleteMe();
				}
        }
    }
	  return true;
    }

	@Override
	public boolean isGlobalAI() {
		return true;
	}
}