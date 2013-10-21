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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestOustPartyMember extends L2GameClientPacket
{
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
		Party party = activeChar.getParty();
		if ((party == null) || !activeChar.getParty().isLeader(activeChar))
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage("Вы не можете �?ейча�? выйти из группы.");
			return;
		}
		Player member = party.getPlayerByName(_name);
		if (member == activeChar)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (member == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		Reflection r = party.getReflection();
		if (r != null)
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestOustPartyMember.CantOustInDungeon", activeChar));
		}
		else
		{
			party.removePartyMember(member, true);
		}
	}
}
