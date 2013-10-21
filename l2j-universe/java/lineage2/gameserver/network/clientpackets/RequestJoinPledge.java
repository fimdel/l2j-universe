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
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.AskJoinPledge;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestJoinPledge extends L2GameClientPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	/**
	 * Field _pledgeType.
	 */
	private int _pledgeType;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_pledgeType = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (activeChar.getClan() == null))
		{
			return;
		}
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isProcessingRequest())
		{
			activeChar.sendPacket(Msg.WAITING_FOR_ANOTHER_REPLY);
			return;
		}
		Clan clan = activeChar.getClan();
		if (!clan.canInvite())
		{
			activeChar.sendPacket(Msg.AFTER_A_CLAN_MEMBER_IS_DISMISSED_FROM_A_CLAN_THE_CLAN_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_ACCEPTING_A_NEW_MEMBER);
			return;
		}
		if (_objectId == activeChar.getObjectId())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_ASK_YOURSELF_TO_APPLY_TO_A_CLAN);
			return;
		}
		if ((activeChar.getClanPrivileges() & Clan.CP_CL_INVITE_CLAN) != Clan.CP_CL_INVITE_CLAN)
		{
			activeChar.sendPacket(Msg.ONLY_THE_LEADER_CAN_GIVE_OUT_INVITATIONS);
			return;
		}
		GameObject object = activeChar.getVisibleObject(_objectId);
		if ((object == null) || !object.isPlayer())
		{
			activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return;
		}
		Player member = (Player) object;
		if (member.getClan() == activeChar.getClan())
		{
			activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return;
		}
		if (!member.getPlayerAccess().CanJoinClan)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_JOIN_THE_CLAN_BECAUSE_ONE_DAY_HAS_NOT_YET_PASSED_SINCE_HE_SHE_LEFT_ANOTHER_CLAN).addName(member));
			return;
		}
		if (member.getClan() != null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_WORKING_WITH_ANOTHER_CLAN).addName(member));
			return;
		}
		if (member.isBusy())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_BUSY_PLEASE_TRY_AGAIN_LATER).addName(member));
			return;
		}
		if ((_pledgeType == Clan.SUBUNIT_ACADEMY) && ((member.getLevel() > 75) || (member.getClassLevel() > 3)))
		{
			activeChar.sendPacket(Msg.TO_JOIN_A_CLAN_ACADEMY_CHARACTERS_MUST_BE_LEVEL_40_OR_BELOW_NOT_BELONG_ANOTHER_CLAN_AND_NOT_YET_COMPLETED_THEIR_2ND_CLASS_TRANSFER);
			return;
		}
		if (clan.getUnitMembersSize(_pledgeType) >= clan.getSubPledgeLimit(_pledgeType))
		{
			if (_pledgeType == 0)
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_FULL_AND_CANNOT_ACCEPT_ADDITIONAL_CLAN_MEMBERS_AT_THIS_TIME).addString(clan.getName()));
			}
			else
			{
				activeChar.sendPacket(Msg.THE_ACADEMY_ROYAL_GUARD_ORDER_OF_KNIGHTS_IS_FULL_AND_CANNOT_ACCEPT_NEW_MEMBERS_AT_THIS_TIME);
			}
			return;
		}
		Request request = new Request(L2RequestType.CLAN, activeChar, member).setTimeout(10000L);
		request.set("pledgeType", _pledgeType);
		member.sendPacket(new AskJoinPledge(activeChar.getObjectId(), activeChar.getClan().getName()));
	}
}
