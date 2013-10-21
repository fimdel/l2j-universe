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
package npc.model.events;

import java.util.List;
import java.util.StringTokenizer;

import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.KrateisCubeEvent;
import lineage2.gameserver.model.entity.events.impl.KrateisCubeRunnerEvent;
import lineage2.gameserver.model.entity.events.objects.KrateisCubePlayerObject;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class KrateisCubeManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for KrateisCubeManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public KrateisCubeManagerInstance(int objectId, NpcTemplate template)
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
		if (command.startsWith("Kratei_UnRegister"))
		{
			KrateisCubeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 2);
			for (KrateisCubeEvent cubeEvent : runnerEvent.getCubes())
			{
				List<KrateisCubePlayerObject> list = cubeEvent.getObjects(KrateisCubeEvent.REGISTERED_PLAYERS);
				KrateisCubePlayerObject krateisCubePlayer = cubeEvent.getRegisteredPlayer(player);
				if (krateisCubePlayer != null)
				{
					list.remove(krateisCubePlayer);
				}
			}
			showChatWindow(player, 4);
		}
		else if (command.startsWith("Kratei_TryRegister"))
		{
			KrateisCubeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 2);
			if (runnerEvent.isRegistrationOver())
			{
				if (runnerEvent.isInProgress())
				{
					showChatWindow(player, 3);
				}
				else
				{
					showChatWindow(player, 7);
				}
			}
			else
			{
				if (player.getLevel() < 70)
				{
					showChatWindow(player, 2);
				}
				else
				{
					showChatWindow(player, 5);
				}
			}
		}
		else if (command.startsWith("Kratei_SeeList"))
		{
			if (player.getLevel() < 70)
			{
				showChatWindow(player, 2);
			}
			else
			{
				showChatWindow(player, 5);
			}
		}
		else if (command.startsWith("Kratei_Register"))
		{
			if (Olympiad.isRegistered(player) || HandysBlockCheckerManager.isRegistered(player))
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_BE_SIMULTANEOUSLY_REGISTERED_FOR_PVP_MATCHES_SUCH_AS_THE_OLYMPIAD_UNDERGROUND_COLISEUM_AERIAL_CLEFT_KRATEIS_CUBE_AND_HANDYS_BLOCK_CHECKERS);
				return;
			}
			if (player.isCursedWeaponEquipped())
			{
				player.sendPacket(SystemMsg.YOU_CANNOT_REGISTER_WHILE_IN_POSSESSION_OF_A_CURSED_WEAPON);
				return;
			}
			StringTokenizer t = new StringTokenizer(command);
			if (t.countTokens() < 2)
			{
				return;
			}
			t.nextToken();
			KrateisCubeEvent cubeEvent = EventHolder.getInstance().getEvent(EventType.PVP_EVENT, Integer.parseInt(t.nextToken()));
			if (cubeEvent == null)
			{
				return;
			}
			if ((player.getLevel() < cubeEvent.getMinLevel()) || (player.getLevel() > cubeEvent.getMaxLevel()))
			{
				showChatWindow(player, 2);
				return;
			}
			List<KrateisCubePlayerObject> list = cubeEvent.getObjects(KrateisCubeEvent.REGISTERED_PLAYERS);
			KrateisCubePlayerObject krateisCubePlayer = cubeEvent.getRegisteredPlayer(player);
			if (krateisCubePlayer != null)
			{
				showChatWindow(player, 6);
				return;
			}
			if (list.size() >= 25)
			{
				showChatWindow(player, 9);
			}
			else
			{
				cubeEvent.addObject(KrateisCubeEvent.REGISTERED_PLAYERS, new KrateisCubePlayerObject(player));
				showChatWindow(player, 8);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
