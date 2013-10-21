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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MammonsInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(MammonsInstance.class);
	/**
	 * Field ANCIENT_ADENA_ID. (value is 5575)
	 */
	private static final int ANCIENT_ADENA_ID = 5575;
	/**
	 * Field MAMMONS_HTML_PATH. (value is ""mammons/"")
	 */
	public static final String MAMMONS_HTML_PATH = "mammons/";
	
	/**
	 * Constructor for MammonsInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MammonsInstance(int objectId, NpcTemplate template)
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
		super.onBypassFeedback(player, command);
		if (command.startsWith("bmarket"))
		{
			ItemInstance ancientAdena = player.getInventory().getItemByItemId(ANCIENT_ADENA_ID);
			long ancientAdenaAmount = ancientAdena == null ? 0 : ancientAdena.getCount();
			int val = Integer.parseInt(command.substring(11, 12).trim());
			if (command.length() > 12)
			{
				val = Integer.parseInt(command.substring(11, 13).trim());
			}
			switch (val)
			{
				case 1:
					long ancientAdenaConvert = 0;
					try
					{
						ancientAdenaConvert = Long.parseLong(command.substring(13).trim());
					}
					catch (NumberFormatException e)
					{
						player.sendMessage(new CustomMessage("common.IntegerAmount", player));
						return;
					}
					catch (StringIndexOutOfBoundsException e)
					{
						player.sendMessage(new CustomMessage("common.IntegerAmount", player));
						return;
					}
					if ((ancientAdenaAmount < ancientAdenaConvert) || (ancientAdenaConvert < 1))
					{
						player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
						return;
					}
					if (player.getInventory().destroyItemByItemId(ANCIENT_ADENA_ID, ancientAdenaConvert))
					{
						player.addAdena(ancientAdenaConvert);
						player.sendPacket(SystemMessage2.removeItems(ANCIENT_ADENA_ID, ancientAdenaConvert));
						player.sendPacket(SystemMessage2.obtainItems(57, ancientAdenaConvert, 0));
					}
					break;
				default:
					showChatWindow(player, "blkmrkt.htm");
					break;
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
		int npcId = getTemplate().npcId;
		String filename = MAMMONS_HTML_PATH;
		switch (npcId)
		{
			case 33511:
				filename += "priestmam.htm";
				break;
			case 31092:
				filename += "blkmrkt.htm";
				break;
			case 31113:
				filename += "merchmamm.htm";
				break;
			case 31126:
				filename += "blksmithmam.htm";
				break;
			default:
				filename = getHtmlPath(npcId, val, player);
		}
		player.sendPacket(new NpcHtmlMessage(player, this, filename, val));
	}
}
