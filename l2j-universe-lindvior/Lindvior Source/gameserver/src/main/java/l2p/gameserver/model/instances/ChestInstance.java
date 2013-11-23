package l2p.gameserver.model.instances;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.templates.npc.NpcTemplate;

public class ChestInstance extends MonsterInstance {
    /**
     *
     */
    private static final long serialVersionUID = 2516211559188406776L;

    public ChestInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    public void tryOpen(Player opener, Skill skill) {
        getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, opener, 100);
    }

    @Override
    public boolean canChampion() {
        return false;
    }
}