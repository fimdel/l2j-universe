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
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MainMachineInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _powerUnits.
	 */
	private int _powerUnits = 3;
	
	/**
	 * Constructor for MainMachineInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MainMachineInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		FortressSiegeEvent siegeEvent = getEvent(FortressSiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		siegeEvent.barrackAction(3, false);
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
		if (_powerUnits != 0)
		{
			return;
		}
		Functions.npcShout(this, NpcString.FORTRESS_POWER_DISABLED);
		FortressSiegeEvent siegeEvent = getEvent(FortressSiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		siegeEvent.spawnAction(FortressSiegeEvent.IN_POWER_UNITS, false);
		siegeEvent.barrackAction(3, true);
		siegeEvent.broadcastTo(SystemMsg.THE_BARRACKS_HAVE_BEEN_SEIZED, SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
		onDecay();
		siegeEvent.checkBarracks();
	}
	
	/**
	 * Method powerOff.
	 * @param powerUnit PowerControlUnitInstance
	 */
	public void powerOff(PowerControlUnitInstance powerUnit)
	{
		FortressSiegeEvent event = getEvent(FortressSiegeEvent.class);
		SpawnExObject exObject = event.getFirstObject(FortressSiegeEvent.IN_POWER_UNITS);
		int machineNumber = -1;
		for (int i = 0; i < 3; i++)
		{
			Spawner spawn = exObject.getSpawns().get(i);
			if (spawn == powerUnit.getSpawn())
			{
				machineNumber = i;
			}
		}
		NpcString msg = null;
		switch (machineNumber)
		{
			case 0:
				msg = NpcString.MACHINE_NO_1_POWER_OFF;
				break;
			case 1:
				msg = NpcString.MACHINE_NO_2_POWER_OFF;
				break;
			case 2:
				msg = NpcString.MACHINE_NO_3_POWER_OFF;
				break;
			default:
				throw new IllegalArgumentException("Wrong spawn at fortress: " + event.getName());
		}
		_powerUnits--;
		Functions.npcShout(this, msg);
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
		NpcHtmlMessage message = new NpcHtmlMessage(player, this);
		if (_powerUnits != 0)
		{
			message.setFile("residence2/fortress/fortress_mainpower002.htm");
		}
		else
		{
			message.setFile("residence2/fortress/fortress_mainpower001.htm");
		}
		player.sendPacket(message);
	}
}
