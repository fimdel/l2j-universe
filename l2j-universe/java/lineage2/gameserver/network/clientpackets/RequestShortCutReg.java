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
import lineage2.gameserver.model.actor.instances.player.ShortCut;
import lineage2.gameserver.network.serverpackets.ShortCutRegister;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestShortCutReg extends L2GameClientPacket
{
	/**
	 * Field _characterType. Field _lvl. Field _page. Field _slot. Field _id. Field _type.
	 */
	private int _type, _id, _slot, _page, _lvl, _characterType;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_type = readD();
		int slot = readD();
		_id = readD();
		_lvl = readD();
		_characterType = readD();
		_slot = slot % 12;
		_page = slot / 12;
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
		if ((_page < 0) || (_page > ShortCut.PAGE_MAX))
		{
			activeChar.sendActionFailed();
			return;
		}
		ShortCut shortCut = new ShortCut(_slot, _page, _type, _id, _lvl, _characterType);
		activeChar.sendPacket(new ShortCutRegister(activeChar, shortCut));
		activeChar.registerShortCut(shortCut);
	}
}
