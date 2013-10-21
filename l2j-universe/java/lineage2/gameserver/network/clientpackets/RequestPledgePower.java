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
import lineage2.gameserver.network.serverpackets.ManagePledgePower;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPledgePower extends L2GameClientPacket
{
	/**
	 * Field _rank.
	 */
	private int _rank;
	/**
	 * Field _action.
	 */
	private int _action;
	/**
	 * Field _privs.
	 */
	private int _privs;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_rank = readD();
		_action = readD();
		if (_action == 2)
		{
			_privs = readD();
		}
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
		if (_action == 2)
		{
			if ((_rank < Clan.RANK_FIRST) || (_rank > Clan.RANK_LAST))
			{
				return;
			}
			if ((activeChar.getClan() != null) && ((activeChar.getClanPrivileges() & Clan.CP_CL_MANAGE_RANKS) == Clan.CP_CL_MANAGE_RANKS))
			{
				if (_rank == 9)
				{
					_privs = (_privs & Clan.CP_CL_WAREHOUSE_SEARCH) + (_privs & Clan.CP_CH_ENTRY_EXIT) + (_privs & Clan.CP_CS_ENTRY_EXIT) + (_privs & Clan.CP_CH_USE_FUNCTIONS) + (_privs & Clan.CP_CS_USE_FUNCTIONS);
				}
				activeChar.getClan().setRankPrivs(_rank, _privs);
				activeChar.getClan().updatePrivsForRank(_rank);
			}
		}
		else if (activeChar.getClan() != null)
		{
			activeChar.sendPacket(new ManagePledgePower(activeChar, _action, _rank));
		}
		else
		{
			activeChar.sendActionFailed();
		}
	}
}
