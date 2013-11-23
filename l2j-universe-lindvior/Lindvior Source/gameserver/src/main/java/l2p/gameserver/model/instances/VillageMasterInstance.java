/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.instances;

import l2p.gameserver.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.impl.CastleSiegeEvent;
import l2p.gameserver.model.entity.events.impl.SiegeEvent;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.entity.residence.Dominion;
import l2p.gameserver.model.entity.residence.Residence;
import l2p.gameserver.model.pledge.Alliance;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.SubUnit;
import l2p.gameserver.model.pledge.UnitMember;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeReceiveSubPledgeCreated;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeShowInfoUpdate;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeShowMemberListUpdate;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeStatusChanged;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.tables.ClanTable;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.SiegeUtils;
import l2p.gameserver.utils.Util;

import java.util.Collection;

public final class VillageMasterInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = 5239866251261965991L;

    public VillageMasterInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.startsWith("create_clan") && command.length() > 12) {
            String val = command.substring(12);
            createClan(player, val);
        } else if (command.startsWith("create_academy") && command.length() > 15) {
            String sub = command.substring(15, command.length());
            createSubPledge(player, sub, Clan.SUBUNIT_ACADEMY, 5, "");
        } else if (command.startsWith("create_royal") && command.length() > 15) {
            String[] sub = command.substring(13, command.length()).split(" ", 2);
            if (sub.length == 2)
                createSubPledge(player, sub[1], Clan.SUBUNIT_ROYAL1, 6, sub[0]);
        } else if (command.startsWith("create_knight") && command.length() > 16) {
            String[] sub = command.substring(14, command.length()).split(" ", 2);
            if (sub.length == 2)
                createSubPledge(player, sub[1], Clan.SUBUNIT_KNIGHT1, 7, sub[0]);
        } else if (command.startsWith("assign_subpl_leader") && command.length() > 22) {
            String[] sub = command.substring(20, command.length()).split(" ", 2);
            if (sub.length == 2)
                assignSubPledgeLeader(player, sub[1], sub[0]);
        } else if (command.startsWith("assign_new_clan_leader") && command.length() > 23) {
            String val = command.substring(23);
            setLeader(player, val);
        }
        if (command.startsWith("create_ally") && command.length() > 12) {
            String val = command.substring(12);
            createAlly(player, val);
        } else if (command.startsWith("dissolve_ally"))
            dissolveAlly(player);
        else if (command.startsWith("dissolve_clan"))
            dissolveClan(player);
        else if (command.startsWith("increase_clan_level"))
            levelUpClan(player);
        else if (command.startsWith("learn_clan_skills"))
            showClanSkillList(player);
        else if (command.startsWith("ShowCouponExchange")) {
            if (Functions.getItemCount(player, 8869) > 0 || Functions.getItemCount(player, 8870) > 0)
                command = "Multisell 800";
            else
                command = "Link villagemaster/reflect_weapon_master_noticket.htm";
            super.onBypassFeedback(player, command);
        } else
            super.onBypassFeedback(player, command);
    }

    @Override
    public String getHtmlPath(int npcId, int val, Player player) {
        String pom;
        if (val == 0)
            pom = "" + npcId;
        else
            pom = npcId + "-" + val;

        return "villagemaster/" + pom + ".htm";
    }

    // Private stuff
    public void createClan(Player player, String clanName) {
        if (player.getLevel() < 10) {
            player.sendPacket(Msg.YOU_ARE_NOT_QUALIFIED_TO_CREATE_A_CLAN);
            return;
        }

        if (player.getClanId() != 0) {
            player.sendPacket(Msg.YOU_HAVE_FAILED_TO_CREATE_A_CLAN);
            return;
        }

        if (!player.canCreateClan()) {
            // you can't create a new clan within 10 days
            player.sendPacket(Msg.YOU_MUST_WAIT_10_DAYS_BEFORE_CREATING_A_NEW_CLAN);
            return;
        }
        if (clanName.length() > 16) {
            player.sendPacket(Msg.CLAN_NAMES_LENGTH_IS_INCORRECT);
            return;
        }
        if (!Util.isMatchingRegexp(clanName, Config.CLAN_NAME_TEMPLATE)) {
            // clan name is not matching template
            player.sendPacket(Msg.CLAN_NAME_IS_INCORRECT);
            return;
        }

        Clan clan = ClanTable.getInstance().createClan(player, clanName);
        if (clan == null) {
            // clan name is already taken
            player.sendPacket(Msg.THIS_NAME_ALREADY_EXISTS);
            return;
        }

        // should be update packet only
        player.sendPacket(clan.listAll());
        player.sendPacket(new PledgeShowInfoUpdate(clan), Msg.CLAN_HAS_BEEN_CREATED);
        player.updatePledgeClass();
        player.broadcastCharInfo();
    }

    public void setLeader(Player leader, String newLeader) {
        if (!leader.isClanLeader()) {
            leader.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
            return;
        }

        if (leader.getEvent(SiegeEvent.class) != null) {
            leader.sendMessage(new CustomMessage("scripts.services.Rename.SiegeNow", leader));
            return;
        }

        Clan clan = leader.getClan();
        SubUnit mainUnit = clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
        UnitMember member = mainUnit.getUnitMember(newLeader);

        if (member == null) {
            //FIX ME зачем 2-ве мессаги(VISTALL)
            //leader.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.S1IsNotMemberOfTheClan", leader).addString(newLeader));
            showChatWindow(leader, "villagemaster/clan-20.htm");
            return;
        }

        if (member.getLeaderOf() != Clan.SUBUNIT_NONE) {
            leader.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.CannotAssignUnitLeader", leader));
            return;
        }

        setLeader(leader, clan, mainUnit, member);
    }

    public static void setLeader(Player player, Clan clan, SubUnit unit, UnitMember newLeader) {
        player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.ClanLeaderWillBeChangedFromS1ToS2", player).addString(clan.getLeaderName()).addString(newLeader.getName()));
        //TODO: В данной редакции смена лидера производится сразу же.
        // Надо подумать над реализацией смены кланлидера в запланированный день недели.

        /*if(clan.getLevel() >= CastleSiegeManager.getSiegeClanMinLevel())
          {
          if(clan.getLeader() != null)
          {
          L2Player oldLeaderPlayer = clan.getLeader().getPlayer();
          if(oldLeaderPlayer != null)
              SiegeUtils.removeSiegeSkills(oldLeaderPlayer);
          }
          L2Player newLeaderPlayer = newLeader.getPlayer();
          if(newLeaderPlayer != null)
          SiegeUtils.addSiegeSkills(newLeaderPlayer);
          }
          */
        unit.setLeader(newLeader, true);

        clan.broadcastClanStatus(true, true, false);
    }

    public void createSubPledge(Player player, String clanName, int pledgeType, int minClanLvl, String leaderName) {
        UnitMember subLeader = null;

        Clan clan = player.getClan();

        if (clan == null || !player.isClanLeader()) {
            player.sendPacket(Msg.YOU_HAVE_FAILED_TO_CREATE_A_CLAN);
            return;
        }

        if (!Util.isMatchingRegexp(clanName, Config.CLAN_NAME_TEMPLATE)) {
            player.sendPacket(Msg.CLAN_NAME_IS_INCORRECT);
            return;
        }

        Collection<SubUnit> subPledge = clan.getAllSubUnits();
        for (SubUnit element : subPledge)
            if (element.getName().equals(clanName)) {
                player.sendPacket(Msg.ANOTHER_MILITARY_UNIT_IS_ALREADY_USING_THAT_NAME_PLEASE_ENTER_A_DIFFERENT_NAME);
                return;
            }

        if (ClanTable.getInstance().getClanByName(clanName) != null) {
            player.sendPacket(Msg.ANOTHER_MILITARY_UNIT_IS_ALREADY_USING_THAT_NAME_PLEASE_ENTER_A_DIFFERENT_NAME);
            return;
        }

        if (clan.getLevel() < minClanLvl) {
            player.sendPacket(Msg.THE_CONDITIONS_NECESSARY_TO_CREATE_A_MILITARY_UNIT_HAVE_NOT_BEEN_MET);
            return;
        }

        SubUnit unit = clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);

        if (pledgeType != Clan.SUBUNIT_ACADEMY) {
            subLeader = unit.getUnitMember(leaderName);
            if (subLeader == null) {
                player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.PlayerCantBeAssignedAsSubUnitLeader", player));
                return;
            } else if (subLeader.getLeaderOf() != Clan.SUBUNIT_NONE) {
                player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.ItCantBeSubUnitLeader", player));
                return;
            }
        }

        pledgeType = clan.createSubPledge(player, pledgeType, subLeader, clanName);
        if (pledgeType == Clan.SUBUNIT_NONE)
            return;

        clan.broadcastToOnlineMembers(new PledgeReceiveSubPledgeCreated(clan.getSubUnit(pledgeType)));

        SystemMessage sm;
        if (pledgeType == Clan.SUBUNIT_ACADEMY) {
            sm = new SystemMessage(SystemMessage.CONGRATULATIONS_THE_S1S_CLAN_ACADEMY_HAS_BEEN_CREATED);
            sm.addString(player.getClan().getName());
        } else if (pledgeType >= Clan.SUBUNIT_KNIGHT1) {
            sm = new SystemMessage(SystemMessage.THE_KNIGHTS_OF_S1_HAVE_BEEN_CREATED);
            sm.addString(player.getClan().getName());
        } else if (pledgeType >= Clan.SUBUNIT_ROYAL1) {
            sm = new SystemMessage(SystemMessage.THE_ROYAL_GUARD_OF_S1_HAVE_BEEN_CREATED);
            sm.addString(player.getClan().getName());
        } else
            sm = Msg.CLAN_HAS_BEEN_CREATED;

        player.sendPacket(sm);

        if (subLeader != null) {
            clan.broadcastToOnlineMembers(new PledgeShowMemberListUpdate(subLeader));
            if (subLeader.isOnline()) {
                subLeader.getPlayer().updatePledgeClass();
                subLeader.getPlayer().broadcastCharInfo();
            }
        }
    }

    public void assignSubPledgeLeader(Player player, String clanName, String leaderName) {
        Clan clan = player.getClan();

        if (clan == null) {
            player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.ClanDoesntExist", player));
            return;
        }

        if (!player.isClanLeader()) {
            player.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
            return;
        }

        SubUnit targetUnit = null;
        for (SubUnit unit : clan.getAllSubUnits()) {
            if (unit.getType() == Clan.SUBUNIT_MAIN_CLAN || unit.getType() == Clan.SUBUNIT_ACADEMY)
                continue;
            if (unit.getName().equalsIgnoreCase(clanName))
                targetUnit = unit;

        }
        if (targetUnit == null) {
            player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.SubUnitNotFound", player));
            return;
        }
        SubUnit mainUnit = clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
        UnitMember subLeader = mainUnit.getUnitMember(leaderName);
        if (subLeader == null) {
            player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.PlayerCantBeAssignedAsSubUnitLeader", player));
            return;
        }

        if (subLeader.getLeaderOf() != Clan.SUBUNIT_NONE) {
            player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.ItCantBeSubUnitLeader", player));
            return;
        }

        targetUnit.setLeader(subLeader, true);
        clan.broadcastToOnlineMembers(new PledgeReceiveSubPledgeCreated(targetUnit));

        clan.broadcastToOnlineMembers(new PledgeShowMemberListUpdate(subLeader));
        if (subLeader.isOnline()) {
            subLeader.getPlayer().updatePledgeClass();
            subLeader.getPlayer().broadcastCharInfo();
        }

        player.sendMessage(new CustomMessage("l2p.gameserver.model.instances.L2VillageMasterInstance.NewSubUnitLeaderHasBeenAssigned", player));
    }

    private void dissolveClan(Player player) {
        if (player == null || player.getClan() == null)
            return;
        Clan clan = player.getClan();

        if (!player.isClanLeader()) {
            player.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
            return;
        }
        if (clan.getAllyId() != 0) {
            player.sendPacket(Msg.YOU_CANNOT_DISPERSE_THE_CLANS_IN_YOUR_ALLIANCE);
            return;
        }
        if (clan.isAtWar() > 0) {
            player.sendPacket(Msg.YOU_CANNOT_DISSOLVE_A_CLAN_WHILE_ENGAGED_IN_A_WAR);
            return;
        }
        if (clan.getCastle() != 0 || clan.getHasHideout() != 0 || clan.getHasFortress() != 0) {
            player.sendPacket(Msg.UNABLE_TO_DISPERSE_YOUR_CLAN_OWNS_ONE_OR_MORE_CASTLES_OR_HIDEOUTS);
            return;
        }

        for (Residence r : ResidenceHolder.getInstance().getResidences())
            if (r.getSiegeEvent().getSiegeClan(SiegeEvent.ATTACKERS, clan) != null || r.getSiegeEvent().getSiegeClan(SiegeEvent.DEFENDERS, clan) != null || r.getSiegeEvent().getSiegeClan(CastleSiegeEvent.DEFENDERS_WAITING, clan) != null) {
                player.sendPacket(SystemMsg.UNABLE_TO_DISSOLVE_YOUR_CLAN_HAS_REQUESTED_TO_PARTICIPATE_IN_A_CASTLE_SIEGE);
                return;
            }

        ClanTable.getInstance().dissolveClan(player);
    }

    public void levelUpClan(Player player) {
        Clan clan = player.getClan();
        if (clan == null)
            return;
        if (!player.isClanLeader()) {
            player.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
            return;
        }

        boolean increaseClanLevel = false;

        switch (clan.getLevel()) {
            case 0:
                // Upgrade to 1
                if (player.getSp() >= Config.CLAN_LVL_1_SP && player.getAdena() >= 650000) {
                    player.setSp(player.getSp() - Config.CLAN_LVL_1_SP);
                    player.reduceAdena(650000, true);
                    increaseClanLevel = true;
                }
                break;
            case 1:
                // Upgrade to 2
                if (player.getSp() >= Config.CLAN_LVL_2_SP && player.getAdena() >= 2500000) {
                    player.setSp(player.getSp() - Config.CLAN_LVL_2_SP);
                    player.reduceAdena(2500000, true);
                    increaseClanLevel = true;
                }
                break;
            case 2:
                // Upgrade to 3
                // itemid 1419 == Blood Mark
                if (player.getSp() >= Config.CLAN_LVL_3_SP && player.getInventory().destroyItemByItemId(1419, 1)) {
                    player.setSp(player.getSp() - Config.CLAN_LVL_3_SP);
                    increaseClanLevel = true;
                }
                break;
            case 3:
                // Upgrade to 4
                // itemid 3874 == Alliance Manifesto
                if (player.getSp() >= Config.CLAN_LVL_4_SP && player.getInventory().destroyItemByItemId(3874, 1)) {
                    player.setSp(player.getSp() - Config.CLAN_LVL_4_SP);
                    increaseClanLevel = true;
                }
                break;
            case 4:
                // Upgrade to 5
                // itemid 3870 == Seal of Aspiration
                if (player.getSp() >= Config.CLAN_LVL_5_SP && player.getInventory().destroyItemByItemId(3870, 1)) {
                    player.setSp(player.getSp() - Config.CLAN_LVL_5_SP);
                    increaseClanLevel = true;
                }
                break;
            case 5:
                // Upgrade to 6
                if (clan.getReputationScore() >= Config.CLAN_LVL_6_REP && clan.getAllSize() >= Config.CLAN_LVL_6_SIZE) {
                    clan.incReputation(-Config.CLAN_LVL_6_REP, false, "LvlUpClan");
                    increaseClanLevel = true;
                }
                break;
            case 6:
                // Upgrade to 7
                if (clan.getReputationScore() >= Config.CLAN_LVL_7_REP && clan.getAllSize() >= Config.CLAN_LVL_7_SIZE) {
                    clan.incReputation(-Config.CLAN_LVL_7_REP, false, "LvlUpClan");
                    increaseClanLevel = true;
                }
                break;
            case 7:
                // Upgrade to 8
                if (clan.getReputationScore() >= Config.CLAN_LVL_8_REP && clan.getAllSize() >= Config.CLAN_LVL_8_SIZE) {
                    clan.incReputation(-Config.CLAN_LVL_8_REP, false, "LvlUpClan");
                    increaseClanLevel = true;
                }
                break;
            case 8:
                // Upgrade to 9
                // itemId 9910 == Blood Oath
                if (clan.getReputationScore() >= Config.CLAN_LVL_9_REP && clan.getAllSize() >= Config.CLAN_LVL_9_SIZE)
                    if (player.getInventory().destroyItemByItemId(9910, 150L)) {
                        clan.incReputation(-Config.CLAN_LVL_9_REP, false, "LvlUpClan");
                        increaseClanLevel = true;
                    }
                break;
            case 9:
                // Upgrade to 10
                // itemId 9911 == Blood Alliance
                if (clan.getReputationScore() >= Config.CLAN_LVL_10_REP && clan.getAllSize() >= Config.CLAN_LVL_10_SIZE)
                    if (player.getInventory().destroyItemByItemId(9911, 5)) {
                        clan.incReputation(-Config.CLAN_LVL_10_REP, false, "LvlUpClan");
                        increaseClanLevel = true;
                    }
                break;
            case 10:
                // Upgrade to 11
                // itemId  34996  Плащ Ослепительного Света
                if (clan.getReputationScore() >= Config.CLAN_LVL_11_REP && clan.getAllSize() >= Config.CLAN_LVL_11_SIZE && clan.getCastle() > 0) {
                    Castle castle = ResidenceHolder.getInstance().getResidence(clan.getCastle());
                    Dominion dominion = castle.getDominion();
                    if (dominion.getLordObjectId() == player.getObjectId() && player.getInventory().destroyItemByItemId(34996, 1)) {
                        clan.incReputation(-Config.CLAN_LVL_11_REP, false, "LvlUpClan");
                        increaseClanLevel = true;
                    }
                }
                break;
        }

        if (increaseClanLevel) {
            clan.setLevel(clan.getLevel() + 1);
            clan.updateClanInDB();

            player.broadcastCharInfo();

            doCast(SkillTable.getInstance().getInfo(5103, 1), player, true);

            if (clan.getLevel() >= 4)
                SiegeUtils.addSiegeSkills(player);

            if (clan.getLevel() == 5)
                player.sendPacket(Msg.NOW_THAT_YOUR_CLAN_LEVEL_IS_ABOVE_LEVEL_5_IT_CAN_ACCUMULATE_CLAN_REPUTATION_POINTS);

            // notify all the members about it
            PledgeShowInfoUpdate pu = new PledgeShowInfoUpdate(clan);
            PledgeStatusChanged ps = new PledgeStatusChanged(clan);
            for (UnitMember mbr : clan)
                if (mbr.isOnline()) {
                    mbr.getPlayer().updatePledgeClass();
                    mbr.getPlayer().sendPacket(Msg.CLANS_SKILL_LEVEL_HAS_INCREASED, pu, ps);
                    mbr.getPlayer().broadcastCharInfo();
                }
        } else
            player.sendPacket(Msg.CLAN_HAS_FAILED_TO_INCREASE_SKILL_LEVEL);
    }

    public void createAlly(Player player, String allyName) {
        // D5 You may not ally with clan you are battle with.
        // D6 Only the clan leader may apply for withdraw from alliance.
        // DD No response. Invitation to join an
        // D7 Alliance leaders cannot withdraw.
        // D9 Different Alliance
        // EB alliance information
        // Ec alliance name $s1
        // ee alliance leader: $s2 of $s1
        // ef affilated clans: total $s1 clan(s)
        // f6 you have already joined an alliance
        // f9 you cannot new alliance 10 days
        // fd cannot accept. clan ally is register as enemy during siege battle.
        // fe you have invited someone to your alliance.
        // 100 do you wish to withdraw from the alliance
        // 102 enter the name of the clan you wish to expel.
        // 202 do you realy wish to dissolve the alliance
        // 502 you have accepted alliance
        // 602 you have failed to invite a clan into the alliance
        // 702 you have withdraw

        if (!player.isClanLeader()) {
            player.sendPacket(Msg.ONLY_CLAN_LEADERS_MAY_CREATE_ALLIANCES);
            return;
        }
        if (player.getClan().getAllyId() != 0) {
            player.sendPacket(Msg.YOU_ALREADY_BELONG_TO_ANOTHER_ALLIANCE);
            return;
        }
        if (allyName.length() > 16) {
            player.sendPacket(Msg.INCORRECT_LENGTH_FOR_AN_ALLIANCE_NAME);
            return;
        }
        if (!Util.isMatchingRegexp(allyName, Config.ALLY_NAME_TEMPLATE)) {
            player.sendPacket(Msg.INCORRECT_ALLIANCE_NAME);
            return;
        }
        if (player.getClan().getLevel() < 5) {
            player.sendPacket(Msg.TO_CREATE_AN_ALLIANCE_YOUR_CLAN_MUST_BE_LEVEL_5_OR_HIGHER);
            return;
        }
        if (ClanTable.getInstance().getAllyByName(allyName) != null) {
            player.sendPacket(Msg.THIS_ALLIANCE_NAME_ALREADY_EXISTS);
            return;
        }
        if (!player.getClan().canCreateAlly()) {
            player.sendPacket(Msg.YOU_CANNOT_CREATE_A_NEW_ALLIANCE_WITHIN_1_DAY_AFTER_DISSOLUTION);
            return;
        }

        Alliance alliance = ClanTable.getInstance().createAlliance(player, allyName);
        if (alliance == null)
            return;

        player.broadcastCharInfo();
        player.sendMessage("Alliance " + allyName + " has been created.");
    }

    private void dissolveAlly(Player player) {
        if (player == null || player.getAlliance() == null)
            return;

        if (!player.isAllyLeader()) {
            player.sendPacket(Msg.FEATURE_AVAILABLE_TO_ALLIANCE_LEADERS_ONLY);
            return;
        }

        if (player.getAlliance().getMembersCount() > 1) {
            player.sendPacket(Msg.YOU_HAVE_FAILED_TO_DISSOLVE_THE_ALLIANCE);
            return;
        }

        ClanTable.getInstance().dissolveAlly(player);
    }
}
