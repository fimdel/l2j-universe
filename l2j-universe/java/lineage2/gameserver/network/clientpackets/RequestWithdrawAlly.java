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
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestWithdrawAlly extends L2GameClientPacket
{
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
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
		if (!activeChar.isClanLeader())
		{
			activeChar.sendPacket(Msg.ONLY_THE_CLAN_LEADER_MAY_APPLY_FOR_WITHDRAWAL_FROM_THE_ALLIANCE);
			return;
		}
		if (clan.getAlliance() == null)
		{
			activeChar.sendPacket(Msg.YOU_ARE_NOT_CURRENTLY_ALLIED_WITH_ANY_CLANS);
			return;
		}
		if (clan.equals(clan.getAlliance().getLeader()))
		{
			activeChar.sendPacket(Msg.ALLIANCE_LEADERS_CANNOT_WITHDRAW);
			return;
		}
		clan.broadcastToOnlineMembers(Msg.YOU_HAVE_WITHDRAWN_FROM_THE_ALLIANCE, Msg.A_CLAN_THAT_HAS_WITHDRAWN_OR_BEEN_EXPELLED_CANNOT_ENTER_INTO_AN_ALLIANCE_WITHIN_ONE_DAY_OF_WITHDRAWAL_OR_EXPULSION);
		Alliance alliance = clan.getAlliance();
		clan.setAllyId(0);
		clan.setLeavedAlly();
		alliance.broadcastAllyStatus();
		alliance.removeAllyMember(clan.getClanId());
	}
}
