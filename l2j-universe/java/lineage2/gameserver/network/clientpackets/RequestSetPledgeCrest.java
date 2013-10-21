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

import lineage2.gameserver.cache.CrestCache;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSetPledgeCrest extends L2GameClientPacket
{
	/**
	 * Field _length.
	 */
	private int _length;
	/**
	 * Field _data.
	 */
	private byte[] _data;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_length = readD();
		if ((_length == CrestCache.CREST_SIZE) && (_length == _buf.remaining()))
		{
			_data = new byte[_length];
			readB(_data);
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
		Clan clan = activeChar.getClan();
		if ((activeChar.getClanPrivileges() & Clan.CP_CL_EDIT_CREST) == Clan.CP_CL_EDIT_CREST)
		{
			if (clan.getLevel() < 3)
			{
				activeChar.sendPacket(Msg.CLAN_CREST_REGISTRATION_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3);
				return;
			}
			int crestId = 0;
			if (_data != null)
			{
				crestId = CrestCache.getInstance().savePledgeCrest(clan.getClanId(), _data);
			}
			else if (clan.hasCrest())
			{
				CrestCache.getInstance().removePledgeCrest(clan.getClanId());
			}
			clan.setCrestId(crestId);
			clan.broadcastClanStatus(false, true, false);
		}
	}
}
