package npc.model;

import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

public class ImmuneMonsterInstance extends MonsterInstance {
    /**
     *
     */
    private static final long serialVersionUID = -4134872312064858336L;

    public ImmuneMonsterInstance(int objectId, NpcTemplate template) {
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
}