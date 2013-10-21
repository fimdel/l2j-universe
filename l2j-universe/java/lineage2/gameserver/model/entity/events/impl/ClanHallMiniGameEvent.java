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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lineage2.commons.collections.CollectionUtils;
import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.objects.CMGSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.ClanTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanHallMiniGameEvent extends SiegeEvent<ClanHall, CMGSiegeClanObject>
{
	/**
	 * Field NEXT_STEP. (value is ""next_step"")
	 */
	public static final String NEXT_STEP = "next_step";
	/**
	 * Field REFUND. (value is ""refund"")
	 */
	public static final String REFUND = "refund";
	/**
	 * Field _arenaClosed.
	 */
	private boolean _arenaClosed = true;
	
	/**
	 * Constructor for ClanHallMiniGameEvent.
	 * @param set MultiValueSet<String>
	 */
	public ClanHallMiniGameEvent(MultiValueSet<String> set)
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
		List<CMGSiegeClanObject> siegeClans = getObjects(ATTACKERS);
		if (siegeClans.size() < 2)
		{
			CMGSiegeClanObject siegeClan = CollectionUtils.safeGet(siegeClans, 0);
			if (siegeClan != null)
			{
				CMGSiegeClanObject oldSiegeClan = getSiegeClan(REFUND, siegeClan.getObjectId());
				if (oldSiegeClan != null)
				{
					SiegeClanDAO.getInstance().delete(getResidence(), siegeClan);
					oldSiegeClan.setParam(oldSiegeClan.getParam() + siegeClan.getParam());
					SiegeClanDAO.getInstance().update(getResidence(), oldSiegeClan);
				}
				else
				{
					siegeClan.setType(REFUND);
					siegeClans.remove(siegeClan);
					addObject(REFUND, siegeClan);
					SiegeClanDAO.getInstance().update(getResidence(), siegeClan);
				}
			}
			siegeClans.clear();
			broadcastTo(SystemMsg.THIS_CLAN_HALL_WAR_HAS_BEEN_CANCELLED, ATTACKERS);
			broadcastInZone2(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_HAS_ENDED_IN_A_DRAW).addResidenceName(getResidence()));
			reCalcNextTime(false);
			return;
		}
		CMGSiegeClanObject[] clans = siegeClans.toArray(new CMGSiegeClanObject[siegeClans.size()]);
		Arrays.sort(clans, SiegeClanObject.SiegeClanComparatorImpl.getInstance());
		List<CMGSiegeClanObject> temp = new ArrayList<>(4);
		for (CMGSiegeClanObject siegeClan : clans)
		{
			SiegeClanDAO.getInstance().delete(getResidence(), siegeClan);
			if (temp.size() == 4)
			{
				siegeClans.remove(siegeClan);
				siegeClan.broadcast(SystemMsg.YOU_HAVE_FAILED_IN_YOUR_ATTEMPT_TO_REGISTER_FOR_THE_CLAN_HALL_WAR);
			}
			else
			{
				temp.add(siegeClan);
				siegeClan.broadcast(SystemMsg.YOU_HAVE_BEEN_REGISTERED_FOR_A_CLAN_HALL_WAR);
			}
		}
		_arenaClosed = false;
		super.startEvent();
	}
	
	/**
	 * Method stopEvent.
	 * @param step boolean
	 */
	@Override
	public void stopEvent(boolean step)
	{
		removeBanishItems();
		Clan newOwner = getResidence().getOwner();
		if (newOwner != null)
		{
			if (_oldOwner != newOwner)
			{
				newOwner.broadcastToOnlineMembers(PlaySound.SIEGE_VICTORY);
				newOwner.incReputation(1700, false, toString());
			}
			broadcastTo(new SystemMessage2(SystemMsg.S1_CLAN_HAS_DEFEATED_S2).addString(newOwner.getName()).addResidenceName(getResidence()), ATTACKERS, DEFENDERS);
			broadcastTo(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_IS_FINISHED).addResidenceName(getResidence()), ATTACKERS, DEFENDERS);
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
	 * Method nextStep.
	 */
	public void nextStep()
	{
		List<CMGSiegeClanObject> siegeClans = getObjects(ATTACKERS);
		for (int i = 0; i < siegeClans.size(); i++)
		{
			spawnAction("arena_" + i, true);
		}
		_arenaClosed = true;
		updateParticles(true, ATTACKERS);
		broadcastTo(new SystemMessage2(SystemMsg.THE_SIEGE_TO_CONQUER_S1_HAS_BEGUN).addResidenceName(getResidence()), ATTACKERS);
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
			broadcastTo(SystemMsg.THE_REGISTRATION_PERIOD_FOR_A_CLAN_HALL_WAR_HAS_ENDED, ATTACKERS);
		}
		super.setRegistrationOver(b);
	}
	
	/**
	 * Method newSiegeClan.
	 * @param type String
	 * @param clanId int
	 * @param param long
	 * @param date long
	 * @return CMGSiegeClanObject
	 */
	@Override
	public CMGSiegeClanObject newSiegeClan(String type, int clanId, long param, long date)
	{
		Clan clan = ClanTable.getInstance().getClan(clanId);
		return clan == null ? null : new CMGSiegeClanObject(type, clan, param, date);
	}
	
	/**
	 * Method announce.
	 * @param val int
	 */
	@Override
	public void announce(int val)
	{
		int seconds = val % 60;
		int min = val / 60;
		if (min > 0)
		{
			SystemMsg msg = min > 10 ? SystemMsg.IN_S1_MINUTES_THE_GAME_WILL_BEGIN_ALL_PLAYERS_MUST_HURRY_AND_MOVE_TO_THE_LEFT_SIDE_OF_THE_CLAN_HALLS_ARENA : SystemMsg.IN_S1_MINUTES_THE_GAME_WILL_BEGIN_ALL_PLAYERS_PLEASE_ENTER_THE_ARENA_NOW;
			broadcastTo(new SystemMessage2(msg).addInteger(min), ATTACKERS);
		}
		else
		{
			broadcastTo(new SystemMessage2(SystemMsg.IN_S1_SECONDS_THE_GAME_WILL_BEGIN).addInteger(seconds), ATTACKERS);
		}
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
		addObjects(REFUND, SiegeClanDAO.getInstance().load(getResidence(), REFUND));
	}
	
	/**
	 * Method action.
	 * @param name String
	 * @param start boolean
	 */
	@Override
	public void action(String name, boolean start)
	{
		if (name.equalsIgnoreCase(NEXT_STEP))
		{
			nextStep();
		}
		else
		{
			super.action(name, start);
		}
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
	 * Method isArenaClosed.
	 * @return boolean
	 */
	public boolean isArenaClosed()
	{
		return _arenaClosed;
	}
	
	/**
	 * Method onAddEvent.
	 * @param object GameObject
	 */
	@Override
	public void onAddEvent(GameObject object)
	{
		if (object.isItem())
		{
			addBanishItem((ItemInstance) object);
		}
	}
}
