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
package lineage2.gameserver.model.entity.events.impl;

import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanHallSiegeEvent extends SiegeEvent<ClanHall, SiegeClanObject>
{
	/**
	 * Field BOSS. (value is ""boss"")
	 */
	public static final String BOSS = "boss";
	
	/**
	 * Constructor for ClanHallSiegeEvent.
	 * @param set MultiValueSet<String>
	 */
	public ClanHallSiegeEvent(MultiValueSet<String> set)
	{
		super(set);
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		_oldOwner = getResidence().getOwner();
		if (_oldOwner != null)
		{
			getResidence().changeOwner(null);
			addObject(ATTACKERS, new SiegeClanObject(ATTACKERS, _oldOwner, 0));
		}
		if (getObjects(ATTACKERS).size() == 0)
		{
			broadcastInZone2(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST).addResidenceName(getResidence()));
			reCalcNextTime(false);
			return;
		}
		SiegeClanDAO.getInstance().delete(getResidence());
		updateParticles(true, ATTACKERS);
		broadcastTo(new SystemMessage2(SystemMsg.THE_SIEGE_TO_CONQUER_S1_HAS_BEGUN).addResidenceName(getResidence()), ATTACKERS);
		super.startEvent();
	}
	
	/**
	 * Method stopEvent.
	 * @param step boolean
	 */
	@Override
	public void stopEvent(boolean step)
	{
		Clan newOwner = getResidence().getOwner();
		if (newOwner != null)
		{
			newOwner.broadcastToOnlineMembers(PlaySound.SIEGE_VICTORY);
			newOwner.incReputation(1700, false, toString());
			broadcastTo(new SystemMessage2(SystemMsg.S1_CLAN_HAS_DEFEATED_S2).addString(newOwner.getName()).addResidenceName(getResidence()), ATTACKERS);
			broadcastTo(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_IS_FINISHED).addResidenceName(getResidence()), ATTACKERS);
		}
		else
		{
			broadcastTo(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_HAS_ENDED_IN_A_DRAW).addResidenceName(getResidence()), ATTACKERS);
		}
		updateParticles(false, ATTACKERS);
		removeObjects(ATTACKERS);
		super.stopEvent(step);
		_oldOwner = null;
	}
	
	/**
	 * Method setRegistrationOver.
	 * @param b boolean
	 */
	@Override
	public void setRegistrationOver(boolean b)
	{
		if (b)
		{
			broadcastTo(new SystemMessage2(SystemMsg.THE_DEADLINE_TO_REGISTER_FOR_THE_SIEGE_OF_S1_HAS_PASSED).addResidenceName(getResidence()), ATTACKERS);
		}
		super.setRegistrationOver(b);
	}
	
	/**
	 * Method processStep.
	 * @param clan Clan
	 */
	@Override
	public void processStep(Clan clan)
	{
		if (clan != null)
		{
			getResidence().changeOwner(clan);
		}
		stopEvent(true);
	}
	
	/**
	 * Method loadSiegeClans.
	 */
	@Override
	public void loadSiegeClans()
	{
		addObjects(ATTACKERS, SiegeClanDAO.getInstance().load(getResidence(), ATTACKERS));
	}
	
	/**
	 * Method getUserRelation.
	 * @param thisPlayer Player
	 * @param result int
	 * @return int
	 */
	@Override
	public int getUserRelation(Player thisPlayer, int result)
	{
		return result;
	}
	
	/**
	 * Method getRelation.
	 * @param thisPlayer Player
	 * @param targetPlayer Player
	 * @param result int
	 * @return int
	 */
	@Override
	public int getRelation(Player thisPlayer, Player targetPlayer, int result)
	{
		return result;
	}
	
	/**
	 * Method canRessurect.
	 * @param resurrectPlayer Player
	 * @param target Creature
	 * @param force boolean
	 * @return boolean
	 */
	@Override
	public boolean canRessurect(Player resurrectPlayer, Creature target, boolean force)
	{
		boolean playerInZone = resurrectPlayer.isInZone(Zone.ZoneType.SIEGE);
		boolean targetInZone = target.isInZone(Zone.ZoneType.SIEGE);
		if (!playerInZone && !targetInZone)
		{
			return true;
		}
		if (!targetInZone)
		{
			return false;
		}
		Player targetPlayer = target.getPlayer();
		ClanHallSiegeEvent siegeEvent = target.getEvent(ClanHallSiegeEvent.class);
		if (siegeEvent != this)
		{
			if (force)
			{
				targetPlayer.sendPacket(SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE);
			}
			resurrectPlayer.sendPacket(force ? SystemMsg.IT_IS_NOT_POSSIBLE_TO_RESURRECT_IN_BATTLEFIELDS_WHERE_A_SIEGE_WAR_IS_TAKING_PLACE : SystemMsg.INVALID_TARGET);
			return false;
		}
		SiegeClanObject targetSiegeClan = siegeEvent.getSiegeClan(ATTACKERS, targetPlayer.getClan());
		if (targetSiegeClan.getFlag() == null)
		{
			if (force)
			{
				targetPlayer.sendPacket(SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE);
			}
			resurrectPlayer.sendPacket(force ? SystemMsg.IF_A_BASE_CAMP_DOES_NOT_EXIST_RESURRECTION_IS_NOT_POSSIBLE : SystemMsg.INVALID_TARGET);
			return false;
		}
		if (force)
		{
			return true;
		}
		resurrectPlayer.sendPacket(SystemMsg.INVALID_TARGET);
		return false;
	}
}
