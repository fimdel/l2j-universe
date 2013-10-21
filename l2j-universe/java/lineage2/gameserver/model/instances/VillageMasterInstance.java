/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.instances;

import java.util.Collection;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.PledgeReceiveSubPledgeCreated;
import lineage2.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import lineage2.gameserver.network.serverpackets.PledgeStatusChanged;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.CertificationFunctions;
import lineage2.gameserver.utils.SiegeUtils;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class VillageMasterInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is 5239866251261965991)
	 */
	private static final long serialVersionUID = 5239866251261965991L;
	
	/**
	 * Constructor for VillageMasterInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public VillageMasterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.startsWith("create_clan") && (command.length() > 12))
		{
			String val = command.substring(12);
			createClan(player, val);
		}
		else if (command.startsWith("create_academy") && (command.length() > 15))
		{
			String sub = command.substring(15, command.length());
			createSubPledge(player, sub, Clan.SUBUNIT_ACADEMY, 5, "");
		}
		else if (command.startsWith("create_royal") && (command.length() > 15))
		{
			String[] sub = command.substring(13, command.length()).split(" ", 2);
			if (sub.length == 2)
			{
				createSubPledge(player, sub[1], Clan.SUBUNIT_ROYAL1, 6, sub[0]);
			}
		}
		else if (command.startsWith("create_knight") && (command.length() > 16))
		{
			String[] sub = command.substring(14, command.length()).split(" ", 2);
			if (sub.length == 2)
			{
				createSubPledge(player, sub[1], Clan.SUBUNIT_KNIGHT1, 7, sub[0]);
			}
		}
		else if (command.startsWith("assign_subpl_leader") && (command.length() > 22))
		{
			String[] sub = command.substring(20, command.length()).split(" ", 2);
			if (sub.length == 2)
			{
				assignSubPledgeLeader(player, sub[1], sub[0]);
			}
		}
		else if (command.startsWith("assign_new_clan_leader") && (command.length() > 23))
		{
			String val = command.substring(23);
			setLeader(player, val);
		}
		if (command.startsWith("create_ally") && (command.length() > 12))
		{
			String val = command.substring(12);
			createAlly(player, val);
		}
		else if (command.startsWith("dissolve_ally"))
		{
			dissolveAlly(player);
		}
		else if (command.startsWith("dissolve_clan"))
		{
			dissolveClan(player);
		}
		else if (command.startsWith("increase_clan_level"))
		{
			levelUpClan(player);
		}
		else if (command.startsWith("learn_clan_skills"))
		{
			showClanSkillList(player);
		}
		else if (command.startsWith("ShowCouponExchange"))
		{
			if ((Functions.getItemCount(player, 8869) > 0) || (Functions.getItemCount(player, 8870) > 0))
			{
				command = "Multisell 800";
			}
			else
			{
				command = "Link villagemaster/reflect_weapon_master_noticket.htm";
			}
			super.onBypassFeedback(player, command);
		}
		else if (command.equalsIgnoreCase("CertificationList"))
		{
			CertificationFunctions.showCertificationList(this, player, 65);
		}/*
		else if (command.equalsIgnoreCase("GetCertification65"))
		{
			CertificationFunctions.getCertification65(this, player);
		}
		else if (command.equalsIgnoreCase("GetCertification70"))
		{
			CertificationFunctions.getCertification70(this, player);
		}
		else if (command.equalsIgnoreCase("GetCertification80"))
		{
			CertificationFunctions.getCertification80(this, player);
		}*/
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String pom;
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		return "villagemaster/" + pom + ".htm";
	}
	
	/**
	 * Method createClan.
	 * @param player Player
	 * @param clanName String
	 */
	public void createClan(Player player, String clanName)
	{
		if (player.getLevel() < 10)
		{
			player.sendPacket(Msg.YOU_ARE_NOT_QUALIFIED_TO_CREATE_A_CLAN);
			return;
		}
		if (player.getClanId() != 0)
		{
			player.sendPacket(Msg.YOU_HAVE_FAILED_TO_CREATE_A_CLAN);
			return;
		}
		if (!player.canCreateClan())
		{
			player.sendPacket(Msg.YOU_MUST_WAIT_10_DAYS_BEFORE_CREATING_A_NEW_CLAN);
			return;
		}
		if (clanName.length() > 16)
		{
			player.sendPacket(Msg.CLAN_NAMES_LENGTH_IS_INCORRECT);
			return;
		}
		if (!Util.isMatchingRegexp(clanName, Config.CLAN_NAME_TEMPLATE))
		{
			player.sendPacket(Msg.CLAN_NAME_IS_INCORRECT);
			return;
		}
		Clan clan = ClanTable.getInstance().createClan(player, clanName);
		if (clan == null)
		{
			player.sendPacket(Msg.THIS_NAME_ALREADY_EXISTS);
			return;
		}
		player.sendPacket(clan.listAll());
		player.sendPacket(new PledgeShowInfoUpdate(clan), Msg.CLAN_HAS_BEEN_CREATED);
		player.updatePledgeClass();
		player.broadcastCharInfo();
	}
	
	/**
	 * Method setLeader.
	 * @param leader Player
	 * @param newLeader String
	 */
	public void setLeader(Player leader, String newLeader)
	{
		if (!leader.isClanLeader())
		{
			leader.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
			return;
		}
		if (leader.getEvent(SiegeEvent.class) != null)
		{
			leader.sendMessage(new CustomMessage("scripts.services.Rename.SiegeNow", leader));
			return;
		}
		Clan clan = leader.getClan();
		SubUnit mainUnit = clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
		UnitMember member = mainUnit.getUnitMember(newLeader);
		if (member == null)
		{
			showChatWindow(leader, "villagemaster/clan-20.htm");
			return;
		}
		if (member.getLeaderOf() != Clan.SUBUNIT_NONE)
		{
			leader.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.CannotAssignUnitLeader", leader));
			return;
		}
		setLeader(leader, clan, mainUnit, member);
	}
	
	/**
	 * Method setLeader.
	 * @param player Player
	 * @param clan Clan
	 * @param unit SubUnit
	 * @param newLeader UnitMember
	 */
	public static void setLeader(Player player, Clan clan, SubUnit unit, UnitMember newLeader)
	{
		player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.ClanLeaderWillBeChangedFromS1ToS2", player).addString(clan.getLeaderName()).addString(newLeader.getName()));
		unit.setLeader(newLeader, true);
		clan.broadcastClanStatus(true, true, false);
	}
	
	/**
	 * Method createSubPledge.
	 * @param player Player
	 * @param clanName String
	 * @param pledgeType int
	 * @param minClanLvl int
	 * @param leaderName String
	 */
	public void createSubPledge(Player player, String clanName, int pledgeType, int minClanLvl, String leaderName)
	{
		UnitMember subLeader = null;
		Clan clan = player.getClan();
		if ((clan == null) || !player.isClanLeader())
		{
			player.sendPacket(Msg.YOU_HAVE_FAILED_TO_CREATE_A_CLAN);
			return;
		}
		if (!Util.isMatchingRegexp(clanName, Config.CLAN_NAME_TEMPLATE))
		{
			player.sendPacket(Msg.CLAN_NAME_IS_INCORRECT);
			return;
		}
		Collection<SubUnit> subPledge = clan.getAllSubUnits();
		for (SubUnit element : subPledge)
		{
			if (element.getName().equals(clanName))
			{
				player.sendPacket(Msg.ANOTHER_MILITARY_UNIT_IS_ALREADY_USING_THAT_NAME_PLEASE_ENTER_A_DIFFERENT_NAME);
				return;
			}
		}
		if (ClanTable.getInstance().getClanByName(clanName) != null)
		{
			player.sendPacket(Msg.ANOTHER_MILITARY_UNIT_IS_ALREADY_USING_THAT_NAME_PLEASE_ENTER_A_DIFFERENT_NAME);
			return;
		}
		if (clan.getLevel() < minClanLvl)
		{
			player.sendPacket(Msg.THE_CONDITIONS_NECESSARY_TO_CREATE_A_MILITARY_UNIT_HAVE_NOT_BEEN_MET);
			return;
		}
		SubUnit unit = clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
		if (pledgeType != Clan.SUBUNIT_ACADEMY)
		{
			subLeader = unit.getUnitMember(leaderName);
			if (subLeader == null)
			{
				player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.PlayerCantBeAssignedAsSubUnitLeader", player));
				return;
			}
			else if (subLeader.getLeaderOf() != Clan.SUBUNIT_NONE)
			{
				player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.ItCantBeSubUnitLeader", player));
				return;
			}
		}
		pledgeType = clan.createSubPledge(player, pledgeType, subLeader, clanName);
		if (pledgeType == Clan.SUBUNIT_NONE)
		{
			return;
		}
		clan.broadcastToOnlineMembers(new PledgeReceiveSubPledgeCreated(clan.getSubUnit(pledgeType)));
		SystemMessage sm;
		if (pledgeType == Clan.SUBUNIT_ACADEMY)
		{
			sm = new SystemMessage(SystemMessage.CONGRATULATIONS_THE_S1S_CLAN_ACADEMY_HAS_BEEN_CREATED);
			sm.addString(player.getClan().getName());
		}
		else if (pledgeType >= Clan.SUBUNIT_KNIGHT1)
		{
			sm = new SystemMessage(SystemMessage.THE_KNIGHTS_OF_S1_HAVE_BEEN_CREATED);
			sm.addString(player.getClan().getName());
		}
		else if (pledgeType >= Clan.SUBUNIT_ROYAL1)
		{
			sm = new SystemMessage(SystemMessage.THE_ROYAL_GUARD_OF_S1_HAVE_BEEN_CREATED);
			sm.addString(player.getClan().getName());
		}
		else
		{
			sm = Msg.CLAN_HAS_BEEN_CREATED;
		}
		player.sendPacket(sm);
		if (subLeader != null)
		{
			clan.broadcastToOnlineMembers(new PledgeShowMemberListUpdate(subLeader));
			if (subLeader.isOnline())
			{
				subLeader.getPlayer().updatePledgeClass();
				subLeader.getPlayer().broadcastCharInfo();
			}
		}
	}
	
	/**
	 * Method assignSubPledgeLeader.
	 * @param player Player
	 * @param clanName String
	 * @param leaderName String
	 */
	public void assignSubPledgeLeader(Player player, String clanName, String leaderName)
	{
		Clan clan = player.getClan();
		if (clan == null)
		{
			player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.ClanDoesntExist", player));
			return;
		}
		if (!player.isClanLeader())
		{
			player.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
			return;
		}
		SubUnit targetUnit = null;
		for (SubUnit unit : clan.getAllSubUnits())
		{
			if ((unit.getType() == Clan.SUBUNIT_MAIN_CLAN) || (unit.getType() == Clan.SUBUNIT_ACADEMY))
			{
				continue;
			}
			if (unit.getName().equalsIgnoreCase(clanName))
			{
				targetUnit = unit;
			}
		}
		if (targetUnit == null)
		{
			player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.SubUnitNotFound", player));
			return;
		}
		SubUnit mainUnit = clan.getSubUnit(Clan.SUBUNIT_MAIN_CLAN);
		UnitMember subLeader = mainUnit.getUnitMember(leaderName);
		if (subLeader == null)
		{
			player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.PlayerCantBeAssignedAsSubUnitLeader", player));
			return;
		}
		if (subLeader.getLeaderOf() != Clan.SUBUNIT_NONE)
		{
			player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.ItCantBeSubUnitLeader", player));
			return;
		}
		targetUnit.setLeader(subLeader, true);
		clan.broadcastToOnlineMembers(new PledgeReceiveSubPledgeCreated(targetUnit));
		clan.broadcastToOnlineMembers(new PledgeShowMemberListUpdate(subLeader));
		if (subLeader.isOnline())
		{
			subLeader.getPlayer().updatePledgeClass();
			subLeader.getPlayer().broadcastCharInfo();
		}
		player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2VillageMasterInstance.NewSubUnitLeaderHasBeenAssigned", player));
	}
	
	/**
	 * Method dissolveClan.
	 * @param player Player
	 */
	private void dissolveClan(Player player)
	{
		if ((player == null) || (player.getClan() == null))
		{
			return;
		}
		Clan clan = player.getClan();
		if (!player.isClanLeader())
		{
			player.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
			return;
		}
		if (clan.getAllyId() != 0)
		{
			player.sendPacket(Msg.YOU_CANNOT_DISPERSE_THE_CLANS_IN_YOUR_ALLIANCE);
			return;
		}
		if (clan.isAtWar() > 0)
		{
			player.sendPacket(Msg.YOU_CANNOT_DISSOLVE_A_CLAN_WHILE_ENGAGED_IN_A_WAR);
			return;
		}
		if ((clan.getCastle() != 0) || (clan.getHasHideout() != 0) || (clan.getHasFortress() != 0))
		{
			player.sendPacket(Msg.UNABLE_TO_DISPERSE_YOUR_CLAN_OWNS_ONE_OR_MORE_CASTLES_OR_HIDEOUTS);
			return;
		}
		for (Residence r : ResidenceHolder.getInstance().getResidences())
		{
			if ((r.getSiegeEvent().getSiegeClan(SiegeEvent.ATTACKERS, clan) != null) || (r.getSiegeEvent().getSiegeClan(SiegeEvent.DEFENDERS, clan) != null) || (r.getSiegeEvent().getSiegeClan(CastleSiegeEvent.DEFENDERS_WAITING, clan) != null))
			{
				player.sendPacket(SystemMsg.UNABLE_TO_DISSOLVE_YOUR_CLAN_HAS_REQUESTED_TO_PARTICIPATE_IN_A_CASTLE_SIEGE);
				return;
			}
		}
		ClanTable.getInstance().dissolveClan(player);
	}
	
	/**
	 * Method levelUpClan.
	 * @param player Player
	 */
	public void levelUpClan(Player player)
	{
		Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		if (!player.isClanLeader())
		{
			player.sendPacket(Msg.ONLY_THE_CLAN_LEADER_IS_ENABLED);
			return;
		}
		boolean increaseClanLevel = false;
		switch (clan.getLevel())
		{
			case 0:
				if ((player.getSp() >= 20000) && (player.getAdena() >= 650000))
				{
					player.setSp(player.getSp() - 20000);
					player.reduceAdena(650000, true);
					increaseClanLevel = true;
				}
				break;
			case 1:
				if ((player.getSp() >= 100000) && (player.getAdena() >= 2500000))
				{
					player.setSp(player.getSp() - 100000);
					player.reduceAdena(2500000, true);
					increaseClanLevel = true;
				}
				break;
			case 2:
				if ((player.getSp() >= 350000) && player.getInventory().destroyItemByItemId(1419, 1))
				{
					player.setSp(player.getSp() - 350000);
					increaseClanLevel = true;
				}
				break;
			case 3:
				if ((player.getSp() >= 1000000) && player.getInventory().destroyItemByItemId(3874, 1))
				{
					player.setSp(player.getSp() - 1000000);
					increaseClanLevel = true;
				}
				break;
			case 4:
				if ((player.getSp() >= 2500000) && player.getInventory().destroyItemByItemId(3870, 1))
				{
					player.setSp(player.getSp() - 2500000);
					increaseClanLevel = true;
				}
				break;
			case 5:
				if ((clan.getReputationScore() >= Config.ALT_CLAN_REP_COUNT_6LVL) && (clan.getAllSize() >= Config.ALT_CLAN_PLAYER_COUNT_6LVL))
				{
					clan.incReputation(-Config.ALT_CLAN_REP_COUNT_6LVL, false, "LvlUpClan");
					increaseClanLevel = true;
				}
				break;
			case 6:
				if ((clan.getReputationScore() >= Config.ALT_CLAN_REP_COUNT_7LVL) && (clan.getAllSize() >= Config.ALT_CLAN_PLAYER_COUNT_7LVL))
				{
					clan.incReputation(-Config.ALT_CLAN_REP_COUNT_7LVL, false, "LvlUpClan");
					increaseClanLevel = true;
				}
				break;
			case 7:
				if ((clan.getReputationScore() >= Config.ALT_CLAN_REP_COUNT_8LVL) && (clan.getAllSize() >= Config.ALT_CLAN_PLAYER_COUNT_8LVL))
				{
					clan.incReputation(-Config.ALT_CLAN_REP_COUNT_8LVL, false, "LvlUpClan");
					increaseClanLevel = true;
				}
				break;
			case 8:
				if ((clan.getReputationScore() >= Config.ALT_CLAN_REP_COUNT_9LVL) && (clan.getAllSize() >= Config.ALT_CLAN_PLAYER_COUNT_9LVL))
				{
					if (player.getInventory().destroyItemByItemId(9910, 150L))
					{
						clan.incReputation(-Config.ALT_CLAN_REP_COUNT_9LVL, false, "LvlUpClan");
						increaseClanLevel = true;
					}
				}
				break;
			case 9:
				if ((clan.getReputationScore() >= Config.ALT_CLAN_REP_COUNT_10LVL) && (clan.getAllSize() >= Config.ALT_CLAN_PLAYER_COUNT_10LVL))
				{
					if (player.getInventory().destroyItemByItemId(9911, 5))
					{
						clan.incReputation(-Config.ALT_CLAN_REP_COUNT_10LVL, false, "LvlUpClan");
						increaseClanLevel = true;
					}
				}
				break;
			case 10:
				if ((clan.getReputationScore() >= Config.ALT_CLAN_REP_COUNT_11LVL) && (clan.getAllSize() >= Config.ALT_CLAN_PLAYER_COUNT_11LVL) && (clan.getCastle() > 0))
				{
					//Radian Cloak of Light
					if (player.getInventory().destroyItemByItemId(34996, 1))
					{
						clan.incReputation(-Config.ALT_CLAN_REP_COUNT_11LVL, false, "LvlUpClan");
						increaseClanLevel = true;
					}
				}
				break;
		}
		if (increaseClanLevel)
		{
			clan.setLevel(clan.getLevel() + 1);
			clan.updateClanInDB();
			player.broadcastCharInfo();
			doCast(SkillTable.getInstance().getInfo(5103, 1), player, true);
			if (clan.getLevel() >= 4)
			{
				SiegeUtils.addSiegeSkills(player);
			}
			if (clan.getLevel() == 5)
			{
				player.sendPacket(Msg.NOW_THAT_YOUR_CLAN_LEVEL_IS_ABOVE_LEVEL_5_IT_CAN_ACCUMULATE_CLAN_REPUTATION_POINTS);
			}
			PledgeShowInfoUpdate pu = new PledgeShowInfoUpdate(clan);
			PledgeStatusChanged ps = new PledgeStatusChanged(clan);
			for (UnitMember mbr : clan)
			{
				if (mbr.isOnline())
				{
					mbr.getPlayer().updatePledgeClass();
					mbr.getPlayer().sendPacket(Msg.CLANS_SKILL_LEVEL_HAS_INCREASED, pu, ps);
					mbr.getPlayer().broadcastCharInfo();
				}
			}
		}
		else
		{
			player.sendPacket(Msg.CLAN_HAS_FAILED_TO_INCREASE_SKILL_LEVEL);
		}
	}
	
	/**
	 * Method createAlly.
	 * @param player Player
	 * @param allyName String
	 */
	public void createAlly(Player player, String allyName)
	{
		if (!player.isClanLeader())
		{
			player.sendPacket(Msg.ONLY_CLAN_LEADERS_MAY_CREATE_ALLIANCES);
			return;
		}
		if (player.getClan().getAllyId() != 0)
		{
			player.sendPacket(Msg.YOU_ALREADY_BELONG_TO_ANOTHER_ALLIANCE);
			return;
		}
		if (allyName.length() > 16)
		{
			player.sendPacket(Msg.INCORRECT_LENGTH_FOR_AN_ALLIANCE_NAME);
			return;
		}
		if (!Util.isMatchingRegexp(allyName, Config.ALLY_NAME_TEMPLATE))
		{
			player.sendPacket(Msg.INCORRECT_ALLIANCE_NAME);
			return;
		}
		if (player.getClan().getLevel() < 5)
		{
			player.sendPacket(Msg.TO_CREATE_AN_ALLIANCE_YOUR_CLAN_MUST_BE_LEVEL_5_OR_HIGHER);
			return;
		}
		if (ClanTable.getInstance().getAllyByName(allyName) != null)
		{
			player.sendPacket(Msg.THIS_ALLIANCE_NAME_ALREADY_EXISTS);
			return;
		}
		if (!player.getClan().canCreateAlly())
		{
			player.sendPacket(Msg.YOU_CANNOT_CREATE_A_NEW_ALLIANCE_WITHIN_1_DAY_AFTER_DISSOLUTION);
			return;
		}
		Alliance alliance = ClanTable.getInstance().createAlliance(player, allyName);
		if (alliance == null)
		{
			return;
		}
		player.broadcastCharInfo();
		player.sendMessage("Alliance " + allyName + " has been created.");
	}
	
	/**
	 * Method dissolveAlly.
	 * @param player Player
	 */
	private void dissolveAlly(Player player)
	{
		if ((player == null) || (player.getAlliance() == null))
		{
			return;
		}
		if (!player.isAllyLeader())
		{
			player.sendPacket(Msg.FEATURE_AVAILABLE_TO_ALLIANCE_LEADERS_ONLY);
			return;
		}
		if (player.getAlliance().getMembersCount() > 1)
		{
			player.sendPacket(Msg.YOU_HAVE_FAILED_TO_DISSOLVE_THE_ALLIANCE);
			return;
		}
		ClanTable.getInstance().dissolveAlly(player);
	}
}
