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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.tables.ClanTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestStopPledgeWar extends L2GameClientPacket
{
	/**
	 * Field _pledgeName.
	 */
	private String _pledgeName;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_pledgeName = readS(32);
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		Clan playerClan = activeChar.getClan();
		if (playerClan == null)
		{
			return;
		}
		if (!((activeChar.getClanPrivileges() & Clan.CP_CL_CLAN_WAR) == Clan.CP_CL_CLAN_WAR))
		{
			activeChar.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT, ActionFail.STATIC);
			return;
		}
		Clan clan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (clan == null)
		{
			activeChar.sendPacket(SystemMsg.CLAN_NAME_IS_INVALID, ActionFail.STATIC);
			return;
		}
		if (!playerClan.isAtWarWith(clan.getClanId()))
		{
			activeChar.sendPacket(Msg.YOU_HAVE_NOT_DECLARED_A_CLAN_WAR_TO_S1_CLAN, ActionFail.STATIC);
			return;
		}
		for (UnitMember mbr : playerClan)
		{
			if (mbr.isOnline() && mbr.getPlayer().isInCombat())
			{
				activeChar.sendPacket(Msg.A_CEASE_FIRE_DURING_A_CLAN_WAR_CAN_NOT_BE_CALLED_WHILE_MEMBERS_OF_YOUR_CLAN_ARE_ENGAGED_IN_BATTLE, ActionFail.STATIC);
				return;
			}
		}
		ClanTable.getInstance().stopClanWar(playerClan, clan);
	}
}
