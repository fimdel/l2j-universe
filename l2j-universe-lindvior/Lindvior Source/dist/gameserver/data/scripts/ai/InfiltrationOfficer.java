/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Guard;
import l2p.gameserver.listener.actor.OnAttackListener;
import l2p.gameserver.listener.actor.OnMagicUseListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.NpcSay;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.utils.Location;


public class InfiltrationOfficer extends Guard implements OnAttackListener, OnMagicUseListener

{
    public enum State {
        AI_IDLE(0),
        AI_FOLLOW(1),
        AI_ATTACK_GENERATOR(2),
        AI_NEXT_STEP(3);

        private final int _id;

        State(int id) {
            _id = id;
        }
    }

    private final static int GENERATOR = 33216;

    private final static int[][] POINTS = {{-117032, 212568, -8617}, {-117896, 214264, -8617}, {-119208, 213768, -8617},};

    private boolean configured = false;
    private short _step = 0;
    private State _state = State.AI_IDLE;
    /**
     * Last time when NPC follows to player location
     */
    private long lastFollowPlayer = 0;
    private boolean attacksGenerator = false;
    private long lastOfficerSay = 0;
    private Player player = null;

    public InfiltrationOfficer(NpcInstance actor) {
        super(actor);
        actor.setRunning();
    }

    private void config() {
        if (!configured) {
            getActor().getFollowTarget().addListener(this);
            configured = true;
        }
    }

    //	@Override
    //	protected void returnHome(boolean clearAggro, boolean teleport) {}

    //	@Override
    //	protected boolean randomWalk() { return true; }

    //	@Override
    //	protected boolean maybeMoveToHome() { return false; }

    @Override
    public void onAttack(Creature actor, Creature target) {
        if (isUnderState(State.AI_FOLLOW) && target.isMonster()) {
            getActor().getAggroList().addDamageHate(target, 0, 1);
            setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
        }
    }

    @Override
    public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt) {
        if (isUnderState(State.AI_FOLLOW) && target.isMonster()) {
            getActor().getAggroList().addDamageHate(target, 0, 1);
            setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
        }
    }

    public void setState(State state) {
        config();
        _state = state;
    }

    public boolean isUnderState(State state) {
        return _state == state;
    }

    @Override
    protected void onEvtSpawn() {
        super.onEvtSpawn();
    }

    @Override
    public void onEvtDeSpawn() {
        if (getActor().getFollowTarget() != null)
            getActor().getFollowTarget().removeListener(this);
    }

    @Override
    protected void thinkAttack() {
        super.thinkAttack();
    }

    @Override
    protected boolean thinkActive() {
        if (isUnderState(State.AI_IDLE))
            return false;

        NpcInstance actor = getActor();
        if (player == null)
            player = actor.getFollowTarget().getPlayer();
        if (player == null)
            return false;
        actor.setRunning();

        // Follow by player
        if (isUnderState(State.AI_FOLLOW) && actor.getAggroList().isEmpty() && System.currentTimeMillis() - lastFollowPlayer > 2000) {
            lastFollowPlayer = System.currentTimeMillis();
            actor.setFollowTarget(player);
            actor.moveToLocation(player.getLoc(), Rnd.get(200), true);
        }

        // Going to the next (new location after door opening)
        else if (isUnderState(State.AI_NEXT_STEP)) {
            if (_step < POINTS.length) {
                actor.setFollowTarget(player);
                actor.moveToLocation(new Location(POINTS[_step][0], POINTS[_step][1], POINTS[_step][2]), 0, true);
                ++_step;
            }
            setState(State.AI_IDLE);
        }
        // Attacking electricity generator
        else if (isUnderState(State.AI_ATTACK_GENERATOR)) {
            if (!attacksGenerator) {
                actor.getAggroList().clear();
                actor.getAggroList().addDamageHate(actor.getReflection().getAllByNpcId(GENERATOR, true).get(0), 0, Integer.MAX_VALUE);
                setIntention(CtrlIntention.AI_INTENTION_ATTACK);
                attacksGenerator = true;
            }
            if (System.currentTimeMillis() - lastOfficerSay > 3000) {
                actor.broadcastPacket(new NpcSay(actor, ChatType.ALL, NpcString.DONT_COME_BACK_HERE));
                lastOfficerSay = System.currentTimeMillis();
            }
        }

        return false;
    }

    @Override
    protected void onEvtAttacked(Creature attacker, int damage) {
        if (getActor().getFollowTarget() != null)
            getActor().getFollowTarget().sendPacket(new ExShowScreenMessage(NpcString.IF_TERAIN_DIES_MISSION_WILL_FAIL, 3000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, true));
    }
}