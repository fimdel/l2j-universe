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
package npc.model.residences.castle;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.CastleSiegeInfo;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleMessengerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for CastleMessengerInstance.
	 * @param objectID int
	 * @param template NpcTemplate
	 */
	public CastleMessengerInstance(int objectID, NpcTemplate template)
	{
		super(objectID, template);
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		Castle castle = getCastle();
		if (player.isCastleLord(castle.getId()))
		{
			if (castle.getSiegeEvent().isInProgress())
			{
				showChatWindow(player, "residence2/castle/sir_tyron021.htm");
			}
			else
			{
				showChatWindow(player, "residence2/castle/sir_tyron007.htm");
			}
		}
		else if (castle.getSiegeEvent().isInProgress())
		{
			showChatWindow(player, "residence2/castle/sir_tyron021.htm");
		}
		else
		{
			player.sendPacket(new CastleSiegeInfo(castle, player));
		}
	}
}
