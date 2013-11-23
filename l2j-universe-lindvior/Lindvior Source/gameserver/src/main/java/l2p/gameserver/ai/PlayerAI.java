package l2p.gameserver.ai;

import l2p.gameserver.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.*;
import l2p.gameserver.model.Skill.SkillType;
import l2p.gameserver.model.items.attachment.FlagItemAttachment;
import l2p.gameserver.network.serverpackets.ActionFail;
import l2p.gameserver.network.serverpackets.ExRotation;
import l2p.gameserver.network.serverpackets.SocialAction;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

import static l2p.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;

public class PlayerAI extends PlayableAI {
    public PlayerAI(Player actor) {
        super(actor);
    }

    @Override
    protected void onIntentionRest() {
        changeIntention(CtrlIntention.AI_INTENTION_REST, null, null);
        setAttackTarget(null);
        clientStopMoving();
    }

    @Override
    protected void onIntentionActive() {
        clearNextAction();
        changeIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
    }

    @Override
    public void onIntentionInteract(GameObject object) {
        Player actor = getActor();

        if (actor.getSittingTask()) {
            setNextAction(nextAction.INTERACT, object, null, false, false);
            return;
        } else if (actor.isSitting()) {
            actor.sendPacket(Msg.YOU_CANNOT_MOVE_WHILE_SITTING);
            clientActionFailed();
            return;
        }
        super.onIntentionInteract(object);
    }

    @Override
    public void onIntentionPickUp(GameObject object) {
        Player actor = getActor();

        if (actor.getSittingTask()) {
            setNextAction(nextAction.PICKUP, object, null, false, false);
            return;
        } else if (actor.isSitting()) {
            actor.sendPacket(Msg.YOU_CANNOT_MOVE_WHILE_SITTING);
            clientActionFailed();
            return;
        }
        super.onIntentionPickUp(object);
    }

    @Override
    protected void onEvtForgetObject(GameObject object) {
        if (object == null)
            return;
        super.onEvtForgetObject(object);

        for (Summon summon : getActor().getSummonList())
            summon.getAI().notifyEvent(CtrlEvent.EVT_FORGET_OBJECT, object);
    }

    @Override
    protected void thinkAttack(boolean checkRange) {
        Player actor = getActor();

        if (actor.isInFlyingTransform()) {
            setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
            return;
        }

        FlagItemAttachment attachment = actor.getActiveWeaponFlagAttachment();
        if (attachment != null && !attachment.canAttack(actor)) {
            setIntention(AI_INTENTION_ACTIVE);
            actor.sendActionFailed();
            return;
        }

        if (actor.isFrozen()) {
            setIntention(AI_INTENTION_ACTIVE);
            actor.sendPacket(SystemMsg.YOU_CANNOT_MOVE_WHILE_FROZEN, ActionFail.STATIC);
            return;
        }

        super.thinkAttack(checkRange);
    }

    @Override
    protected void thinkCast(boolean checkRange) {
        Player actor = getActor();

        FlagItemAttachment attachment = actor.getActiveWeaponFlagAttachment();
        if (attachment != null && !attachment.canCast(actor, _skill)) {
            setIntention(AI_INTENTION_ACTIVE);
            actor.sendActionFailed();
            return;
        }

        if (actor.isFrozen()) {
            setIntention(AI_INTENTION_ACTIVE);
            actor.sendPacket(SystemMsg.YOU_CANNOT_MOVE_WHILE_FROZEN, ActionFail.STATIC);
            return;
        }

        super.thinkCast(checkRange);
    }

    @Override
    protected void thinkCoupleAction(Player target, Integer socialId, boolean cancel) {
        Player actor = getActor();
        if (target == null || !target.isOnline()) {
            actor.sendPacket(Msg.COUPLE_ACTION_WAS_CANCELED);
            return;
        }

        if (cancel || !actor.isInRange(target, 50) || actor.isInRange(target, 20) || actor.getReflection() != target.getReflection() || !GeoEngine.canSeeTarget(actor, target, false)) {
            target.sendPacket(Msg.COUPLE_ACTION_WAS_CANCELED);
            actor.sendPacket(Msg.COUPLE_ACTION_WAS_CANCELED);
            return;
        }
        if (_forceUse) // служит только для флага что б активировать у другого игрока социалку
            target.getAI().setIntention(CtrlIntention.AI_INTENTION_COUPLE_ACTION, actor, socialId);
        //
        int heading = actor.calcHeading(target.getX(), target.getY());
        actor.setHeading(heading);
        actor.broadcastPacket(new ExRotation(actor.getObjectId(), heading));
        //
        actor.broadcastPacket(new SocialAction(actor.getObjectId(), socialId));
    }

    @Override
    public void Attack(GameObject target, boolean forceUse, boolean dontMove) {
        Player actor = getActor();

        if (actor.isInFlyingTransform()) {
            actor.sendActionFailed();
            return;
        }

        if (System.currentTimeMillis() - actor.getLastAttackPacket() < Config.ATTACK_PACKET_DELAY) {
            actor.sendActionFailed();
            return;
        }

        actor.setLastAttackPacket();

        if (actor.getSittingTask()) {
            setNextAction(nextAction.ATTACK, target, null, forceUse, false);
            return;
        } else if (actor.isSitting()) {
            actor.sendPacket(Msg.YOU_CANNOT_MOVE_WHILE_SITTING);
            clientActionFailed();
            return;
        }

        super.Attack(target, forceUse, dontMove);
    }

    @Override
    public void Cast(Skill skill, Creature target, boolean forceUse, boolean dontMove) {
        Player actor = getActor();

        if (!skill.altUse() && !skill.isToggle() && !(skill.getSkillType() == SkillType.CRAFT && Config.ALLOW_TALK_WHILE_SITTING))
            // Если в этот момент встаем, то использовать скилл когда встанем
            if (actor.getSittingTask()) {
                setNextAction(nextAction.CAST, skill, target, forceUse, dontMove);
                clientActionFailed();
                return;
            } else if (skill.getSkillType() == SkillType.SUMMON && actor.getPrivateStoreType() != Player.STORE_PRIVATE_NONE) {
                actor.sendPacket(Msg.YOU_CANNOT_SUMMON_DURING_A_TRADE_OR_WHILE_USING_THE_PRIVATE_SHOPS);
                clientActionFailed();
                return;
            }
            // если сидим - скиллы нельзя использовать
            else if (actor.isSitting()) {
                if (skill.getSkillType() == SkillType.TRANSFORMATION)
                    actor.sendPacket(Msg.YOU_CANNOT_TRANSFORM_WHILE_SITTING);
                else
                    actor.sendPacket(Msg.YOU_CANNOT_MOVE_WHILE_SITTING);

                clientActionFailed();
                return;
            }

        super.Cast(skill, target, forceUse, dontMove);
    }

    @Override
    public Player getActor() {
        return (Player) super.getActor();
    }
}