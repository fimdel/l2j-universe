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
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPledgeSetMemberPowerGrade extends L2GameClientPacket
{
	/**
	 * Field _powerGrade.
	 */
	private int _powerGrade;
	/**
	 * Field _name.
	 */
	private String _name;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_name = readS(16);
		_powerGrade = readD();
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
		if ((_powerGrade < Clan.RANK_FIRST) || (_powerGrade > Clan.RANK_LAST))
		{
			return;
		}
		Clan clan = activeChar.getClan();
		if (clan == null)
		{
			return;
		}
		if ((activeChar.getClanPrivileges() & Clan.CP_CL_MANAGE_RANKS) == Clan.CP_CL_MANAGE_RANKS)
		{
			UnitMember member = activeChar.getClan().getAnyMember(_name);
			if (member != null)
			{
				if (Clan.isAcademy(member.getPledgeType()))
				{
					activeChar.sendMessage("You cannot change academy member grade.");
					return;
				}
				if (_powerGrade > 5)
				{
					member.setPowerGrade(clan.getAffiliationRank(member.getPledgeType()));
				}
				else
				{
					member.setPowerGrade(_powerGrade);
				}
				if (member.isOnline())
				{
					member.getPlayer().sendUserInfo();
				}
			}
			else
			{
				activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestPledgeSetMemberPowerGrade.NotBelongClan", activeChar));
			}
		}
		else
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestPledgeSetMemberPowerGrade.HaveNotAuthority", activeChar));
		}
	}
}
