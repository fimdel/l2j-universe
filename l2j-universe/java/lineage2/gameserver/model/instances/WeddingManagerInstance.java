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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.instancemanager.CoupleManager;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Couple;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WeddingManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for WeddingManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public WeddingManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
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
		String filename = "wedding/start.htm";
		String replace = "";
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile(filename);
		html.replace("%replace%", replace);
		html.replace("%npcname%", getName());
		player.sendPacket(html);
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
		String filename = "wedding/start.htm";
		String replace = "";
		if (player.getPartnerId() == 0)
		{
			filename = "wedding/nopartner.htm";
			sendHtmlMessage(player, filename, replace);
			return;
		}
		Player ptarget = GameObjectsStorage.getPlayer(player.getPartnerId());
		if ((ptarget == null) || !ptarget.isOnline())
		{
			filename = "wedding/notfound.htm";
			sendHtmlMessage(player, filename, replace);
			return;
		}
		else if (player.isMaried())
		{
			filename = "wedding/already.htm";
			sendHtmlMessage(player, filename, replace);
			return;
		}
		else if (command.startsWith("AcceptWedding"))
		{
			player.setMaryAccepted(true);
			Couple couple = CoupleManager.getInstance().getCouple(player.getCoupleId());
			couple.marry();
			player.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2WeddingManagerMessage", player));
			player.setMaried(true);
			player.setMaryRequest(false);
			ptarget.sendMessage(new CustomMessage("lineage2.gameserver.model.instances.L2WeddingManagerMessage", ptarget));
			ptarget.setMaried(true);
			ptarget.setMaryRequest(false);
			player.broadcastPacket(new MagicSkillUse(player, player, 2230, 1, 1, 0));
			ptarget.broadcastPacket(new MagicSkillUse(ptarget, ptarget, 2230, 1, 1, 0));
			player.broadcastPacket(new MagicSkillUse(player, player, 025, 1, 1, 0));
			ptarget.broadcastPacket(new MagicSkillUse(ptarget, ptarget, 2025, 1, 1, 0));
			Announcements.getInstance().announceByCustomMessage("lineage2.gameserver.model.instances.L2WeddingManagerMessage.announce", new String[]
			{
				player.getName(),
				ptarget.getName()
			});
			filename = "wedding/accepted.htm";
			replace = ptarget.getName();
			sendHtmlMessage(ptarget, filename, replace);
			return;
		}
		else if (player.isMaryRequest())
		{
			if (Config.WEDDING_FORMALWEAR && !isWearingFormalWear(player))
			{
				filename = "wedding/noformal.htm";
				sendHtmlMessage(player, filename, replace);
				return;
			}
			filename = "wedding/ask.htm";
			player.setMaryRequest(false);
			ptarget.setMaryRequest(false);
			replace = ptarget.getName();
			sendHtmlMessage(player, filename, replace);
			return;
		}
		else if (command.startsWith("AskWedding"))
		{
			if (Config.WEDDING_FORMALWEAR && !isWearingFormalWear(player))
			{
				filename = "wedding/noformal.htm";
				sendHtmlMessage(player, filename, replace);
				return;
			}
			else if (player.getAdena() < Config.WEDDING_PRICE)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			else
			{
				player.setMaryAccepted(true);
				ptarget.setMaryRequest(true);
				replace = ptarget.getName();
				filename = "wedding/requested.htm";
				player.reduceAdena(Config.WEDDING_PRICE, true);
				sendHtmlMessage(player, filename, replace);
				return;
			}
		}
		else if (command.startsWith("DeclineWedding"))
		{
			player.setMaryRequest(false);
			ptarget.setMaryRequest(false);
			player.setMaryAccepted(false);
			ptarget.setMaryAccepted(false);
			player.sendMessage("You declined");
			ptarget.sendMessage("Your partner declined");
			replace = ptarget.getName();
			filename = "wedding/declined.htm";
			sendHtmlMessage(ptarget, filename, replace);
			return;
		}
		else if (player.isMaryAccepted())
		{
			filename = "wedding/waitforpartner.htm";
			sendHtmlMessage(player, filename, replace);
			return;
		}
		sendHtmlMessage(player, filename, replace);
	}
	
	/**
	 * Method isWearingFormalWear.
	 * @param player Player
	 * @return boolean
	 */
	private static boolean isWearingFormalWear(Player player)
	{
		if ((player != null) && (player.getInventory() != null) && (player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST) == ItemTemplate.ITEM_ID_FORMAL_WEAR))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method sendHtmlMessage.
	 * @param player Player
	 * @param filename String
	 * @param replace String
	 */
	private void sendHtmlMessage(Player player, String filename, String replace)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile(filename);
		html.replace("%replace%", replace);
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
}
