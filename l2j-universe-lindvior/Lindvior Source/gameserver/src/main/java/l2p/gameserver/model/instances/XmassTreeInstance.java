package l2p.gameserver.model.instances;

import l2p.gameserver.model.Creature;
import l2p.gameserver.templates.npc.NpcTemplate;

public class XmassTreeInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -1416662127300750079L;

    public XmassTreeInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public boolean isAttackable(Creature attacker) {
        return false;
    }

    @Override
    public boolean isAutoAttackable(Creature attacker) {
        return false;
    }

    @Override
    public boolean hasRandomWalk() {
        return false;
    }

    @Override
    public boolean isFearImmune() {
        return true;
    }

    @Override
    public boolean isParalyzeImmune() {
        return true;
    }

    @Override
    public boolean isLethalImmune() {
        return true;
    }
}