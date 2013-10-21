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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.PetInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestChangePetName extends L2GameClientPacket
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
		_name = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		PetInstance pet = activeChar.getSummonList().getPet();
		if (pet == null)
		{
			return;
		}
		if (pet.isDefaultName())
		{
			if ((_name.length() < 1) || (_name.length() > 8))
			{
				sendPacket(Msg.YOUR_PETS_NAME_CAN_BE_UP_TO_8_CHARACTERS);
				return;
			}
			pet.setName(_name);
			pet.broadcastCharInfo();
			pet.updateControlItem();
		}
	}
}
