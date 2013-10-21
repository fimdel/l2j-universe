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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestOustPledgeMember extends L2GameClientPacket
{
	/**
	 * Field _target.
	 */
	private String _target;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_target = readS(16);
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || !((activeChar.getClanPrivileges() & Clan.CP_CL_DISMISS) == Clan.CP_CL_DISMISS))
		{
			return;
		}
		Clan clan = activeChar.getClan();
		UnitMember member = clan.getAnyMember(_target);
		if (member == null)
		{
			activeChar.sendPacket(SystemMsg.THE_TARGET_MUST_BE_A_CLAN_MEMBER);
			return;
		}
		Player memberPlayer = member.getPlayer();
		if (member.isOnline() && member.getPlayer().isInCombat())
		{
			activeChar.sendPacket(SystemMsg.A_CLAN_MEMBER_MAY_NOT_BE_DISMISSED_DURING_COMBAT);
			return;
		}
		if (member.isClanLeader())
		{
			activeChar.sendPacket(SystemMsg.THIS_CLAN_MEMBER_CANNOT_WITHDRAW_OR_BE_EXPELLED_WHILE_PARTICIPATING_IN_A_TERRITORY_WAR);
			return;
		}
		int subUnitType = member.getPledgeType();
		clan.removeClanMember(subUnitType, member.getObjectId());
		clan.broadcastToOnlineMembers(new SystemMessage(SystemMessage.CLAN_MEMBER_S1_HAS_BEEN_EXPELLED).addString(_target), new PledgeShowMemberListDelete(_target));
		clan.setExpelledMember();
		if (memberPlayer == null)
		{
			return;
		}
		if (subUnitType == Clan.SUBUNIT_ACADEMY)
		{
			memberPlayer.setLvlJoinedAcademy(0);
		}
		memberPlayer.setClan(null);
		if (!memberPlayer.isNoble())
		{
			memberPlayer.setTitle("");
		}
		memberPlayer.setLeaveClanCurTime();
		memberPlayer.broadcastCharInfo();
		memberPlayer.store(true);
		memberPlayer.sendPacket(Msg.YOU_HAVE_RECENTLY_BEEN_DISMISSED_FROM_A_CLAN_YOU_ARE_NOT_ALLOWED_TO_JOIN_ANOTHER_CLAN_FOR_24_HOURS, PledgeShowMemberListDeleteAll.STATIC);
	}
}
