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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.tables.ClanTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestOustAlly extends L2GameClientPacket
{
	/**
	 * Field _clanName.
	 */
	private String _clanName;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_clanName = readS(32);
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
		Clan leaderClan = activeChar.getClan();
		if (leaderClan == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		Alliance alliance = leaderClan.getAlliance();
		if (alliance == null)
		{
			activeChar.sendPacket(Msg.YOU_ARE_NOT_CURRENTLY_ALLIED_WITH_ANY_CLANS);
			return;
		}
		Clan clan;
		if (!activeChar.isAllyLeader())
		{
			activeChar.sendPacket(Msg.FEATURE_AVAILABLE_TO_ALLIANCE_LEADERS_ONLY);
			return;
		}
		if (_clanName == null)
		{
			return;
		}
		clan = ClanTable.getInstance().getClanByName(_clanName);
		if (clan != null)
		{
			if (!alliance.isMember(clan.getClanId()))
			{
				activeChar.sendActionFailed();
				return;
			}
			if (alliance.getLeader().equals(clan))
			{
				activeChar.sendPacket(Msg.YOU_HAVE_FAILED_TO_WITHDRAW_FROM_THE_ALLIANCE);
				return;
			}
			clan.broadcastToOnlineMembers(new SystemMessage("Your clan has been expelled from " + alliance.getAllyName() + " alliance."), new SystemMessage(SystemMessage.A_CLAN_THAT_HAS_WITHDRAWN_OR_BEEN_EXPELLED_CANNOT_ENTER_INTO_AN_ALLIANCE_WITHIN_ONE_DAY_OF_WITHDRAWAL_OR_EXPULSION));
			clan.setAllyId(0);
			clan.setLeavedAlly();
			alliance.broadcastAllyStatus();
			alliance.removeAllyMember(clan.getClanId());
			alliance.setExpelledMember();
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestOustAlly.ClanDismissed", activeChar).addString(clan.getName()).addString(alliance.getAllyName()));
		}
	}
}
