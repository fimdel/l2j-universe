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

import java.util.StringTokenizer;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class MercManagerInstance extends MerchantInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field COND_ALL_FALSE.
	 */
	private static int COND_ALL_FALSE = 0;
	/**
	 * Field COND_BUSY_BECAUSE_OF_SIEGE.
	 */
	private static int COND_BUSY_BECAUSE_OF_SIEGE = 1;
	/**
	 * Field COND_OWNER.
	 */
	private static int COND_OWNER = 2;
	
	/**
	 * Constructor for MercManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MercManagerInstance(int objectId, NpcTemplate template)
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
		int condition = validateCondition(player);
		if ((condition <= COND_ALL_FALSE) || (condition == COND_BUSY_BECAUSE_OF_SIEGE))
		{
			return;
		}
		if (condition == COND_OWNER)
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			String actualCommand = st.nextToken();
			String val = "";
			if (st.countTokens() >= 1)
			{
				val = st.nextToken();
			}
			if (actualCommand.equalsIgnoreCase("hire"))
			{
				if (val.equals(""))
				{
					return;
				}
				showShopWindow(player, Integer.parseInt(val), false);
			}
			else
			{
				super.onBypassFeedback(player, command);
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
		String filename = "castle/mercmanager/mercmanager-no.htm";
		int condition = validateCondition(player);
		if (condition == COND_BUSY_BECAUSE_OF_SIEGE)
		{
			filename = "castle/mercmanager/mercmanager-busy.htm";
		}
		else if (condition == COND_OWNER)
		{
			filename = "castle/mercmanager/mercmanager.htm";
		}
		player.sendPacket(new NpcHtmlMessage(player, this, filename, val));
	}
	
	/**
	 * Method validateCondition.
	 * @param player Player
	 * @return int
	 */
	private int validateCondition(Player player)
	{
		if (player.isGM())
		{
			return COND_OWNER;
		}
		if ((getCastle() != null) && (getCastle().getId() > 0))
		{
			if (player.getClan() != null)
			{
				if (getCastle().getSiegeEvent().isInProgress())
				{
					return COND_BUSY_BECAUSE_OF_SIEGE;
				}
				else if ((getCastle().getOwnerId() == player.getClanId()) && ((player.getClanPrivileges() & Clan.CP_CS_MERCENARIES) == Clan.CP_CS_MERCENARIES))
				{
					return COND_OWNER;
				}
			}
		}
		return COND_ALL_FALSE;
	}
}
