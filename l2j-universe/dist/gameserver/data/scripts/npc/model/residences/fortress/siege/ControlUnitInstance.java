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
package npc.model.residences.fortress.siege;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ControlUnitInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ITEM_ID. (value is 10014)
	 */
	private static final int ITEM_ID = 10014;
	/**
	 * Field COND_CAN_OPEN. (value is 0)
	 */
	private static final int COND_CAN_OPEN = 0;
	/**
	 * Field COND_NO_ITEM. (value is 1)
	 */
	private static final int COND_NO_ITEM = 1;
	/**
	 * Field COND_POWER. (value is 2)
	 */
	private static final int COND_POWER = 2;
	
	/**
	 * Constructor for ControlUnitInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ControlUnitInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		int cond = getCond(player);
		if (cond == COND_CAN_OPEN)
		{
			if (player.consumeItem(ITEM_ID, 1))
			{
				FortressSiegeEvent event = getEvent(FortressSiegeEvent.class);
				event.doorAction(FortressSiegeEvent.MACHINE_DOORS, true);
				event.spawnAction(FortressSiegeEvent.OUT_POWER_UNITS, false);
			}
			else
			{
				showChatWindow(player, "residence2/fortress/fortress_controller002.htm");
			}
		}
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
		int cond = getCond(player);
		switch (cond)
		{
			case COND_CAN_OPEN:
				showChatWindow(player, "residence2/fortress/fortress_controller001.htm");
				break;
			case COND_NO_ITEM:
				showChatWindow(player, "residence2/fortress/fortress_controller002.htm");
				break;
			case COND_POWER:
				showChatWindow(player, "residence2/fortress/fortress_controller003.htm");
				break;
		}
	}
	
	/**
	 * Method getCond.
	 * @param player Player
	 * @return int
	 */
	private int getCond(Player player)
	{
		FortressSiegeEvent event = getEvent(FortressSiegeEvent.class);
		if (event == null)
		{
			return COND_POWER;
		}
		SpawnExObject object = event.getFirstObject(FortressSiegeEvent.OUT_POWER_UNITS);
		boolean allPowerDisabled = true;
		for (int i = 0; i < 4; i++)
		{
			Spawner spawn = object.getSpawns().get(i);
			if (spawn.getFirstSpawned() != null)
			{
				allPowerDisabled = false;
			}
		}
		if (allPowerDisabled)
		{
			if (player.getInventory().getCountOf(ITEM_ID) > 0)
			{
				return COND_CAN_OPEN;
			}
			return COND_NO_ITEM;
		}
		return COND_POWER;
	}
}
