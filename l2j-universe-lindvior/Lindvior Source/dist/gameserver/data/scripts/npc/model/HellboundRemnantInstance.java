package npc.model;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

public class HellboundRemnantInstance extends MonsterInstance {
    /**
     *
     */
    private static final long serialVersionUID = -8555108834204626091L;

    public HellboundRemnantInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void reduceCurrentHp(double i, double reflectableDamage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect, boolean transferDamage, boolean isDot, boolean sendMessage) {
        super.reduceCurrentHp(Math.min(i, getCurrentHp() - 1), reflectableDamage, attacker, skill, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
    }

    public void onUseHolyWater(Creature user) {
        if (getCurrentHp() < 100)
            doDie(user);
    }
}