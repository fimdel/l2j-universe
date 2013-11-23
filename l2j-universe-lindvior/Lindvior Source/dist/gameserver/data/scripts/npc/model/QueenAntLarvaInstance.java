package npc.model;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

public class QueenAntLarvaInstance extends MonsterInstance {
    /**
     *
     */
    private static final long serialVersionUID = 5566866506114004106L;

    public QueenAntLarvaInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void reduceCurrentHp(double damage, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage) {
        damage = getCurrentHp() - damage > 1 ? damage : getCurrentHp() - 1;
        super.reduceCurrentHp(damage, reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
    }

    @Override
    public boolean canChampion() {
        return false;
    }

    @Override
    public boolean isImmobilized() {
        return true;
    }
}