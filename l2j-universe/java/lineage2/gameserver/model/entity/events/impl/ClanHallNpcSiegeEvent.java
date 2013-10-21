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
public class ClanHallNpcSiegeEvent extends SiegeEvent<ClanHall, SiegeClanObject>
{
	/**
	 * Constructor for ClanHallNpcSiegeEvent.
	 * @param set MultiValueSet<String>
	 */
	public ClanHallNpcSiegeEvent(MultiValueSet<String> set)
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
		broadcastInZone(new SystemMessage2(SystemMsg.THE_SIEGE_TO_CONQUER_S1_HAS_BEGUN).addResidenceName(getResidence()));
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
			if (_oldOwner != newOwner)
			{
				newOwner.broadcastToOnlineMembers(PlaySound.SIEGE_VICTORY);
				newOwner.incReputation(1700, false, toString());
				if (_oldOwner != null)
				{
					_oldOwner.incReputation(-1700, false, toString());
				}
			}
			broadcastInZone(new SystemMessage2(SystemMsg.S1_CLAN_HAS_DEFEATED_S2).addString(newOwner.getName()).addResidenceName(getResidence()));
			broadcastInZone(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_IS_FINISHED).addResidenceName(getResidence()));
		}
		else
		{
			broadcastInZone(new SystemMessage2(SystemMsg.THE_SIEGE_OF_S1_HAS_ENDED_IN_A_DRAW).addResidenceName(getResidence()));
		}
		super.stopEvent(step);
		_oldOwner = null;
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
	}
}
