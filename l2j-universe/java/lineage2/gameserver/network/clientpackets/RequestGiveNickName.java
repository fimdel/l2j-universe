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

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.network.serverpackets.NickNameChanged;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestGiveNickName extends L2GameClientPacket
{
	/**
	 * Field _target.
	 */
	private String _target;
	/**
	 * Field _title.
	 */
	private String _title;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_target = readS(Config.CNAME_MAXLEN);
		_title = readS();
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
		if (!_title.isEmpty() && !Util.isMatchingRegexp(_title, Config.CLAN_TITLE_TEMPLATE))
		{
			activeChar.sendMessage("Incorrect title.");
			return;
		}
		if (activeChar.isNoble() && _target.matches(activeChar.getName()))
		{
			activeChar.setTitle(_title);
			activeChar.sendPacket(Msg.TITLE_HAS_CHANGED);
			activeChar.broadcastPacket(new NickNameChanged(activeChar));
			return;
		}
		else if ((activeChar.getClanPrivileges() & Clan.CP_CL_MANAGE_TITLES) != Clan.CP_CL_MANAGE_TITLES)
		{
			return;
		}
		if (activeChar.getClan().getLevel() < 3)
		{
			activeChar.sendPacket(Msg.TITLE_ENDOWMENT_IS_ONLY_POSSIBLE_WHEN_CLANS_SKILL_LEVELS_ARE_ABOVE_3);
			return;
		}
		UnitMember member = activeChar.getClan().getAnyMember(_target);
		if (member != null)
		{
			member.setTitle(_title);
			if (member.isOnline())
			{
				member.getPlayer().sendPacket(Msg.TITLE_HAS_CHANGED);
				member.getPlayer().sendChanges();
			}
		}
		else
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestGiveNickName.NotInClan", activeChar));
		}
	}
}
