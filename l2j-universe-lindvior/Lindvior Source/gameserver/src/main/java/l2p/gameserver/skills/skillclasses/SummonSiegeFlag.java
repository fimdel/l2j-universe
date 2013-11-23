package l2p.gameserver.skills.skillclasses;

import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Skill;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.entity.events.impl.DominionSiegeEvent;
import l2p.gameserver.model.entity.events.impl.SiegeEvent;
import l2p.gameserver.model.entity.events.objects.SiegeClanObject;
import l2p.gameserver.model.entity.events.objects.ZoneObject;
import l2p.gameserver.model.instances.residences.SiegeFlagInstance;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.stats.Stats;
import l2p.gameserver.stats.funcs.FuncMul;
import l2p.gameserver.templates.StatsSet;

import java.util.List;

public class SummonSiegeFlag extends Skill {
    public static enum FlagType {
        DESTROY,
        NORMAL,
        ADVANCED,
        OUTPOST
    }

    private final FlagType _flagType;
    private final double _advancedMult;

    public SummonSiegeFlag(StatsSet set) {
        super(set);
        _flagType = set.getEnum("flagType", FlagType.class);
        _advancedMult = set.getDouble("advancedMultiplier", 1.);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first) {
        if (!activeChar.isPlayer())
            return false;
        if (!super.checkCondition(activeChar, target, forceUse, dontMove, first))
            return false;

        Player player = (Player) activeChar;
        if (player.getClan() == null || !player.isClanLeader())
            return false;

        switch (_flagType) {
            case DESTROY:
                //
                break;
            case OUTPOST:
            case NORMAL:
            case ADVANCED:
                if (player.isInZone(Zone.ZoneType.RESIDENCE)) {
                    player.sendPacket(SystemMsg.YOU_CANNOT_SET_UP_A_BASE_HERE, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
                    return false;
                }

                SiegeEvent siegeEvent = activeChar.getEvent(SiegeEvent.class);
                if (siegeEvent == null) {
                    player.sendPacket(SystemMsg.YOU_CANNOT_SET_UP_A_BASE_HERE, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
                    return false;
                }

                boolean inZone = false;
                List<ZoneObject> zones = siegeEvent.getObjects(SiegeEvent.FLAG_ZONES);
                for (ZoneObject zone : zones)
                    if (player.isInZone(zone.getZone()))
                        inZone = true;

                if (!inZone) {
                    player.sendPacket(SystemMsg.YOU_CANNOT_SET_UP_A_BASE_HERE, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
                    return false;
                }

                SiegeClanObject siegeClan = siegeEvent.getSiegeClan(siegeEvent.getClass() == DominionSiegeEvent.class ? SiegeEvent.DEFENDERS : SiegeEvent.ATTACKERS, player.getClan());
                if (siegeClan == null) {
                    player.sendPacket(SystemMsg.YOU_CANNOT_SUMMON_THE_ENCAMPMENT_BECAUSE_YOU_ARE_NOT_A_MEMBER_OF_THE_SIEGE_CLAN_INVOLVED_IN_THE_CASTLE__FORTRESS__HIDEOUT_SIEGE, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
                    return false;
                }

                if (siegeClan.getFlag() != null) {
                    player.sendPacket(SystemMsg.AN_OUTPOST_OR_HEADQUARTERS_CANNOT_BE_BUILT_BECAUSE_ONE_ALREADY_EXISTS, new SystemMessage2(SystemMsg.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addSkillName(this));
                    return false;
                }
                break;
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void useSkill(Creature activeChar, List<Creature> targets) {
        Player player = (Player) activeChar;

        Clan clan = player.getClan();
        if (clan == null || !player.isClanLeader())
            return;

        SiegeEvent siegeEvent = activeChar.getEvent(SiegeEvent.class);
        if (siegeEvent == null)
            return;

        SiegeClanObject siegeClan = siegeEvent.getSiegeClan(siegeEvent.getClass() == DominionSiegeEvent.class ? SiegeEvent.DEFENDERS : SiegeEvent.ATTACKERS, clan);
        if (siegeClan == null)
            return;

        switch (_flagType) {
            case DESTROY:
                siegeClan.deleteFlag();
                break;
            default:
                if (siegeClan.getFlag() != null)
                    return;

                // 35062/36590
                SiegeFlagInstance flag = (SiegeFlagInstance) NpcHolder.getInstance().getTemplate(_flagType == FlagType.OUTPOST ? 36590 : 35062).getNewInstance();
                flag.setClan(siegeClan);
                flag.addEvent(siegeEvent);

                if (_flagType == FlagType.ADVANCED)
                    flag.addStatFunc(new FuncMul(Stats.MAX_HP, 0x50, flag, _advancedMult));

                flag.setCurrentHpMp(flag.getMaxHp(), flag.getMaxMp(), true);
                flag.setHeading(player.getHeading());

                // Ставим флаг перед чаром
                int x = (int) (player.getX() + 100 * Math.cos(player.headingToRadians(player.getHeading() - 32768)));
                int y = (int) (player.getY() + 100 * Math.sin(player.headingToRadians(player.getHeading() - 32768)));
                flag.spawnMe(GeoEngine.moveCheck(player.getX(), player.getY(), player.getZ(), x, y, player.getGeoIndex()));

                siegeClan.setFlag(flag);
                break;
        }
    }
}
