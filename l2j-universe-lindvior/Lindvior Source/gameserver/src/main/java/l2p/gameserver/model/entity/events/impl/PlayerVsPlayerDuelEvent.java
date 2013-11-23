package l2p.gameserver.model.entity.events.impl;

import l2p.commons.collections.MultiValueSet;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.model.GameObject;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Request;
import l2p.gameserver.model.base.TeamType;
import l2p.gameserver.model.entity.events.objects.DuelSnapshotObject;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

import java.util.List;

/**
 * @author VISTALL
 * @date 3:26/29.06.2011
 */
public class PlayerVsPlayerDuelEvent extends DuelEvent {
    public PlayerVsPlayerDuelEvent(MultiValueSet<String> set) {
        super(set);
    }

    protected PlayerVsPlayerDuelEvent(int id, String name) {
        super(id, name);
    }

    @Override
    public boolean canDuel(Player player, Player target, boolean first) {
        IStaticPacket sm = canDuel0(player, target);
        if (sm != null) {
            player.sendPacket(sm);
            return false;
        }

        sm = canDuel0(target, player);
        if (sm != null) {
            player.sendPacket(SystemMsg.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
            return false;
        }

        return true;
    }

    @Override
    public void askDuel(Player player, Player target) {
        Request request = new Request(Request.L2RequestType.DUEL, player, target).setTimeout(10000L);
        request.set("duelType", 0);
        player.setRequest(request);
        target.setRequest(request);

        player.sendPacket(new SystemMessage2(SystemMsg.C1_HAS_BEEN_CHALLENGED_TO_A_DUEL).addName(target));
        target.sendPacket(new SystemMessage2(SystemMsg.C1_HAS_CHALLENGED_YOU_TO_A_DUEL).addName(player), new ExDuelAskStart(player.getName(), 0));
    }

    @Override
    public void createDuel(Player player, Player target) {
        PlayerVsPlayerDuelEvent duelEvent = new PlayerVsPlayerDuelEvent(getDuelType(), player.getObjectId() + "_" + target.getObjectId() + "_duel");
        cloneTo(duelEvent);

        duelEvent.addObject(BLUE_TEAM, new DuelSnapshotObject(player, TeamType.BLUE));
        duelEvent.addObject(RED_TEAM, new DuelSnapshotObject(target, TeamType.RED));
        duelEvent.sendPacket(new ExDuelReady(this));
        duelEvent.reCalcNextTime(false);
    }

    @Override
    public void stopEvent() {
        clearActions();

        updatePlayers(false, false);

        for (DuelSnapshotObject d : this) {
            d.getPlayer().sendPacket(new ExDuelEnd(this));
            GameObject target = d.getPlayer().getTarget();
            if (target != null)
                d.getPlayer().getAI().notifyEvent(CtrlEvent.EVT_FORGET_OBJECT, target);
        }

        switch (_winner) {
            case NONE:
                sendPacket(SystemMsg.THE_DUEL_HAS_ENDED_IN_A_TIE);
                break;
            case RED:
            case BLUE:
                List<DuelSnapshotObject> winners = getObjects(_winner.name());
                List<DuelSnapshotObject> lossers = getObjects(_winner.revert().name());

                sendPacket(new SystemMessage2(SystemMsg.C1_HAS_WON_THE_DUEL).addName(winners.get(0).getPlayer()));

                for (DuelSnapshotObject d : lossers)
                    d.getPlayer().broadcastPacket(new SocialAction(d.getPlayer().getObjectId(), SocialAction.BOW));
                break;
        }

        removeObjects(RED_TEAM);
        removeObjects(BLUE_TEAM);
    }

    @Override
    public void onDie(Player player) {
        TeamType team = player.getTeam();
        if (team == TeamType.NONE || _aborted)
            return;

        boolean allDead = true;
        List<DuelSnapshotObject> objs = getObjects(team.name());
        for (DuelSnapshotObject obj : objs) {
            if (obj.getPlayer() == player)
                obj.setDead();

            if (!obj.isDead())
                allDead = false;
        }

        if (allDead) {
            _winner = team.revert();

            stopEvent();
        }
    }

    @Override
    public int getDuelType() {
        return 0;
    }

    @Override
    public void playerExit(Player player) {
        if (_winner != TeamType.NONE || _aborted)
            return;

        _winner = player.getTeam().revert();
        _aborted = false;

        stopEvent();
    }

    @Override
    public void packetSurrender(Player player) {
        playerExit(player);
    }

    @Override
    protected long startTimeMillis() {
        return System.currentTimeMillis() + 5000L;
    }
}
