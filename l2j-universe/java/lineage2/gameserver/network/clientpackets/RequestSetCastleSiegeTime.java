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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.CastleSiegeEvent;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.CastleSiegeInfo;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSetCastleSiegeTime extends L2GameClientPacket
{
	/**
	 * Field _time. Field _id.
	 */
	private int _id, _time;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_id = readD();
		_time = readD();
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
		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, _id);
		if (castle == null)
		{
			return;
		}
		if (player.getClan().getCastle() != castle.getId())
		{
			return;
		}
		if ((player.getClanPrivileges() & Clan.CP_CS_MANAGE_SIEGE) != Clan.CP_CS_MANAGE_SIEGE)
		{
			player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_THE_AUTHORITY_TO_MODIFY_THE_SIEGE_TIME);
			return;
		}
		CastleSiegeEvent siegeEvent = castle.getSiegeEvent();
		siegeEvent.setNextSiegeTime(_time);
		player.sendPacket(new CastleSiegeInfo(castle, player));
	}
}
