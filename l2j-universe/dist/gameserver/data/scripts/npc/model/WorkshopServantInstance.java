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
package npc.model;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WorkshopServantInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field medals.
	 */
	private static final int[] medals =
	{
		10427,
		10428,
		10429,
		10430,
		10431,
	};
	/**
	 * Field phrases.
	 */
	private static final String[] phrases =
	{
		"We won't let you go with this knowledge! Die!",
		"Mysterious Agent has failed! Kill him!",
		"Mates! Attack those fools!",
	};
	
	/**
	 * Constructor for WorkshopServantInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public WorkshopServantInstance(int objectId, NpcTemplate template)
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
		if (command.startsWith("getmedals"))
		{
			for (int medal : medals)
			{
				if (player.getInventory().getItemByItemId(medal) != null)
				{
					player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Ingenious Contraption:<br><br>You already have one of the medals. Cannot proceed."));
					return;
				}
			}
			Functions.addItem(player, medals[Rnd.get(0, 4)], 1);
			player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Ingenious Contraption:<br><br>The medal for access to Anomic Founrdy has been given."));
		}
		else if (command.startsWith("requestteleport"))
		{
			player.teleToLocation(-12220, 279713, -13595);
		}
		else if (command.startsWith("teletoroof"))
		{
			player.teleToLocation(22616, 244888, 11062);
		}
		else if (command.startsWith("teleto7thfloor"))
		{
			player.teleToLocation(-12520, 280120, -11649);
		}
		else if (command.startsWith("acceptjob"))
		{
			broadcastPacket(new MagicSkillUse(this, player, 5526, 1, 0, 0));
			player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(5526, 1));
			player.teleToLocation(22616, 244888, 11062);
		}
		else if (command.startsWith("rejectjob"))
		{
			for (NpcInstance challenger : World.getAroundNpc(this, 600, 300))
			{
				challenger.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 5000);
				switch (challenger.getNpcId())
				{
					case 25600:
						Functions.npcSay(challenger, phrases[0]);
						break;
					case 25601:
						Functions.npcSay(challenger, phrases[1]);
						break;
					case 25602:
						Functions.npcSay(challenger, phrases[2]);
						break;
					default:
						break;
				}
			}
			Functions.npcSay(this, "Oh...");
			doDie(null);
		}
		else if (command.startsWith("tryanomicentry"))
		{
			if (!player.isInParty())
			{
				player.sendPacket(Msg.YOU_ARE_NOT_CURRENTLY_IN_A_PARTY_SO_YOU_CANNOT_ENTER);
				return;
			}
			Party party = player.getParty();
			if (!party.isLeader(player))
			{
				player.sendPacket(Msg.ONLY_A_PARTY_LEADER_CAN_TRY_TO_ENTER);
				return;
			}
			for (Player p : party.getPartyMembers())
			{
				if (!this.isInRange(p, 500))
				{
					player.sendPacket(Msg.ITS_TOO_FAR_FROM_THE_NPC_TO_WORK);
					return;
				}
			}
			for (int i = 0; i < medals.length; i++)
			{
				if (!hasItem(party, medals[i]))
				{
					player.sendMessage("In order to enter the Anomic Foundry your party should be carrying all 5 medals of Tully");
					return;
				}
			}
			party.Teleport(new Location(25512, 247240, -2656));
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String pom;
		if (val == 0)
		{
			pom = String.valueOf(npcId);
		}
		else
		{
			pom = npcId + "-" + val;
		}
		if (getNpcId() == 32372)
		{
			if (this.isInZone("[tully5]"))
			{
				return "default/32372-floor.htm";
			}
		}
		return "default/" + pom + ".htm";
	}
	
	/**
	 * Method hasItem.
	 * @param party Party
	 * @param itemId int
	 * @return boolean
	 */
	private boolean hasItem(Party party, int itemId)
	{
		for (Player p : party.getPartyMembers())
		{
			if (p.getInventory().getItemByItemId(itemId) != null)
			{
				return true;
			}
		}
		return false;
	}
}
