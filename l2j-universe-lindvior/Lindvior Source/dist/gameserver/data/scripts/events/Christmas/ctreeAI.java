package events.Christmas;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;

public class ctreeAI extends DefaultAI {
    public ctreeAI(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor == null)
            return true;

        int skillId = 2139;
        for (Player player : World.getAroundPlayers(actor, 200, 200))
            if (player != null && !player.isInZonePeace() && player.getEffectList().getEffectsBySkillId(skillId) == null)
                actor.doCast(SkillTable.getInstance().getInfo(skillId, 1), player, true);
        return false;
    }

    @Override
    protected boolean randomAnimation() {
        return false;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}