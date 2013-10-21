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

import lineage2.gameserver.instancemanager.FindPartyManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAcceptWaitingSubstitute extends L2GameClientPacket
{
	/**
	 * Field willJoin.
	 */
	boolean willJoin;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		willJoin = readD() == 1;
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (willJoin)
		{
			if (activeChar.getParty() == null)
			{
				FindPartyManager.getInstance().removeLookingForParty(activeChar);
			}
			if (activeChar.getPlayerForChange() != null)
			{
				Player player = activeChar.getPlayerForChange();
				FindPartyManager.getInstance().removeChangeThisPlayer(player);
				if (player.getParty() != null)
				{
					activeChar.joinParty(player.getParty());
					player.getParty().removePartyMember(player, false);
					activeChar.setPlayerForChange(null);
					Player leader = activeChar.getParty().getPartyLeader();
					if (leader != null)
					{
						activeChar.teleToLocation(leader.getX(), leader.getY(), leader.getZ());
						for (Player member : activeChar.getParty().getPartyMembers())
						{
							SkillTable.getInstance().getInfo(14534, 1).getEffects(member, member, true, true);
							SkillTable.getInstance().getInfo(14535, 1).getEffects(member, member, true, true);
						}
					}
				}
			}
		}
	}
}
