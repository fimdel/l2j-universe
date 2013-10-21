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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class FacilityManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for FacilityManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public FacilityManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method buyFacility.
	 * @param player Player
	 * @param type int
	 * @param lvl int
	 * @param price long
	 * @return boolean
	 */
	protected boolean buyFacility(Player player, int type, int lvl, long price)
	{
		Fortress fortress = getFortress();
		if ((player.getClanPrivileges() & Clan.CP_CS_MANAGE_SIEGE) != Clan.CP_CS_MANAGE_SIEGE)
		{
			showChatWindow(player, "residence2/fortress/fortress_not_authorized.htm");
			return false;
		}
		if (fortress.getContractState() != Fortress.CONTRACT_WITH_CASTLE)
		{
			showChatWindow(player, "residence2/fortress/fortress_supply_officer005.htm");
			return false;
		}
		if (fortress.getFacilityLevel(type) >= lvl)
		{
			showChatWindow(player, "residence2/fortress/fortress_already_upgraded.htm");
			return false;
		}
		if (player.consumeItem(ItemTemplate.ITEM_ID_ADENA, price))
		{
			fortress.setFacilityLevel(type, lvl);
			fortress.setJdbcState(JdbcEntityState.UPDATED);
			fortress.update();
			showChatWindow(player, "residence2/fortress/fortress_supply_officer006.htm");
			return true;
		}
		showChatWindow(player, "residence2/fortress/fortress_not_enough_money.htm");
		return false;
	}
}
