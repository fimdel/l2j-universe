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
package lineage2.gameserver.model.items.listeners;

import lineage2.gameserver.listener.inventory.OnEquipListener;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.stats.funcs.Func;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class StatsListener implements OnEquipListener
{
	/**
	 * Field _instance.
	 */
	private static final StatsListener _instance = new StatsListener();
	
	/**
	 * Method getInstance.
	 * @return StatsListener
	 */
	public static StatsListener getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method onUnequip.
	 * @param slot int
	 * @param item ItemInstance
	 * @param actor Playable
	 * @see lineage2.gameserver.listener.inventory.OnEquipListener#onUnequip(int, ItemInstance, Playable)
	 */
	@Override
	public void onUnequip(int slot, ItemInstance item, Playable actor)
	{
		actor.removeStatsOwner(item);
		actor.updateStats();
	}
	
	/**
	 * Method onEquip.
	 * @param slot int
	 * @param item ItemInstance
	 * @param actor Playable
	 * @see lineage2.gameserver.listener.inventory.OnEquipListener#onEquip(int, ItemInstance, Playable)
	 */
	@Override
	public void onEquip(int slot, ItemInstance item, Playable actor)
	{
		Func[] funcs = item.getStatFuncs();
		actor.addStatFuncs(funcs);
		actor.updateStats();
	}
}
