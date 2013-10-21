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
package npc.model.residences.clanhall;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuctionedManagerInstance extends ManagerInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for AuctionedManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public AuctionedManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method setDialogs.
	 */
	@Override
	protected void setDialogs()
	{
		_mainDialog = getTemplate().getAIParams().getString("main_dialog", "residence2/clanhall/black001.htm");
		_failDialog = getTemplate().getAIParams().getString("fail_dialog", "residence2/clanhall/black002.htm");
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
			return COND_OWNER;
		}
		return COND_FAIL;
	}
}
