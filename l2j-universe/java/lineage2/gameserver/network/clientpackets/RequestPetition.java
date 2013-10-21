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
import lineage2.gameserver.instancemanager.PetitionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.petition.PetitionMainGroup;
import lineage2.gameserver.model.petition.PetitionSubGroup;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestPetition extends L2GameClientPacket
{
	/**
	 * Field _content.
	 */
	private String _content;
	/**
	 * Field _type.
	 */
	private int _type;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_content = readS();
		_type = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if (Config.EX_NEW_PETITION_SYSTEM)
		{
			PetitionMainGroup group = player.getPetitionGroup();
			if (group == null)
			{
				return;
			}
			PetitionSubGroup subGroup = group.getSubGroup(_type);
			if (subGroup == null)
			{
				return;
			}
			subGroup.getHandler().handle(player, _type, _content);
		}
		else
		{
			PetitionManager.getInstance().handle(player, _type, _content);
		}
	}
}
