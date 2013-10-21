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
package lineage2.gameserver.tables;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GmListTable
{
	/**
	 * Method getAllGMs.
	 * @return List<Player>
	 */
	public static List<Player> getAllGMs()
	{
		List<Player> gmList = new ArrayList<>();
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (player.isGM())
			{
				gmList.add(player);
			}
		}
		return gmList;
	}
	
	/**
	 * Method getAllVisibleGMs.
	 * @return List<Player>
	 */
	public static List<Player> getAllVisibleGMs()
	{
		List<Player> gmList = new ArrayList<>();
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (player.isGM() && !player.isInvisible())
			{
				gmList.add(player);
			}
		}
		return gmList;
	}
	
	/**
	 * Method sendListToPlayer.
	 * @param player Player
	 */
	public static void sendListToPlayer(Player player)
	{
		List<Player> gmList = getAllVisibleGMs();
		if (gmList.isEmpty())
		{
			player.sendPacket(Msg.THERE_ARE_NOT_ANY_GMS_THAT_ARE_PROVIDING_CUSTOMER_SERVICE_CURRENTLY);
			return;
		}
		player.sendPacket(Msg._GM_LIST_);
		for (Player gm : gmList)
		{
			player.sendPacket(new SystemMessage(SystemMessage.GM_S1).addString(gm.getName()));
		}
	}
	
	/**
	 * Method broadcastToGMs.
	 * @param packet L2GameServerPacket
	 */
	public static void broadcastToGMs(L2GameServerPacket packet)
	{
		for (Player gm : getAllGMs())
		{
			gm.sendPacket(packet);
		}
	}
	
	/**
	 * Method broadcastMessageToGMs.
	 * @param message String
	 */
	public static void broadcastMessageToGMs(String message)
	{
		for (Player gm : getAllGMs())
		{
			gm.sendMessage(message);
		}
	}
}
