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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.GameClient.GameClientState;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.CharSelected;
import lineage2.gameserver.utils.AutoBan;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterSelected extends L2GameClientPacket
{
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
		GameClient client = getClient();
		if (Config.SECOND_AUTH_ENABLED && !client.getSecondaryAuth().isAuthed())
		{
			client.getSecondaryAuth().openDialog();
			return;
		}
		if (client.getActiveChar() != null)
		{
			return;
		}
		int objId = client.getObjectIdForSlot(_charSlot);
		if (AutoBan.isBanned(objId))
		{
			sendPacket(ActionFail.STATIC);
			return;
		}
		Player activeChar = client.loadCharFromDisk(_charSlot);
		if (activeChar == null)
		{
			sendPacket(ActionFail.STATIC);
			return;
		}
		if (activeChar.getAccessLevel() < 0)
		{
			activeChar.setAccessLevel(0);
		}
		client.setState(GameClientState.IN_GAME);
		sendPacket(new CharSelected(activeChar, client.getSessionKey().playOkID1));
	}
}
