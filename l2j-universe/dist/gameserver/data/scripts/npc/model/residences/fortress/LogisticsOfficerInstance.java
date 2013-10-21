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
package npc.model.residences.fortress;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LogisticsOfficerInstance extends FacilityManagerInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field SUPPLY_NPC.
	 */
	private static final int[] SUPPLY_NPC = new int[]
	{
		35665,
		35697,
		35734,
		35766,
		35803,
		35834
	};
	/**
	 * Field ITEM_ID. (value is 9910)
	 */
	private static final int ITEM_ID = 9910;
	
	/**
	 * Constructor for LogisticsOfficerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public LogisticsOfficerInstance(int objectId, NpcTemplate template)
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
		Fortress fortress = getFortress();
		if (!player.isClanLeader() || (fortress.getOwnerId() != player.getClanId()))
		{
			showChatWindow(player, "residence2/fortress/fortress_not_authorized.htm");
			return;
		}
		if (command.equalsIgnoreCase("guardInfo"))
		{
			if (fortress.getContractState() != Fortress.CONTRACT_WITH_CASTLE)
			{
				showChatWindow(player, "residence2/fortress/fortress_supply_officer005.htm");
				return;
			}
			showChatWindow(player, "residence2/fortress/fortress_supply_officer002.htm", "%guard_buff_level%", fortress.getFacilityLevel(Fortress.GUARD_BUFF));
		}
		else if (command.equalsIgnoreCase("supplyInfo"))
		{
			if (fortress.getContractState() != Fortress.CONTRACT_WITH_CASTLE)
			{
				showChatWindow(player, "residence2/fortress/fortress_supply_officer005.htm");
				return;
			}
			showChatWindow(player, "residence2/fortress/fortress_supply_officer009.htm", "%supply_count%", fortress.getSupplyCount());
		}
		else if (command.equalsIgnoreCase("rewardInfo"))
		{
			showChatWindow(player, "residence2/fortress/fortress_supply_officer010.htm", "%blood_oaths%", fortress.getRewardCount());
		}
		else if (command.equalsIgnoreCase("receiveSupply"))
		{
			String filename;
			if (fortress.getSupplyCount() > 0)
			{
				filename = "residence2/fortress/fortress_supply_officer016.htm";
				NpcInstance npc = NpcHolder.getInstance().getTemplate(SUPPLY_NPC[fortress.getSupplyCount() - 1]).getNewInstance();
				npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp());
				npc.spawnMe(new Location(getX() - 23, getY() + 41, getZ()));
			}
			else
			{
				filename = "residence2/fortress/fortress_supply_officer017.htm";
			}
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile(filename);
			player.sendPacket(html);
		}
		else if (command.equalsIgnoreCase("receiveRewards"))
		{
			String filename;
			int count = fortress.getRewardCount();
			if (count > 0)
			{
				filename = "residence2/fortress/fortress_supply_officer013.htm";
				fortress.setRewardCount(0);
				fortress.setJdbcState(JdbcEntityState.UPDATED);
				fortress.update();
				Functions.addItem(player, ITEM_ID, count);
			}
			else
			{
				filename = "residence2/fortress/fortress_supply_officer014.htm";
			}
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile(filename);
			player.sendPacket(html);
		}
		else if (command.equalsIgnoreCase("toLevel1"))
		{
			buyFacility(player, Fortress.GUARD_BUFF, 1, 100000);
		}
		else if (command.equalsIgnoreCase("toLevel2"))
		{
			buyFacility(player, Fortress.GUARD_BUFF, 2, 150000);
		}
		else
		{
			super.onBypassFeedback(player, command);
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
		showChatWindow(player, "residence2/fortress/fortress_supply_officer001.htm");
	}
}
