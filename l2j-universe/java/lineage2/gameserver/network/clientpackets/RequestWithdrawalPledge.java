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
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListDelete;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListDeleteAll;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestWithdrawalPledge extends L2GameClientPacket
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
		if (activeChar.getClanId() == 0)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInCombat())
		{
			activeChar.sendPacket(Msg.ONE_CANNOT_LEAVE_ONES_CLAN_DURING_COMBAT);
			return;
		}
		Clan clan = activeChar.getClan();
		if (clan == null)
		{
			return;
		}
		UnitMember member = clan.getAnyMember(activeChar.getObjectId());
		if (member == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (member.isClanLeader())
		{
			activeChar.sendMessage("A clan leader may not be dismissed.");
			return;
		}
		int subUnitType = activeChar.getPledgeType();
		clan.removeClanMember(subUnitType, activeChar.getObjectId());
		clan.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.S1_HAS_WITHDRAWN_FROM_THE_CLAN).addString(activeChar.getName()), new PledgeShowMemberListDelete(activeChar.getName()));
		if (subUnitType == Clan.SUBUNIT_ACADEMY)
		{
			activeChar.setLvlJoinedAcademy(0);
		}
		activeChar.setClan(null);
		if (!activeChar.isNoble())
		{
			activeChar.setTitle(StringUtils.EMPTY);
		}
		activeChar.setLeaveClanCurTime();
		activeChar.broadcastCharInfo();
		activeChar.sendPacket(SystemMsg.YOU_HAVE_RECENTLY_BEEN_DISMISSED_FROM_A_CLAN, PledgeShowMemberListDeleteAll.STATIC);
	}
}
