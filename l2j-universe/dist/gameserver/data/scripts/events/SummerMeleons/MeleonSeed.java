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
package events.SummerMeleons;

import handler.items.ScriptItemHandler;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import npc.model.MeleonInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MeleonSeed extends ScriptItemHandler
{
	/**
	 * @author Mobius
	 */
	static public class DeSpawnScheduleTimerTask extends RunnableImpl
	{
		/**
		 * Field spawnedPlant.
		 */
		SimpleSpawner spawnedPlant = null;
		
		/**
		 * Constructor for DeSpawnScheduleTimerTask.
		 * @param spawn SimpleSpawner
		 */
		public DeSpawnScheduleTimerTask(SimpleSpawner spawn)
		{
			spawnedPlant = spawn;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			spawnedPlant.deleteAll();
		}
	}
	
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		15366,
		15367
	};
	/**
	 * Field _npcIds.
	 */
	private static final int[] _npcIds =
	{
		13271,
		13275
	};
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 * @see lineage2.gameserver.handler.items.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		final Player activeChar = (Player) playable;
		if (activeChar.isInZone(ZoneType.RESIDENCE))
		{
			return false;
		}
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage("�?ел�?з�? взращиват�? арбуз на �?тадионе.");
			return false;
		}
		if (!activeChar.getReflection().isDefault())
		{
			activeChar.sendMessage("�?ел�?з�? взращиват�? арбуз в ин�?тан�?е.");
			return false;
		}
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
		if (template == null)
		{
			return false;
		}
		if (!activeChar.getInventory().destroyItem(item, 1L))
		{
			return false;
		}
		final SimpleSpawner spawn = new SimpleSpawner(template);
		spawn.setLoc(Location.findPointToStay(activeChar, 30, 70));
		final NpcInstance npc = spawn.doSpawn(true);
		npc.setAI(new MeleonAI(npc));
		((MeleonInstance) npc).setSpawner(activeChar);
		ThreadPoolManager.getInstance().schedule(new DeSpawnScheduleTimerTask(spawn), 180000);
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[]
	 * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
