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

import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.instances.MerchantInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ClassMasterInstance extends MerchantInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for ClassMasterInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ClassMasterInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Field jobLevel.
	 */
	int jobLevel = 0;
	
	/**
	 * Method makeMessage.
	 * @param player Player
	 * @return String
	 */
	private String makeMessage(Player player)
	{
		ClassId classId = player.getClassId();
		jobLevel = player.getClassLevel();
		int level = player.getLevel();
		StringBuilder html = new StringBuilder();
		if (Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			jobLevel = 5;
		}
		if ((((level >= 20) && (jobLevel == 1)) || ((level >= 40) && (jobLevel == 2)) || ((level >= 76) && (jobLevel == 3)) || ((level >= 85) && (jobLevel == 4))) && Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel]);
			if (Config.CLASS_MASTERS_PRICE_LIST[jobLevel] > 0)
			{
				html.append("Price: ").append(Util.formatAdena(Config.CLASS_MASTERS_PRICE_LIST[jobLevel])).append(' ').append(item.getName()).append("<br1>");
			}
			for (ClassId cid : ClassId.VALUES)
			{
				if (cid == ClassId.INSPECTOR)
				{
					continue;
				}
				if (cid.childOf(classId) && (cid.getClassLevel().ordinal() == (classId.getClassLevel().ordinal() + 1)))
				{
					html.append("<a action=\"bypass -h npc_").append(getObjectId()).append("_change_class ").append(cid.getId()).append(' ').append(Config.CLASS_MASTERS_PRICE_LIST[jobLevel]).append("\">").append(HtmlUtils.htmlClassName(cid.getId())).append("</a><br>");
				}
			}
			player.sendPacket(new NpcHtmlMessage(player, this).setHtml(html.toString()));
		}
		else
		{
			switch (jobLevel)
			{
				case 1:
					html.append("Come back here when you reached level 20 to change your class.");
					break;
				case 2:
					html.append("Come back here when you reached level 40 to change your class.");
					break;
				case 3:
					html.append("Come back here when you reached level 76 to change your class.");
					break;
				case 4:
					html.append("Come back here when you reached level 85 to change your class.");
					break;
				case 5:
					html.append("There is no class changes for you any more.");
					break;
			}
		}
		return html.toString();
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
		NpcHtmlMessage msg = new NpcHtmlMessage(player, this);
		msg.setFile("custom/31860.htm");
		msg.replace("%classmaster%", makeMessage(player));
		player.sendPacket(msg);
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
		StringTokenizer st = new StringTokenizer(command);
		if (st.nextToken().equals("change_class"))
		{
			int val = Integer.parseInt(st.nextToken());
			long price = Long.parseLong(st.nextToken());
			if (player.getInventory().destroyItemByItemId(Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel], price))
			{
				changeClass(player, val);
			}
			else if (Config.CLASS_MASTERS_PRICE_ITEM_LIST[jobLevel] == 57)
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			}
			else
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method changeClass.
	 * @param player Player
	 * @param val int
	 */
	private void changeClass(Player player, int val)
	{
		if (player.getClassId().isOfLevel(ClassLevel.Second))
		{
			player.sendPacket(SystemMsg.CONGRATULATIONS__YOUVE_COMPLETED_YOUR_THIRDCLASS_TRANSFER_QUEST);
		}
		else
		{
			player.sendPacket(SystemMsg.CONGRATULATIONS__YOUVE_COMPLETED_A_CLASS_TRANSFER);
		}
		player.setClassId(val, false, false);
		player.broadcastCharInfo();
	}
}
