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
package services;

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;
import quests._240_ImTheOnlyOneYouCanTrust;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleToStakatoNest extends Functions
{
	/**
	 * Field teleports.
	 */
	private final static Location[] teleports =
	{
		new Location(80456, -52322, -5640),
		new Location(88718, -46214, -4640),
		new Location(87464, -54221, -5120),
		new Location(80848, -49426, -5128),
		new Location(87682, -43291, -4128)
	};
	
	/**
	 * Method list.
	 */
	public void list()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		QuestState qs = player.getQuestState(_240_ImTheOnlyOneYouCanTrust.class);
		if ((qs == null) || !qs.isCompleted())
		{
			show("scripts/services/TeleToStakatoNest-no.htm", player);
			return;
		}
		show("scripts/services/TeleToStakatoNest.htm", player);
	}
	
	/**
	 * Method teleTo.
	 * @param args String[]
	 */
	public void teleTo(String[] args)
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (args.length != 1)
		{
			return;
		}
		Location loc = teleports[Integer.parseInt(args[0]) - 1];
		Party party = player.getParty();
		if (party == null)
		{
			player.teleToLocation(loc);
		}
		else
		{
			for (Player member : party.getPartyMembers())
			{
				if ((member != null) && member.isInRange(player, 1000))
				{
					member.teleToLocation(loc);
				}
			}
		}
	}
}
