/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai.Kartia;

import l2p.commons.util.Rnd;
import l2p.gameserver.Config;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;

import java.util.concurrent.ScheduledFuture;

public class KartiaGuard extends Fighter {

    private long _ReuseTimer = 0;
    /**
     * Field _followTask.
     */
    ScheduledFuture<?> _followTask;
    Creature master = null;

    /**
     * Constructor for FollowNpc.
     *
     * @param actor NpcInstance
     */
    public KartiaGuard(NpcInstance actor) {
        super(actor);
    }

    /**
     * Method canAttackCharacter.
     *
     * @param target Creature
     * @return boolean
     */
    @Override
    public boolean canAttackCharacter(Creature target) {
        return target.isMonster();
    }

    /**
     * Method onEvtThink.
     */
    @Override
    protected void onEvtThink() {
        final NpcInstance actor = getActor();
        if (master == null)
            master = getActor().getFollowTarget();
        //Check for Heal
        if (actor.getNpcId() == 33639 || actor.getNpcId() == 33628 || actor.getNpcId() == 33617) {
            if (master != null && !master.isDead() && master.getCurrentHpPercents() < 50) {
                if (!actor.isCastingNow() && (_ReuseTimer < System.currentTimeMillis())) {
                    actor.doCast(SkillTable.getInstance().getInfo(698, 1), master, true);
                    _ReuseTimer = System.currentTimeMillis() + (3 * 1000L);
                }
            }
        }
        if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK) {
            //Check for Mobs to Attack
            int mobscount = 0;
            for (NpcInstance npc : actor.getAroundNpc(600, 100)) {
                if (npc instanceof MonsterInstance) {
                    actor.getAggroList().addDamageHate(npc, 10, 10);
                    mobscount++;
                }
            }
            if (mobscount > 0 && !actor.getAggroList().isEmpty()) {
                Attack(actor.getAggroList().getRandomHated(), true, false);
            }
            //Check for Follow
            else {
                if (getIntention() == CtrlIntention.AI_INTENTION_ACTIVE) {
                    setIntention(CtrlIntention.AI_INTENTION_FOLLOW);
                }
                if (master != null && actor.getDistance(master.getLoc()) > 300) {
                    final Location loc = new Location(master.getX() + Rnd.get(-120, 120), master.getY() + Rnd.get(-120, 120), master.getZ());
                    actor.followToCharacter(loc, master, Config.FOLLOW_RANGE, false);
                    actor.setRunning();
                }
            }
        }
        super.onEvtThink();
    }
}

