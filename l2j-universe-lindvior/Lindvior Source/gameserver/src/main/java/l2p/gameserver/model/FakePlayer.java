/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.PlayableAI;
import l2p.gameserver.listener.actor.OnAttackListener;
import l2p.gameserver.listener.actor.OnMagicUseListener;
import l2p.gameserver.model.entity.events.GlobalEvent;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.scripts.Events;
import l2p.gameserver.templates.item.WeaponTemplate;
import l2p.gameserver.templates.player.PlayerTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/*
*       По сути - упрощенная копия плеера для скилов Которые спавнят копию плеера. И для статуй статистики музея.
*/
public class FakePlayer extends Playable {
    private static final long serialVersionUID = -7275714049223105460L;

    private final Player _owner;

    private OwnerAttakListener _listener;

    public FakePlayer(int objectId, PlayerTemplate template, Player owner) {
        super(objectId, template);
        _owner = owner;
        _ai = new PlayableAI(this);
        _listener = new OwnerAttakListener();
        owner.addListener(_listener);
        ThreadPoolManager.getInstance().schedule(new DeleteMeTimer(this), 60000L);

        getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, getPlayer(), Config.FOLLOW_RANGE);
    }

    @Override
    public Player getPlayer() {
        return _owner;
    }

    @Override
    public PlayableAI getAI() {
        return (PlayableAI) _ai;
    }

    @Override
    public boolean isAttackable(Creature attacker) {
        return isCtrlAttackable(attacker, true, false);
    }

    @Override
    public boolean isAutoAttackable(Creature attacker) {
        return isCtrlAttackable(attacker, false, false);
    }

    public boolean isCtrlAttackable(Creature attacker, boolean force, boolean witchCtrl) {
        Player player = getPlayer();
        if (attacker == null || player == null || attacker == this || attacker == player && !force || isAlikeDead() || attacker.isAlikeDead())
            return false;

        if (isInvisible() || getReflection() != attacker.getReflection())
            return false;

        if (isInBoat())
            return false;

        for (GlobalEvent e : getEvents())
            if (e.checkForAttack(this, attacker, null, force) != null)
                return false;

        for (GlobalEvent e : player.getEvents())
            if (e.canAttack(this, attacker, null, force))
                return true;

        Player pcAttacker = attacker.getPlayer();

        if (pcAttacker != null && pcAttacker != player) {
            if (pcAttacker.isInBoat())
                return false;

            if (pcAttacker.getBlockCheckerArena() > -1 || player.getBlockCheckerArena() > -1)
                return false;

            // Player with lvl < 21 can't attack a cursed weapon holder, and a cursed weapon holder can't attack players with lvl < 21
            if (pcAttacker.isCursedWeaponEquipped() && player.getLevel() < 21 || player.isCursedWeaponEquipped() && pcAttacker.getLevel() < 21)
                return false;

            if (player.isInZone(Zone.ZoneType.epic) != pcAttacker.isInZone(Zone.ZoneType.epic))
                return false;

            if ((player.isInOlympiadMode() || pcAttacker.isInOlympiadMode()) && player.getOlympiadGame() != pcAttacker.getOlympiadGame()) // На всякий случай
                return false;
            if (player.isInOlympiadMode() && !player.isOlympiadCompStart()) // Бой еще не начался
                return false;
            if (player.isInOlympiadMode() && player.isOlympiadCompStart() && player.getOlympiadSide() == pcAttacker.getOlympiadSide() && !force) // Свою команду атаковать нельзя
                return false;
            if (isInZonePeace())
                return false;
            if (isInZoneBattle())
                return true;
            if (!force && player.getParty() != null && player.getParty() == pcAttacker.getParty())
                return false;
            if (!force && player.getClan() != null && player.getClan() == pcAttacker.getClan())
                return false;
            if (isInZone(Zone.ZoneType.SIEGE))
                return true;
            if (pcAttacker.atMutualWarWith(player))
                return true;
            if (player.getKarma() < 0 || player.getPvpFlag() != 0)
                return true;
            if (witchCtrl && player.getPvpFlag() > 0)
                return true;

            return force;
        }

        return true;
    }

    @Override
    public int getLevel() {
        return _owner.getLevel();
    }

    @Override
    public ItemInstance getActiveWeaponInstance() {
        return _owner.getActiveWeaponInstance();
    }

    @Override
    public WeaponTemplate getActiveWeaponItem() {
        return _owner.getActiveWeaponItem();
    }

    @Override
    public ItemInstance getSecondaryWeaponInstance() {
        return _owner.getSecondaryWeaponInstance();
    }

    @Override
    public WeaponTemplate getSecondaryWeaponItem() {
        return _owner.getSecondaryWeaponItem();
    }

    @Override
    public void onAction(final Player player, boolean shift) {
        if (isFrozen()) {
            player.sendPacket(ActionFail.STATIC);
            return;
        }

        if (Events.onAction(player, this, shift)) {
            player.sendPacket(ActionFail.STATIC);
            return;
        }

        Player owner = getPlayer();

        if (player.getTarget() != this) {
            player.setTarget(this);
            if (player.getTarget() == this)
                player.sendPacket(new MyTargetSelected(getObjectId(), 0), makeStatusUpdate(StatusUpdate.CUR_HP, StatusUpdate.MAX_HP, StatusUpdate.CUR_MP, StatusUpdate.MAX_MP));
            else
                player.sendPacket(ActionFail.STATIC);
        } else if (player == owner) {
            player.sendPacket(new CharInfo(this));
            player.sendPacket(ActionFail.STATIC);
        } else if (isAutoAttackable(player))
            player.getAI().Attack(this, false, shift);
        else if (player.getAI().getIntention() != CtrlIntention.AI_INTENTION_FOLLOW) {
            if (!shift)
                player.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, this, Config.FOLLOW_RANGE);
            else
                player.sendActionFailed();
        } else
            player.sendActionFailed();
    }

    private ScheduledFuture<?> _broadcastCharInfoTask;

    public class BroadcastCharInfoTask extends RunnableImpl {
        @Override
        public void runImpl() throws Exception {
            broadcastCharInfoImpl();
            _broadcastCharInfoTask = null;
        }
    }

    @Override
    public void broadcastCharInfo() {
        if (!isVisible())
            return;

        if (_broadcastCharInfoTask != null)
            return;

        _broadcastCharInfoTask = ThreadPoolManager.getInstance().schedule(new BroadcastCharInfoTask(), Config.BROADCAST_CHAR_INFO_INTERVAL);
    }

    public void broadcastCharInfoImpl() {
        for (Player player : World.getAroundPlayers(this))
            player.sendPacket(new CharInfo(this));
    }

    @Override
    public List<L2GameServerPacket> addPacketList(Player forPlayer, Creature dropper) {
        if (isInvisible() && forPlayer.getObjectId() != getObjectId())
            return Collections.emptyList();

        List<L2GameServerPacket> list = new ArrayList<L2GameServerPacket>();
        list.add(new CharInfo(this));

        if (isCastingNow()) {
            Creature castingTarget = getCastingTarget();
            Skill castingSkill = getCastingSkill();
            long animationEndTime = getAnimationEndTime();
            if (castingSkill != null && castingTarget != null && castingTarget.isCreature() && getAnimationEndTime() > 0)
                list.add(new MagicSkillUse(this, castingTarget, castingSkill.getId(), castingSkill.getLevel(), (int) (animationEndTime - System.currentTimeMillis()), 0L));
        }

        if (isInCombat())
            list.add(new AutoAttackStart(getObjectId()));

        if (isMoving || isFollow)
            list.add(movePacket());
        return list;
    }

    public void notifyOwerStartAttak(Creature targets) {
        getAI().Attack(targets, true, false);
    }

    private class OwnerAttakListener implements OnAttackListener, OnMagicUseListener {

        @Override
        public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt) {
            notifyOwerStartAttak(target);
        }

        @Override
        public void onAttack(Creature actor, Creature target) {
            notifyOwerStartAttak(target);
        }

    }

    private class DeleteMeTimer extends RunnableImpl {

        private FakePlayer _p;

        public DeleteMeTimer(FakePlayer p) {
            _p = p;
        }

        @Override
        public void runImpl() throws Exception {
            _p.deleteMe();
        }
    }

    @Override
    public <E extends GlobalEvent> E getEvent(Class<E> eventClass) {
        Player player = getPlayer();
        if (player != null)
            return player.getEvent(eventClass);
        else
            return super.getEvent(eventClass);
    }

    @Override
    public Set<GlobalEvent> getEvents() {
        Player player = getPlayer();
        if (player != null)
            return player.getEvents();
        else
            return super.getEvents();
    }

    @Override
    public boolean isPlayable() {
        return true;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public long getWearedMask() {
        return 0;
    }

    @Override
    public void doPickupItem(GameObject object) {
    }

}
