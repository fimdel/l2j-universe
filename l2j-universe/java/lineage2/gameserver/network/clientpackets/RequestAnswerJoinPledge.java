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

import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.JoinPledge;
import lineage2.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListAdd;
import lineage2.gameserver.network.serverpackets.PledgeSkillList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAnswerJoinPledge extends L2GameClientPacket
{
	/**
	 * Field _response.
	 */
	private int _response;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_response = _buf.hasRemaining() ? readD() : 0;
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
		Request request = player.getRequest();
		if ((request == null) || !request.isTypeOf(L2RequestType.CLAN))
		{
			return;
		}
		if (!request.isInProgress())
		{
			request.cancel();
			player.sendActionFailed();
			return;
		}
		if (player.isOutOfControl())
		{
			request.cancel();
			player.sendActionFailed();
			return;
		}
		Player requestor = request.getRequestor();
		if (requestor == null)
		{
			request.cancel();
			player.sendPacket(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE);
			player.sendActionFailed();
			return;
		}
		if (requestor.getRequest() != request)
		{
			request.cancel();
			player.sendActionFailed();
			return;
		}
		Clan clan = requestor.getClan();
		if (clan == null)
		{
			request.cancel();
			player.sendActionFailed();
			return;
		}
		if (_response == 0)
		{
			request.cancel();
			requestor.sendPacket(new SystemMessage2(SystemMsg.S1_DECLINED_YOUR_CLAN_INVITATION).addName(player));
			return;
		}
		if (!player.canJoinClan())
		{
			request.cancel();
			player.sendPacket(SystemMsg.AFTER_LEAVING_OR_HAVING_BEEN_DISMISSED_FROM_A_CLAN_YOU_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_JOINING_ANOTHER_CLAN);
			return;
		}
		try
		{
			player.sendPacket(new JoinPledge(requestor.getClanId()));
			int pledgeType = request.getInteger("pledgeType");
			SubUnit subUnit = clan.getSubUnit(pledgeType);
			if (subUnit == null)
			{
				return;
			}
			UnitMember member = new UnitMember(clan, player.getName(), player.getTitle(), player.getLevel(), player.getClassId().getId(), player.getObjectId(), pledgeType, player.getPowerGrade(), player.getApprentice(), player.getSex(), Clan.SUBUNIT_NONE);
			subUnit.addUnitMember(member);
			player.setPledgeType(pledgeType);
			player.setClan(clan);
			member.setPlayerInstance(player, false);
			if (pledgeType == Clan.SUBUNIT_ACADEMY)
			{
				player.setLvlJoinedAcademy(player.getLevel());
			}
			member.setPowerGrade(clan.getAffiliationRank(player.getPledgeType()));
			clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListAdd(member), player);
			clan.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.S1_HAS_JOINED_THE_CLAN).addString(player.getName()), new PledgeShowInfoUpdate(clan));
			player.sendPacket(SystemMsg.ENTERED_THE_CLAN);
			player.sendPacket(player.getClan().listAll());
			player.setLeaveClanTime(0);
			player.updatePledgeClass();
			clan.addSkillsQuietly(player);
			player.sendPacket(new PledgeSkillList(clan));
			player.sendSkillList();
			EventHolder.getInstance().findEvent(player);
			player.broadcastCharInfo();
			player.store(false);
		}
		finally
		{
			request.done();
		}
	}
}
