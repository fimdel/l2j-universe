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

import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import bosses.BelethManager;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class BelethCoffinInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field RING. (value is 10314)
	 */
	private static final int RING = 10314;
	
	/**
	 * Constructor for BelethCoffinInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public BelethCoffinInstance(int objectId, NpcTemplate template)
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
		StringTokenizer st = new StringTokenizer(command);
		if (st.nextToken().equals("request_ring"))
		{
			if (!BelethManager.isRingAvailable())
			{
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Stone Coffin:<br><br>Ring is not available. Get lost!"));
				return;
			}
			if ((player.getParty() == null) || (player.getParty().getCommandChannel() == null))
			{
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Stone Coffin:<br><br>You are not allowed to take the ring. Are are not the group or Command Channel."));
				return;
			}
			if (player.getParty().getCommandChannel().getChannelLeader() != player)
			{
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Stone Coffin:<br><br>You are not leader or the Command Channel."));
				return;
			}
			CommandChannel channel = player.getParty().getCommandChannel();
			Functions.addItem(player, RING, 1);
			SystemMessage smsg = new SystemMessage(SystemMessage.S1_HAS_OBTAINED_S2);
			smsg.addString(player.getName());
			smsg.addItemName(RING);
			channel.broadCast(smsg);
			BelethManager.setRingAvailable(false);
			deleteMe();
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
