package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2p.gameserver.model.entity.events.impl.SiegeEvent;
import l2p.gameserver.model.entity.residence.ResidenceSide;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class TakeCastle extends Skill {
    private final ResidenceSide _side;

    public TakeCastle(StatsSet set) {
        super(set);
        _side = set.getEnum("castle_side", ResidenceSide.class, ResidenceSide.NEUTRAL);
    }

    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (!super.checkCondition(activeChar, target, forceUse, dontMove, first))
            return false;

        if (activeChar == null || !activeChar.isPlayer())
            return false;

        Player player = (Player) activeChar;
        if (player.getClan() == null || !player.isClanLeader()) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
            return false;
        }

        CastleSiegeEvent siegeEvent = player.getEvent(CastleSiegeEvent.class);
        if (siegeEvent == null) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
            return false;
        }

        if (siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan()) == null) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
            return false;
        }

        if (player.isMounted()) {
            activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
            return false;
        }

        if (!player.isInRangeZ(target, 185)) {
            player.sendPacket(SystemMsg.YOUR_TARGET_IS_OUT_OF_RANGE);
            return false;
        }

        if (first)
            siegeEvent.broadcastTo(SystemMsg.THE_OPPOSING_CLAN_HAS_STARTED_TO_ENGRAVE_THE_HOLY_ARTIFACT, SiegeEvent.DEFENDERS);

        return true;
    }

    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        for (Creature target : targets)
            if (target != null) {
                if (!target.isArtefact())
                    continue;
                Player player = (Player) activeChar;

                CastleSiegeEvent siegeEvent = player.getEvent(CastleSiegeEvent.class);
                if (siegeEvent != null) {
                    siegeEvent.broadcastTo(new SystemMessage2(SystemMsg.CLAN_S1_HAS_SUCCESSFULLY_ENGRAVED_THE_HOLY_ARTIFACT).addString(player.getClan().getName()), SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
                    siegeEvent.takeCastle(player.getClan(), _side); //processStep(player.getClan());
                }
            }
    }
}
