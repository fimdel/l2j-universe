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
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.tables.ClanTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestStartPledgeWar extends L2GameClientPacket
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
		Clan clan = activeChar.getClan();
		if (clan == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (!((activeChar.getClanPrivileges() & Clan.CP_CL_CLAN_WAR) == Clan.CP_CL_CLAN_WAR))
		{
			activeChar.sendActionFailed();
			return;
		}
		if (clan.getWarsCount() >= 30)
		{
			activeChar.sendPacket(Msg.A_DECLARATION_OF_WAR_AGAINST_MORE_THAN_30_CLANS_CANT_BE_MADE_AT_THE_SAME_TIME, ActionFail.STATIC);
			return;
		}
		if ((clan.getLevel() < 3) || (clan.getAllSize() < 15))
		{
			activeChar.sendPacket(Msg.A_CLAN_WAR_CAN_BE_DECLARED_ONLY_IF_THE_CLAN_IS_LEVEL_THREE_OR_ABOVE_AND_THE_NUMBER_OF_CLAN_MEMBERS_IS_FIFTEEN_OR_GREATER, ActionFail.STATIC);
			return;
		}
		Clan targetClan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (targetClan == null)
		{
			activeChar.sendPacket(Msg.THE_DECLARATION_OF_WAR_CANT_BE_MADE_BECAUSE_THE_CLAN_DOES_NOT_EXIST_OR_ACT_FOR_A_LONG_PERIOD, ActionFail.STATIC);
			return;
		}
		else if (clan.equals(targetClan))
		{
			activeChar.sendPacket(Msg.FOOL_YOU_CANNOT_DECLARE_WAR_AGAINST_YOUR_OWN_CLAN, ActionFail.STATIC);
			return;
		}
		else if (clan.isAtWarWith(targetClan.getClanId()))
		{
			activeChar.sendPacket(Msg.THE_DECLARATION_OF_WAR_HAS_BEEN_ALREADY_MADE_TO_THE_CLAN, ActionFail.STATIC);
			return;
		}
		else if ((clan.getAllyId() == targetClan.getAllyId()) && (clan.getAllyId() != 0))
		{
			activeChar.sendPacket(Msg.A_DECLARATION_OF_CLAN_WAR_AGAINST_AN_ALLIED_CLAN_CANT_BE_MADE, ActionFail.STATIC);
			return;
		}
		else if ((targetClan.getLevel() < 3) || (targetClan.getAllSize() < 15))
		{
			activeChar.sendPacket(Msg.A_CLAN_WAR_CAN_BE_DECLARED_ONLY_IF_THE_CLAN_IS_LEVEL_THREE_OR_ABOVE_AND_THE_NUMBER_OF_CLAN_MEMBERS_IS_FIFTEEN_OR_GREATER, ActionFail.STATIC);
			return;
		}
		ClanTable.getInstance().startClanWar(activeChar.getClan(), targetClan);
	}
}
