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

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.instancemanager.PlayerMessageStack;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.actions.StartStopAction;
import lineage2.gameserver.model.entity.events.objects.AuctionSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanHallAuctionEvent extends SiegeEvent<ClanHall, AuctionSiegeClanObject>
{
	/**
	 * Field _endSiegeDate.
	 */
	private final Calendar _endSiegeDate = Calendar.getInstance();
	
	/**
	 * Constructor for ClanHallAuctionEvent.
	 * @param set MultiValueSet<String>
	 */
	public ClanHallAuctionEvent(MultiValueSet<String> set)
	{
		super(set);
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onStart boolean
	 */
	@Override
	public void reCalcNextTime(boolean onStart)
	{
		clearActions();
		_onTimeActions.clear();
		
		Clan owner = getResidence().getOwner();
		
		_endSiegeDate.setTimeInMillis(0);
		if ((getResidence().getAuctionLength() == 0) && (owner == null))
		{
			getResidence().getSiegeDate().setTimeInMillis(System.currentTimeMillis());
			getResidence().getSiegeDate().set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			getResidence().getSiegeDate().set(Calendar.HOUR_OF_DAY, 15);
			getResidence().getSiegeDate().set(Calendar.MINUTE, 0);
			getResidence().getSiegeDate().set(Calendar.SECOND, 0);
			getResidence().getSiegeDate().set(Calendar.MILLISECOND, 0);
			
			getResidence().setAuctionLength(7);
			getResidence().setAuctionMinBid(getResidence().getBaseMinBid());
			getResidence().setJdbcState(JdbcEntityState.UPDATED);
			getResidence().update();
			
			_onTimeActions.clear();
			addOnTimeAction(0, new StartStopAction(EVENT, true));
			addOnTimeAction(getResidence().getAuctionLength() * 86400, new StartStopAction(EVENT, false));
			
			_endSiegeDate.setTimeInMillis(getResidence().getSiegeDate().getTimeInMillis() + (getResidence().getAuctionLength() * 86400000L));
			
			registerActions();
		}
		else if ((getResidence().getAuctionLength() == 0) && (owner != null))
		{
		}
		else
		{
			long endDate = getResidence().getSiegeDate().getTimeInMillis() + (getResidence().getAuctionLength() * 86400000L);
			if (endDate <= System.currentTimeMillis())
			{
				getResidence().getSiegeDate().setTimeInMillis(System.currentTimeMillis());
			}
			
			_endSiegeDate.setTimeInMillis(getResidence().getSiegeDate().getTimeInMillis() + (getResidence().getAuctionLength() * 86400000L));
			
			_onTimeActions.clear();
			addOnTimeAction(0, new StartStopAction(EVENT, true));
			addOnTimeAction(getResidence().getAuctionLength() * 86400, new StartStopAction(EVENT, false));
			
			registerActions();
		}
	}
	
	/**
	 * Method stopEvent.
	 * @param step boolean
	 */
	@Override
	public void stopEvent(boolean step)
	{
		List<AuctionSiegeClanObject> siegeClanObjects = removeObjects(ATTACKERS);
		AuctionSiegeClanObject[] clans = siegeClanObjects.toArray(new AuctionSiegeClanObject[siegeClanObjects.size()]);
		Arrays.sort(clans, SiegeClanObject.SiegeClanComparatorImpl.getInstance());
		
		Clan oldOwner = getResidence().getOwner();
		AuctionSiegeClanObject winnerSiegeClan = clans.length > 0 ? clans[0] : null;
		
		if (winnerSiegeClan != null)
		{
			SystemMessage2 msg = new SystemMessage2(SystemMsg.THE_CLAN_HALL_WHICH_WAS_PUT_UP_FOR_AUCTION_HAS_BEEN_AWARDED_TO_S1_CLAN).addString(winnerSiegeClan.getClan().getName());
			for (AuctionSiegeClanObject $siegeClan : siegeClanObjects)
			{
				Player player = $siegeClan.getClan().getLeader().getPlayer();
				if (player != null)
				{
					player.sendPacket(msg);
				}
				else
				{
					PlayerMessageStack.getInstance().mailto($siegeClan.getClan().getLeaderId(), msg);
				}
				
				if ($siegeClan != winnerSiegeClan)
				{
					long returnBid = $siegeClan.getParam() - (long) ($siegeClan.getParam() * 0.1);
					
					$siegeClan.getClan().getWarehouse().addItem(ItemTemplate.ITEM_ID_ADENA, returnBid);
				}
			}
			
			SiegeClanDAO.getInstance().delete(getResidence());
			
			if (oldOwner != null)
			{
				oldOwner.getWarehouse().addItem(ItemTemplate.ITEM_ID_ADENA, getResidence().getDeposit());
			}
			
			getResidence().setAuctionLength(0);
			getResidence().setAuctionMinBid(0);
			getResidence().setAuctionDescription(StringUtils.EMPTY);
			getResidence().getSiegeDate().setTimeInMillis(0);
			getResidence().getLastSiegeDate().setTimeInMillis(0);
			getResidence().getOwnDate().setTimeInMillis(System.currentTimeMillis());
			getResidence().setJdbcState(JdbcEntityState.UPDATED);
			
			getResidence().changeOwner(winnerSiegeClan.getClan());
			getResidence().startCycleTask();
		}
		else
		{
			if (oldOwner != null)
			{
				Player player = oldOwner.getLeader().getPlayer();
				if (player != null)
				{
					player.sendPacket(SystemMsg.THE_CLAN_HALL_WHICH_HAD_BEEN_PUT_UP_FOR_AUCTION_WAS_NOT_SOLD_AND_THEREFORE_HAS_BEEN_RELISTED);
				}
				else
				{
					PlayerMessageStack.getInstance().mailto(oldOwner.getLeaderId(), SystemMsg.THE_CLAN_HALL_WHICH_HAD_BEEN_PUT_UP_FOR_AUCTION_WAS_NOT_SOLD_AND_THEREFORE_HAS_BEEN_RELISTED.packet(null));
				}
			}
		}
		
		super.stopEvent(step);
	}
	
	/**
	 * Method isParticle.
	 * @param player Player
	 * @return boolean
	 */
	@Override
	public boolean isParticle(Player player)
	{
		return false;
	}
	
	/**
	 * Method newSiegeClan.
	 * @param type String
	 * @param clanId int
	 * @param param long
	 * @param date long
	 * @return AuctionSiegeClanObject
	 */
	@Override
	public AuctionSiegeClanObject newSiegeClan(String type, int clanId, long param, long date)
	{
		Clan clan = ClanTable.getInstance().getClan(clanId);
		return clan == null ? null : new AuctionSiegeClanObject(type, clan, param, date);
	}
	
	/**
	 * Method getEndSiegeDate.
	 * @return Calendar
	 */
	public Calendar getEndSiegeDate()
	{
		return _endSiegeDate;
	}
}
