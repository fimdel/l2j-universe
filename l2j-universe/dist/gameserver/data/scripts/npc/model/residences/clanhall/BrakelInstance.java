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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.TimeUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BrakelInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for BrakelInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BrakelInstance(int objectId, NpcTemplate template)
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
		ClanHall clanhall = ResidenceHolder.getInstance().getResidence(ClanHall.class, 21);
		if (clanhall == null)
		{
			return;
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile("residence2/clanhall/partisan_ordery_brakel001.htm");
		html.replace("%next_siege%", TimeUtils.toSimpleFormat(clanhall.getSiegeDate().getTimeInMillis()));
		player.sendPacket(html);
	}
}
