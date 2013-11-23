/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.admincommands.impl;

import l2p.gameserver.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.admincommands.IAdminCommandHandler;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.VillageMasterInstance;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.SubUnit;
import l2p.gameserver.model.pledge.UnitMember;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeShowInfoUpdate;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeStatusChanged;
import l2p.gameserver.tables.ClanTable;
import l2p.gameserver.utils.Util;

import java.util.StringTokenizer;

/**
 * Pledge Manipulation //pledge <create|setlevel|resetcreate|resetwait|addrep|setleader>
 */
public class AdminPledge implements IAdminCommandHandler {
    private static enum Commands {
        admin_pledge
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, Player activeChar) {
        @SuppressWarnings("unused")
        Commands command = (Commands) comm;

        if (activeChar.getPlayerAccess() == null || !activeChar.getPlayerAccess().CanEditPledge || activeChar.getTarget() == null || !activeChar.getTarget().isPlayer())
            return false;

        Player target = (Player) activeChar.getTarget();

        if (fullString.startsWith("admin_pledge")) {
            StringTokenizer st = new StringTokenizer(fullString);
            st.nextToken();

            String action = st.nextToken(); // setlevel|resetcreate|resetwait|addrep

            if (action.equals("create"))
                try {
                    if (target == null) {
                        activeChar.sendPacket(Msg.INVALID_TARGET);
                        return false;
                    }
                    if (target.getPlayer().getLevel() < 10) {
                        activeChar.sendPacket(Msg.YOU_ARE_NOT_QUALIFIED_TO_CREATE_A_CLAN);
                        return false;
                    }
                    String pledgeName = st.nextToken();
                    if (pledgeName.length() > 16) {
                        activeChar.sendPacket(Msg.CLAN_NAMES_LENGTH_IS_INCORRECT);
                        return false;
                    }
                    if (!Util.isMatchingRegexp(pledgeName, Config.CLAN_NAME_TEMPLATE)) {
                        // clan name is not matching template
                        activeChar.sendPacket(Msg.CLAN_NAME_IS_INCORRECT);
                        return false;
                    }

                    Clan clan = ClanTable.getInstance().createClan(target, pledgeName);
                    if (clan != null) {
                        target.sendPacket(clan.listAll());
                        target.sendPacket(new PledgeShowInfoUpdate(clan), Msg.CLAN_HAS_BEEN_CREATED);
                        target.updatePledgeClass();
                        target.sendUserInfo(true);
                        return true;
                    } else {
                        activeChar.sendPacket(Msg.THIS_NAME_ALREADY_EXISTS);
                        return false;
                    }
                } catch (Exception e) {
                }
            else if (action.equals("setlevel")) {
                if (target.getClan() == null) {
                    activeChar.sendPacket(Msg.INVALID_TARGET);
                    return false;
                }

                try {
                    int level = Integer.parseInt(st.nextToken());
                    Clan clan = target.getClan();

                    activeChar.sendMessage("You set level " + level + " for clan " + clan.getName());
                    clan.setLevel(level);
                    clan.updateClanInDB();

                    /*	if(level < CastleSiegeManager.getSiegeClanMinLevel())
                                 SiegeUtils.removeSiegeSkills(target);
                             else
                                 SiegeUtils.addSiegeSkills(target);   */

                    if (level == 5)
                        target.sendPacket(Msg.NOW_THAT_YOUR_CLAN_LEVEL_IS_ABOVE_LEVEL_5_IT_CAN_ACCUMULATE_CLAN_REPUTATION_POINTS);

                    PledgeShowInfoUpdate pu = new PledgeShowInfoUpdate(clan);
                    PledgeStatusChanged ps = new PledgeStatusChanged(clan);

                    for (Player member : clan.getOnlineMembers(0)) {
                        member.updatePledgeClass();
                        member.sendPacket(Msg.CLANS_SKILL_LEVEL_HAS_INCREASED, pu, ps);
                        member.broadcastUserInfo(true);
                    }

                    return true;
                } catch (Exception e) {
                }
            } else if (action.equals("resetcreate")) {
                if (target.getClan() == null) {
                    activeChar.sendPacket(Msg.INVALID_TARGET);
                    return false;
                }
                target.getClan().setExpelledMemberTime(0);
                activeChar.sendMessage("The penalty for creating a clan has been lifted for " + target.getName());
            } else if (action.equals("resetwait")) {
                target.setLeaveClanTime(0);
                activeChar.sendMessage("The penalty for leaving a clan has been lifted for " + target.getName());
            } else if (action.equals("addrep"))
                try {
                    int rep = Integer.parseInt(st.nextToken());

                    if (target.getClan() == null || target.getClan().getLevel() < 5) {
                        activeChar.sendPacket(Msg.INVALID_TARGET);
                        return false;
                    }
                    target.getClan().incReputation(rep, false, "admin_manual");
                    activeChar.sendMessage("Added " + rep + " clan points to clan " + target.getClan().getName() + ".");
                } catch (NumberFormatException nfe) {
                    activeChar.sendMessage("Please specify a number of clan points to add.");
                }
            else if (action.equals("setleader")) {
                Clan clan = target.getClan();
                if (target.getClan() == null) {
                    activeChar.sendPacket(Msg.INVALID_TARGET);
                    return false;
                }
                String newLeaderName = null;
                if (st.hasMoreTokens())
                    newLeaderName = st.nextToken();
                else
                    newLeaderName = target.getName();
                SubUnit mainUnit = clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
                UnitMember newLeader = mainUnit.getUnitMember(newLeaderName);
                if (newLeader == null) {
                    activeChar.sendPacket(Msg.INVALID_TARGET);
                    return false;
                }
                VillageMasterInstance.setLeader(activeChar, clan, mainUnit, newLeader);
            }
        }

        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enum[] getAdminCommandEnum() {
        return Commands.values();
    }
}
