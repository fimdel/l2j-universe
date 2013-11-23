package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class ClanGate extends Skill {
    public ClanGate(StatsSet set) {
        super(set);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (!activeChar.isPlayer())
            return false;

        Player player = (Player) activeChar;
        if (!player.isClanLeader()) {
            player.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
            return false;
        }

        SystemMessage msg = Call.canSummonHere(player);
        if (msg != null) {
            activeChar.sendPacket(msg);
            return false;
        }

        return super.checkCondition(activeChar, target, forceUse, dontMove, first);
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        if (!activeChar.isPlayer())
            return;

        Player player = (Player) activeChar;
        Clan clan = player.getClan();
        clan.broadcastToOtherOnlineMembers(Msg.COURT_MAGICIAN__THE_PORTAL_HAS_BEEN_CREATED, player);

        getEffects(activeChar, activeChar, getActivateRate() > 0, true);
    }
}
