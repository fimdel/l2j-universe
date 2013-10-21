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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FameManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for FameManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public FameManagerInstance(int objectId, NpcTemplate template)
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
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken();
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		if (actualCommand.equalsIgnoreCase("PK_Count"))
		{
			if (player.getFame() >= 5000)
			{
				if (player.getPkKills() > 0)
				{
					player.setFame(player.getFame() - 5000, "PK_Count");
					player.setPkKills(player.getPkKills() - 1);
					html.setFile("default/" + getNpcId() + "-okpk.htm");
				}
				else
				{
					html.setFile("default/" + getNpcId() + "-nohavepk.htm");
				}
			}
			else
			{
				html.setFile("default/" + getNpcId() + "-nofame.htm");
			}
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("CRP"))
		{
			if ((player.getClan() == null) || (player.getClassId().level() < 2) || (player.getClan().getLevel() < 5))
			{
				html.setFile("default/" + getNpcId() + "-noclancrp.htm");
			}
			else if (player.getFame() < 1000)
			{
				html.setFile("default/" + getNpcId() + "-nofame.htm");
			}
			else
			{
				player.setFame(player.getFame() - 1000, "CRP");
				player.getClan().incReputation(50, false, "FameManager from " + player.getName());
				player.getClan().broadcastToOnlineMembers(new PledgeShowInfoUpdate(player.getClan()));
				player.sendPacket(Msg.ACQUIRED_50_CLAN_FAME_POINTS);
				html.setFile("default/" + getNpcId() + "-okclancrp.htm");
			}
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
