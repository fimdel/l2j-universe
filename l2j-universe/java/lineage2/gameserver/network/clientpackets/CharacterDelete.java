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
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.CharacterDeleteFail;
import lineage2.gameserver.network.serverpackets.CharacterDeleteSuccess;
import lineage2.gameserver.network.serverpackets.CharacterSelectionInfo;
import lineage2.gameserver.network.serverpackets.ExLoginVitalityEffectInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterDelete extends L2GameClientPacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CharacterDelete.class);
	/**
	 * Field _charSlot.
	 */
	private int _charSlot;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_charSlot = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		int clan = clanStatus();
		int online = onlineStatus();
		if ((clan > 0) || (online > 0))
		{
			if (clan == 2)
			{
				sendPacket(new CharacterDeleteFail(CharacterDeleteFail.REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED));
			}
			else if (clan == 1)
			{
				sendPacket(new CharacterDeleteFail(CharacterDeleteFail.REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER));
			}
			else if (online > 0)
			{
				sendPacket(new CharacterDeleteFail(CharacterDeleteFail.REASON_DELETION_FAILED));
			}
			return;
		}
		GameClient client = getClient();
		try
		{
			if (Config.DELETE_DAYS == 0)
			{
				client.deleteChar(_charSlot);
			}
			else
			{
				client.markToDeleteChar(_charSlot);
			}
		}
		catch (Exception e)
		{
			_log.error("Error:", e);
		}
		sendPacket(new CharacterDeleteSuccess());
		CharacterSelectionInfo cl = new CharacterSelectionInfo(client.getLogin(), client.getSessionKey().playOkID1);
		ExLoginVitalityEffectInfo vl = new ExLoginVitalityEffectInfo(cl.getCharInfo());
		sendPacket(cl, vl);
		client.setCharSelection(cl.getCharInfo());
	}
	
	/**
	 * Method clanStatus.
	 * @return int
	 */
	private int clanStatus()
	{
		int obj = getClient().getObjectIdForSlot(_charSlot);
		if (obj == -1)
		{
			return 0;
		}
		if (mysql.simple_get_int("clanid", "characters", "obj_Id=" + obj) > 0)
		{
			if (mysql.simple_get_int("leader_id", "clan_subpledges", "leader_id=" + obj + " AND type = " + Clan.SUBUNIT_MAIN_CLAN) > 0)
			{
				return 2;
			}
			return 1;
		}
		return 0;
	}
	
	/**
	 * Method onlineStatus.
	 * @return int
	 */
	private int onlineStatus()
	{
		int obj = getClient().getObjectIdForSlot(_charSlot);
		if (obj == -1)
		{
			return 0;
		}
		if (mysql.simple_get_int("online", "characters", "obj_Id=" + obj) > 0)
		{
			return 1;
		}
		return 0;
	}
}
