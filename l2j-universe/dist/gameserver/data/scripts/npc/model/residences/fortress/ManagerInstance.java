/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package npc.model.residences.fortress;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.HtmlUtils;
import npc.model.residences.ResidenceManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManagerInstance extends ResidenceManager
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field REWARD_CYCLE.
	 */
	private static final long REWARD_CYCLE = 6 * 60 * 60;
	
	/**
	 * Constructor for ManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method setDialogs.
	 */
	@Override
	protected void setDialogs()
	{
		_mainDialog = "residence2/fortress/fortress_steward001.htm";
		_failDialog = "residence2/fortress/fortress_steward002.htm";
		_siegeDialog = "residence2/fortress/fortress_steward018.htm";
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
		if (command.equalsIgnoreCase("receive_report"))
		{
			int ownedTime = (int) ((System.currentTimeMillis() - getFortress().getOwnDate().getTimeInMillis()) / 60000L);
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			Fortress fortress = getFortress();
			if (fortress.getContractState() == Fortress.CONTRACT_WITH_CASTLE)
			{
				html.setFile("residence2/fortress/fortress_steward022.htm");
				html.replace("%castle_name%", HtmlUtils.htmlResidenceName(getFortress().getCastleId()));
				html.replaceNpcString("%contract%", NpcString.CONTRACT_STATE);
				long leftTime = (REWARD_CYCLE - (3600 - fortress.getCycleDelay()) - (fortress.getPaidCycle() * 3600)) / 60;
				html.replace("%rent_cost%", String.valueOf(Fortress.CASTLE_FEE));
				html.replace("%next_hour%", String.valueOf(leftTime / 60));
				html.replace("%next_min%", String.valueOf(leftTime % 60));
			}
			else
			{
				html.setFile("residence2/fortress/fortress_steward023.htm");
			}
			html.replaceNpcString("%time_remained%", NpcString.S1HOUR_S2MINUTE, ownedTime / 60, ownedTime % 60);
			player.sendPacket(html);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getCond.
	 * @param player Player
	 * @return int
	 */
	@Override
	protected int getCond(Player player)
	{
		Residence residence = getResidence();
		Clan residenceOwner = residence.getOwner();
		if ((residenceOwner != null) && (player.getClan() == residenceOwner))
		{
			if (residence.getSiegeEvent().isInProgress())
			{
				return COND_SIEGE;
			}
			return COND_OWNER;
		}
		return COND_FAIL;
	}
	
	/**
	 * Method getResidence.
	 * @return Residence
	 */
	@Override
	protected Residence getResidence()
	{
		return getFortress();
	}
	
	/**
	 * Method decoPacket.
	 * @return L2GameServerPacket
	 */
	@Override
	public L2GameServerPacket decoPacket()
	{
		return null;
	}
	
	/**
	 * Method getPrivUseFunctions.
	 * @return int
	 */
	@Override
	protected int getPrivUseFunctions()
	{
		return Clan.CP_CS_USE_FUNCTIONS;
	}
	
	/**
	 * Method getPrivSetFunctions.
	 * @return int
	 */
	@Override
	protected int getPrivSetFunctions()
	{
		return Clan.CP_CS_SET_FUNCTIONS;
	}
	
	/**
	 * Method getPrivDismiss.
	 * @return int
	 */
	@Override
	protected int getPrivDismiss()
	{
		return Clan.CP_CS_DISMISS;
	}
	
	/**
	 * Method getPrivDoors.
	 * @return int
	 */
	@Override
	protected int getPrivDoors()
	{
		return Clan.CP_CS_ENTRY_EXIT;
	}
}
