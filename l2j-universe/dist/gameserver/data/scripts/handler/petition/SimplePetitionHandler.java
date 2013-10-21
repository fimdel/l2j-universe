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
package handler.petition;

import lineage2.gameserver.handler.petition.IPetitionHandler;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SimplePetitionHandler implements IPetitionHandler
{
	/**
	 * Constructor for SimplePetitionHandler.
	 */
	public SimplePetitionHandler()
	{
	}
	
	/**
	 * Method handle.
	 * @param player Player
	 * @param id int
	 * @param txt String
	 * @see lineage2.gameserver.handler.petition.IPetitionHandler#handle(Player, int, String)
	 */
	@Override
	public void handle(Player player, int id, String txt)
	{
		player.sendMessage(txt);
	}
}
