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
package lineage2.gameserver.model.entity.events.actions;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.events.EventAction;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.MapUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NpcSayAction implements EventAction
{
	/**
	 * Field _npcId.
	 */
	private final int _npcId;
	/**
	 * Field _range.
	 */
	private final int _range;
	/**
	 * Field _chatType.
	 */
	private final ChatType _chatType;
	/**
	 * Field _text.
	 */
	private final NpcString _text;
	
	/**
	 * Constructor for NpcSayAction.
	 * @param npcId int
	 * @param range int
	 * @param type ChatType
	 * @param string NpcString
	 */
	public NpcSayAction(int npcId, int range, ChatType type, NpcString string)
	{
		_npcId = npcId;
		_range = range;
		_chatType = type;
		_text = string;
	}
	
	/**
	 * Method call.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.EventAction#call(GlobalEvent)
	 */
	@Override
	public void call(GlobalEvent event)
	{
		NpcInstance npc = GameObjectsStorage.getByNpcId(_npcId);
		if (npc == null)
		{
			return;
		}
		if (_range <= 0)
		{
			int rx = MapUtils.regionX(npc);
			int ry = MapUtils.regionY(npc);
			int offset = Config.SHOUT_OFFSET;
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				if (npc.getReflection() != player.getReflection())
				{
					continue;
				}
				int tx = MapUtils.regionX(player);
				int ty = MapUtils.regionY(player);
				if ((tx >= (rx - offset)) && (tx <= (rx + offset)) && (ty >= (ry - offset)) && (ty <= (ry + offset)))
				{
					packet(npc, player);
				}
			}
		}
		else
		{
			for (Player player : World.getAroundPlayers(npc, _range, Math.max(_range / 2, 200)))
			{
				if (npc.getReflection() == player.getReflection())
				{
					packet(npc, player);
				}
			}
		}
	}
	
	/**
	 * Method packet.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	private void packet(NpcInstance npc, Player player)
	{
		player.sendPacket(new NpcSay(npc, _chatType, _text));
	}
}
