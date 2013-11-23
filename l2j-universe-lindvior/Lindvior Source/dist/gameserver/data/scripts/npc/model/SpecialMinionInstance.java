package npc.model;

import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * @author pchayka
 */
public final class SpecialMinionInstance extends MonsterInstance {
    /**
     *
     */
    private static final long serialVersionUID = 8714691609313528687L;

    public SpecialMinionInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
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

    @Override
    public boolean canChampion() {
        return false;
    }

    @Override
    public void onRandomAnimation() {
        return;
    }

}