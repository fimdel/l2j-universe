package ai.freya;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Zone.ZoneType;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.tables.SkillTable;

import java.util.List;

/**
 * @author pchayka
 */

public class Maguen extends Fighter {
    private static final int[] maguenStatsSkills = {6343, 6365, 6366};
    private static final int[] maguenRaceSkills = {6367, 6368, 6369};

    public Maguen(NpcInstance actor) {
        super(actor);
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
        ThreadPoolManager.getInstance().schedule(new Plasma(), 2000L);
        ThreadPoolManager.getInstance().schedule(new Despawn(), 10000L);
        List<Creature> around = getActor().getAroundCharacters(800, 300);
        if (!getActor().isInZone(ZoneType.dummy) && around != null && !around.isEmpty()) {
            ExShowScreenMessage sm = new ExShowScreenMessage(NpcString.MAGUEN_APPEARANCE, 5000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true);
            for (Creature character : around)
                if (character.isPlayer())
                    character.sendPacket(sm);
        }
    }

    @Override
    protected void onEvtSeeSpell(Skill skill, Creature caster) {
        if (skill.getId() != 9060)
            return;
        NpcInstance actor = getActor();
        if (actor.isInZone(ZoneType.dummy))
            switch (actor.getNpcState()) {
                case 1:
                    if (Rnd.chance(80))
                        actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[0], Rnd.get(2, 3)), caster, true);
                    else
                        actor.doCast(SkillTable.getInstance().getInfo(maguenStatsSkills[0], Rnd.get(1, 2)), caster, true);
                    break;
                case 2:
                    if (Rnd.chance(80))
                        actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[1], Rnd.get(2, 3)), caster, true);
                    else
                        actor.doCast(SkillTable.getInstance().getInfo(maguenStatsSkills[1], Rnd.get(1, 2)), caster, true);
                    break;
                case 3:
                    if (Rnd.chance(80))
                        actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[2], Rnd.get(2, 3)), caster, true);
                    else
                        actor.doCast(SkillTable.getInstance().getInfo(maguenStatsSkills[2], Rnd.get(1, 2)), caster, true);
                    break;
                default:
                    break;
            }
        else
            switch (actor.getNpcState()) {
                case 1:
                    actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[0], 1), caster, true);
                    break;
                case 2:
                    actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[1], 1), caster, true);
                    break;
                case 3:
                    actor.doCast(SkillTable.getInstance().getInfo(maguenRaceSkills[2], 1), caster, true);
                    break;
                default:
                    break;
            }
        getActor().setNpcState(4);
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        if (attacker == null)
            return;

        if (attacker.isPlayable())
            return;

        super.onEvtAttacked(attacker, damage);
    }

    @Override
    public boolean checkAggression(Creature target) {
        if (target.isPlayable())
            return false;

        return super.checkAggression(target);
    }

    private class Plasma extends RunnableImpl {
        @Override
        public void runImpl() {
            getActor().setNpcState(Rnd.get(1, 3));
        }
    }

    private class Despawn extends RunnableImpl {
        @Override
        public void runImpl() {
            getActor().setNpcState(4);
            getActor().doDie(null);
        }
    }
}