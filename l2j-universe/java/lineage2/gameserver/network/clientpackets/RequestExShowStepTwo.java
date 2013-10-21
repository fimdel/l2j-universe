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
import lineage2.gameserver.data.xml.holder.PetitionGroupHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.petition.PetitionMainGroup;
import lineage2.gameserver.network.serverpackets.ExResponseShowStepTwo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExShowStepTwo extends L2GameClientPacket
{
	/**
	 * Field _petitionGroupId.
	 */
	private int _petitionGroupId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_petitionGroupId = readC();
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
		PetitionMainGroup group = PetitionGroupHolder.getInstance().getPetitionGroup(_petitionGroupId);
		if (group == null)
		{
			return;
		}
		player.setPetitionGroup(group);
		player.sendPacket(new ExResponseShowStepTwo(player, group));
	}
}
