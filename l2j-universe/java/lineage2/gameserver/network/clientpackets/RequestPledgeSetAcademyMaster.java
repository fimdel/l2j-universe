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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.PledgeReceiveMemberInfo;
import lineage2.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPledgeSetAcademyMaster extends L2GameClientPacket
{
	/**
	 * Field _mode.
	 */
	private int _mode;
	/**
	 * Field _sponsorName.
	 */
	private String _sponsorName;
	/**
	 * Field _apprenticeName.
	 */
	private String _apprenticeName;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_mode = readD();
		_sponsorName = readS(16);
		_apprenticeName = readS(16);
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
			return;
		}
		if ((activeChar.getClanPrivileges() & Clan.CP_CL_APPRENTICE) == Clan.CP_CL_APPRENTICE)
		{
			UnitMember sponsor = activeChar.getClan().getAnyMember(_sponsorName);
			UnitMember apprentice = activeChar.getClan().getAnyMember(_apprenticeName);
			if ((sponsor != null) && (apprentice != null))
			{
				if ((apprentice.getPledgeType() != Clan.SUBUNIT_ACADEMY) || (sponsor.getPledgeType() == Clan.SUBUNIT_ACADEMY))
				{
					return;
				}
				if (_mode == 1)
				{
					if (sponsor.hasApprentice())
					{
						activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestOustAlly.MemberAlreadyHasApprentice", activeChar));
						return;
					}
					if (apprentice.hasSponsor())
					{
						activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestOustAlly.ApprenticeAlreadyHasSponsor", activeChar));
						return;
					}
					sponsor.setApprentice(apprentice.getObjectId());
					clan.broadcastToOnlineMembers(new PledgeShowMemberListUpdate(apprentice));
					clan.broadcastToOnlineMembers(new SystemMessage(SystemMessage.S2_HAS_BEEN_DESIGNATED_AS_THE_APPRENTICE_OF_CLAN_MEMBER_S1).addString(sponsor.getName()).addString(apprentice.getName()));
				}
				else
				{
					if (!sponsor.hasApprentice())
					{
						activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestOustAlly.MemberHasNoApprentice", activeChar));
						return;
					}
					sponsor.setApprentice(0);
					clan.broadcastToOnlineMembers(new PledgeShowMemberListUpdate(apprentice));
					clan.broadcastToOnlineMembers(new SystemMessage(SystemMessage.S2_CLAN_MEMBER_S1S_APPRENTICE_HAS_BEEN_REMOVED).addString(sponsor.getName()).addString(apprentice.getName()));
				}
				if (apprentice.isOnline())
				{
					apprentice.getPlayer().broadcastCharInfo();
				}
				activeChar.sendPacket(new PledgeReceiveMemberInfo(sponsor));
			}
		}
		else
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestOustAlly.NoMasterRights", activeChar));
		}
	}
}
