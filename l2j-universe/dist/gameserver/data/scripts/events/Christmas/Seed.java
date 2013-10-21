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
package events.Christmas;

import handler.items.ScriptItemHandler;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Seed extends ScriptItemHandler
{
	/**
	 * @author Mobius
	 */
	static public class DeSpawnScheduleTimerTask extends RunnableImpl
	{
		/**
		 * Field spawnedTree.
		 */
		SimpleSpawner spawnedTree = null;
		
		/**
		 * Constructor for DeSpawnScheduleTimerTask.
		 * @param spawn SimpleSpawner
		 */
		public DeSpawnScheduleTimerTask(SimpleSpawner spawn)
		{
			spawnedTree = spawn;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			spawnedTree.deleteAll();
		}
	}
	
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		5560,
		5561
	};
	/**
	 * Field _npcIds.
	 */
	private static final int[] _npcIds =
	{
		13006,
		13007
	};
	/**
	 * Field DESPAWN_TIME. (value is 600000)
	 */
	private static final int DESPAWN_TIME = 600000;
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean * @see lineage2.gameserver.handler.items.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		final Player activeChar = (Player) playable;
		NpcTemplate template = null;
		final int itemId = item.getItemId();
		for (int i = 0; i < _itemIds.length; i++)
		{
			if (_itemIds[i] == itemId)
			{
				template = NpcHolder.getInstance().getTemplate(_npcIds[i]);
				break;
			}
		}
		for (NpcInstance npc : World.getAroundNpc(activeChar, 300, 200))
		{
			if ((npc.getNpcId() == _npcIds[0]) || (npc.getNpcId() == _npcIds[1]))
			{
				activeChar.sendPacket(new SystemMessage2(SystemMsg.SINCE_S1_ALREADY_EXISTS_NEARBY_YOU_CANNOT_SUMMON_IT_AGAIN).addName(npc));
				return false;
			}
		}
		if (World.getAroundNpc(activeChar, 100, 200).size() > 0)
		{
			activeChar.sendPacket(Msg.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION);
			return false;
		}
		if (template == null)
		{
			return false;
		}
		if (!activeChar.getInventory().destroyItem(item, 1L))
		{
			return false;
		}
		final SimpleSpawner spawn = new SimpleSpawner(template);
		spawn.setLoc(activeChar.getLoc());
		final NpcInstance npc = spawn.doSpawn(false);
		npc.setTitle(activeChar.getName());
		spawn.respawnNpc(npc);
		if (itemId == 5561)
		{
			npc.setAI(new ctreeAI(npc));
		}
		ThreadPoolManager.getInstance().schedule(new DeSpawnScheduleTimerTask(spawn), (activeChar.isInPeaceZone() ? DESPAWN_TIME / 3 : DESPAWN_TIME));
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
