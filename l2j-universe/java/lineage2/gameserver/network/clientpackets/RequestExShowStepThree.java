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
import lineage2.gameserver.model.petition.PetitionMainGroup;
import lineage2.gameserver.model.petition.PetitionSubGroup;
import lineage2.gameserver.network.serverpackets.ExResponseShowContents;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExShowStepThree extends L2GameClientPacket
{
	/**
	 * Field _subId.
	 */
	private int _subId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_subId = readC();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if ((player == null) || !Config.EX_NEW_PETITION_SYSTEM)
		{
			return;
		}
		PetitionMainGroup group = player.getPetitionGroup();
		if (group == null)
		{
			return;
		}
		PetitionSubGroup subGroup = group.getSubGroup(_subId);
		if (subGroup == null)
		{
			return;
		}
		player.sendPacket(new ExResponseShowContents(subGroup.getDescription(player.getLanguage())));
	}
}
