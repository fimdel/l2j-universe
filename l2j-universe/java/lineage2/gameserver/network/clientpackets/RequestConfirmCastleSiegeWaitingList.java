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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.CastleSiegeDefenderList;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestConfirmCastleSiegeWaitingList extends L2GameClientPacket
{
	/**
	 * Field _approved.
	 */
	private boolean _approved;
	/**
	 * Field _unitId.
	 */
	private int _unitId;
	/**
	 * Field _clanId.
	 */
	private int _clanId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_unitId = readD();
		_clanId = readD();
		_approved = readD() == 1;
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if (player.getClan() == null)
		{
			return;
		}
		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, _unitId);
		if ((castle == null) || (player.getClan().getCastle() != castle.getId()))
		{
			player.sendActionFailed();
			return;
		}
		CastleSiegeEvent siegeEvent = castle.getSiegeEvent();
		SiegeClanObject siegeClan = siegeEvent.getSiegeClan(CastleSiegeEvent.DEFENDERS_WAITING, _clanId);
		if (siegeClan == null)
		{
			siegeClan = siegeEvent.getSiegeClan(SiegeEvent.DEFENDERS, _clanId);
		}
		if (siegeClan == null)
		{
			return;
		}
		if ((player.getClanPrivileges() & Clan.CP_CS_MANAGE_SIEGE) != Clan.CP_CS_MANAGE_SIEGE)
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_CASTLE_DEFENDER_LIST);
			return;
		}
		if (siegeEvent.isRegistrationOver())
		{
			player.sendPacket(SystemMsg.THIS_IS_NOT_THE_TIME_FOR_SIEGE_REGISTRATION_AND_SO_REGISTRATIONS_CANNOT_BE_ACCEPTED_OR_REJECTED);
			return;
		}
		int allSize = siegeEvent.getObjects(SiegeEvent.DEFENDERS).size();
		if (allSize >= CastleSiegeEvent.MAX_SIEGE_CLANS)
		{
			player.sendPacket(SystemMsg.NO_MORE_REGISTRATIONS_MAY_BE_ACCEPTED_FOR_THE_DEFENDER_SIDE);
			return;
		}
		siegeEvent.removeObject(siegeClan.getType(), siegeClan);
		if (_approved)
		{
			siegeClan.setType(SiegeEvent.DEFENDERS);
		}
		else
		{
			siegeClan.setType(CastleSiegeEvent.DEFENDERS_REFUSED);
		}
		siegeEvent.addObject(siegeClan.getType(), siegeClan);
		SiegeClanDAO.getInstance().update(castle, siegeClan);
		player.sendPacket(new CastleSiegeDefenderList(castle));
	}
}
